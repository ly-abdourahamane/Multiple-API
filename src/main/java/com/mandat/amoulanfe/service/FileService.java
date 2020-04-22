package com.mandat.amoulanfe.service;

import com.mandat.amoulanfe.domain.FileDomain;
import com.mandat.amoulanfe.domain.FileStorageProperties;
import com.mandat.amoulanfe.exception.FileNotFoundException;
import com.mandat.amoulanfe.exception.FileStoreException;
import com.mandat.amoulanfe.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Slf4j
@Service
public class FileService {

    private FileRepository fileRepository;
    private final Path fileStorageLocation;

    @Autowired
    public FileService(FileRepository fileRepository, FileStorageProperties fileStorageProperties) {
        this.fileRepository = fileRepository;

        this.fileStorageLocation =  Paths.get("dir"); /*Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize(); */

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStoreException("Impossible de créer le dossier de stockage des fichiers", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if(fileName.contains("..")) {
                throw new FileStoreException("Le nom du fichier est incorrect " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

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
}
