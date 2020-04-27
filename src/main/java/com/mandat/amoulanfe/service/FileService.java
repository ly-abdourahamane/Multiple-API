package com.mandat.amoulanfe.service;

import com.mandat.amoulanfe.domain.FileDomain;
import com.mandat.amoulanfe.domain.FileStorageProperties;
import com.mandat.amoulanfe.exception.FileNotFoundException;
import com.mandat.amoulanfe.exception.FileStoreException;
import com.mandat.amoulanfe.repository.FileRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Setter
@Slf4j
@Service
public class FileService {

    private FileRepository fileRepository;
    private final Path fileStorageLocation;

    @Autowired
    public FileService(FileRepository fileRepository, FileStorageProperties fileStorageProperties) {
        this.fileRepository = fileRepository;

        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStoreException("Impossible de créer le dossier de stockage des fichiers", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        FileDomain fileDomain = new FileDomain();

        try {
            if(fileName.contains("..")) {
                throw new FileStoreException("Le nom du fichier est incorrect " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileDomain DBFile = fileRepository.findByName(fileName);

            //Mise à jour du fichier s'il existe
            if(DBFile != null) {
                fileDomain.setId(DBFile.getId());
                log.info("Le fichier " + fileName + " existe déjà, il a été mis à jour");
            }

            fileDomain.setName(fileName);
            fileDomain.setType(file.getContentType());
            fileDomain.setBuffer(file.getBytes());
            this.fileRepository.save(fileDomain);

            log.info("Le fichier " + fileName + " à été bien chargé");
            return fileName;
        } catch (IOException ex) {
            throw new FileStoreException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("Fichier non trouvé " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("Fichier non truvé " + fileName, ex);
        }
    }

    public FileDomain getFileByID(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("Fichier non trouvé avec l'id  " + fileId));
    }

    public FileDomain findFileByName(String name) {
        return fileRepository.findByName(name);
    }

    public void zipDownload(List<String> fileNameList, HttpServletResponse response) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        String fileBasePath = "UPLOAD-FILES/";
        String zipFileName = "filesZip";

        for (String fileName : fileNameList) {
            FileSystemResource resource = new FileSystemResource(fileBasePath + fileName);
            ZipEntry zipEntry = new ZipEntry(Objects.requireNonNull(resource.getFilename()));
            zipEntry.setSize(resource.contentLength());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(resource.getInputStream(), zipOut);
            zipOut.closeEntry();
        }

        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFileName + "\"");
    }
}
