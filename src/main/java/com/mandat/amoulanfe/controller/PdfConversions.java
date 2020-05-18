package com.mandat.amoulanfe.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/utils/pdf/conversions")
public class PdfConversions {

    @ApiOperation(value = "Transformation d'un pdf en html")
    @PostMapping(value = "/pdfToHtml")
    @PreAuthorize("hasRole('USER')")
    private void generateHTMLFromPDF(@RequestParam("file") MultipartFile file) throws IOException, ParserConfigurationException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        File newFile = new File(fileName);
        boolean isCreated = newFile.createNewFile();

        if(isCreated) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(newFile)) {
                fileOutputStream.write(file.getBytes());
            }

            PDDocument pdf = PDDocument.load(newFile);
            Writer output = new PrintWriter("src/output/"+fileName+".html", StandardCharsets.UTF_8);

            new PDFDomTree().writeText(pdf, output);

            output.close();
            log.info("Le fichier " + fileName + " à été converti en html avec succès");
        } else {
            log.error("Impossible de convertir " + fileName + " en html");
        }
    }
}
