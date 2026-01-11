package com.example.skipro.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple utility to extract text from a PDF file using Apache PDFBox.
 *
 * Usage (example):
 * PdfTextExtractor.extractToFile(Path.of("SkiPro.pdf"), Path.of("build/SkiPro_extracted.txt"));
 */
public final class PdfTextExtractor {
    private PdfTextExtractor() {
    }

    public static void extractToFile(Path pdfPath, Path outputTxtPath) throws IOException {
        try (PDDocument doc = PDDocument.load(pdfPath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            Files.createDirectories(outputTxtPath.getParent());
            Files.writeString(outputTxtPath, text, StandardCharsets.UTF_8);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: PdfTextExtractor <input.pdf> <output.txt>");
            System.exit(1);
        }
        extractToFile(Path.of(args[0]), Path.of(args[1]));
    }
}

