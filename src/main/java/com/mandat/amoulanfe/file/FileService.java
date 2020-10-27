package com.mandat.amoulanfe.file;

import com.mandat.amoulanfe.exception.FileNotFoundException;
import com.mandat.amoulanfe.exception.FileStoreException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Setter
@Slf4j
@Service
public class FileService {

    private final Path fileStorageLocation;
    private FileRepository fileRepository;

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
        FileUpload fileUpload = new FileUpload();

        try {
            if (fileName.contains("..")) {
                throw new FileStoreException("Le nom du fichier est incorrect " + fileName);
            }

            //TODO: CHOISIR DE STOCKER LES FICHIERS DANS UN DISQUE OUBIEN DANS LA BD

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileUpload DBFile = fileRepository.findByName(fileName)
                    .orElse(null);

            //Mise à jour du fichier s'il existe
            if (DBFile != null) {
                fileUpload.setId(DBFile.getId());
                log.info("Le fichier " + fileName + " existe déjà, il a été mis à jour");
            }

            fileUpload.setName(fileName);
            fileUpload.setType(file.getContentType());
            fileUpload.setBuffer(file.getBytes());
            fileUpload.setSize(file.getSize());
            this.fileRepository.save(fileUpload);

            if (DBFile == null) {
                log.info("Le fichier " + fileName + " à été bien chargé");
            }

            return fileName;
        } catch (IOException ex) {
            throw new FileStoreException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("Fichier non trouvé " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("Fichier non truvé " + fileName, ex);
        }
    }

    public FileUpload getFileByID(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("Fichier non trouvé avec l'id  " + fileId));
    }

    public FileUpload findFileByName(String name) {
        return fileRepository.findByName(name)
                .orElseThrow(() -> new FileNotFoundException("Le fichier [" + name + "] est introuvable"));
    }

    public List<FileUploadDTO> findAllFilesInfos() {
        List<FileUpload> fileUploads = this.fileRepository.findAll();
        List<FileUploadDTO> fileUploadDTOS = new ArrayList<>();

        fileUploads.forEach(elem -> fileUploadDTOS.add(new FileUploadDTO(elem.getId(), elem.getName(),
                elem.getType(), elem.getSize(), elem.getCreationDate())));

        return fileUploadDTOS;
    }

    public void zipDownload(List<String> fileNameList, HttpServletResponse response) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        String fileBasePath = "FILES-STORE/";
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

    public void deleteFileByID(Long id) {
        this.fileRepository.deleteById(id);
    }
}
