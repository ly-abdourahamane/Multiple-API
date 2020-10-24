package com.mandat.amoulanfe.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
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
@RequestMapping("/api/v1/pdf/conversions")
public class PdfConversions {

    @ApiOperation(value = "Transformation d'un pdf en html")
    @PostMapping(value = "/htmlFromPdf")
    @PreAuthorize("hasRole('USER')")
    private void generateHTMLFromPDF(@RequestParam("file") MultipartFile file) throws IOException, ParserConfigurationException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String htmlFileName = fileName.split("\\.")[0] + ".html";
        File newFile = new File(fileName);
        boolean isCreated = newFile.createNewFile();

        if(isCreated) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(newFile)) {
                fileOutputStream.write(file.getBytes());
            }

            PDDocument pdf = PDDocument.load(newFile);
            Writer output = new PrintWriter("src/output/"+htmlFileName);

            new PDFDomTree().writeText(pdf, output);

            output.close();
            log.info("Le fichier " + fileName + " à été converti en html avec succès");
        } else {
            log.error("Impossible de convertir " + fileName + " en html");
        }
    }

    @ApiOperation(value = "Transformation d'un html en pdf")
    @PostMapping(value = "/pdfFromHtml")
    @PreAuthorize("hasRole('USER')")
    private static void generatePdfFromHTML() throws IOException, DocumentException {
        Document document = new Document();
        String filename = "test.pdf";
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("src/output/test.html"));
        document.open();

        XMLWorkerHelper xmlWorkerHelper = XMLWorkerHelper.getInstance();
        xmlWorkerHelper.parseXHtml(pdfWriter, document, new FileInputStream("src/output/test.pdf"));
        document.close();
    }

    @ApiOperation(value = "mation d'un html en pdf")
    @PostMapping(value = "/docsFromPdf")
    @PreAuthorize("hasRole('USER')")
    private void docxFromPdf() throws IOException {
        XWPFDocument doc = new XWPFDocument();
        String fileName = "IVVQ_Cours_1.pdf";
        PdfReader reader = new PdfReader(fileName);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);

        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            TextExtractionStrategy strategy =
                    parser.processContent(i, new SimpleTextExtractionStrategy());
            String text = strategy.getResultantText();
            XWPFParagraph p = doc.createParagraph();
            XWPFRun run = p.createRun();
            run.setText(text);
            run.addBreak(BreakType.PAGE);
        }

        FileOutputStream out = new FileOutputStream("src/output/pdf.docx");
        doc.write(out);

        reader.close();
        doc.close();
    }
}
