package com.mandat.amoulanfe.controller;

import com.mandat.amoulanfe.domain.FileUpload;
import com.mandat.amoulanfe.domain.UploadFileResponse;
import com.mandat.amoulanfe.dto.FileUploadDTO;
import com.mandat.amoulanfe.service.FileService;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Setter
@RestController
@RequestMapping("/api/v1/utils/files/")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "Chargement d'un fichier")
    @PostMapping(value = "/upload/one")
    @PreAuthorize("hasRole('USER')")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/utilsAPI/files/download/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @ApiOperation(value = "Chargement de plusieurs fichiers")
    @PostMapping("/upload/multiple")
    @PreAuthorize("hasRole('USER')")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Téléchargement d'un fichier")
    @GetMapping("/download/{fileName:.+}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileService.loadFile(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Type de contenu par defaut
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @ApiOperation(value = "Téléchargement des fichiers dont les noms sont donnés en paramètres dans un zip")
    @GetMapping(value = "zip-download" , produces="application/zip")
    @PreAuthorize("hasRole('USER')")
    public void zipDownload(@RequestParam("name") List<String> fileNameList, HttpServletResponse response) throws IOException {
        fileService.zipDownload(fileNameList, response);
    }

    @ApiOperation(value = "Téléchargement d'un fichier à partir de son id")
    @GetMapping("/download/v2/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        FileUpload fileUpload = fileService.getFileByID(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileUpload.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileUpload.getName() + "\"")
                .body(new ByteArrayResource(fileUpload.getBuffer()));
    }

    @ApiOperation(value = "Téléchargement d'un fichier à partir de son nom")
    @GetMapping("/download/v3/{fileName}")
    public ResponseEntity<Resource> downloadFileByName(@PathVariable String fileName) {
        FileUpload fileUpload = fileService.findFileByName(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileUpload.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileUpload.getName() + "\"")
                .body(new ByteArrayResource(fileUpload.getBuffer()));
    }

    @ApiOperation(value = "Téléchargement d'un fichier à partir de son nom")
    @GetMapping("/all/infos")
    public List<FileUploadDTO> findAllFilesInfos() {
        return this.fileService.findAllFilesInfos();
    }

    @ApiOperation(value = "Suppression d'un fichier à partir de son id")
    @GetMapping("/delete/{fileID}")
    public void delete(@PathVariable Long fileID) {
        this.fileService.deleteFileByID(fileID);
    }
}
