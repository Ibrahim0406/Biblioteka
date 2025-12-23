package com.biblioteka.util;

import com.biblioteka.model.Book;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.FileOutputStream;
import java.util.List;

public class PDFExporter {

    public static void exportBooksToPDF(List<Book> books, String filename) throws Exception {
        if (books == null) {
            throw new IllegalArgumentException("Lista knjiga ne može biti null");
        }
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Ime fajla ne može biti prazno");
        }

        FileOutputStream fos = null;
        PdfWriter writer = null;
        PdfDocument pdfDoc = null;
        com.itextpdf.layout.Document document = null;

        try {
            fos = new FileOutputStream(filename);
            writer = new PdfWriter(fos);
            pdfDoc = new PdfDocument(writer);
            document = new com.itextpdf.layout.Document(pdfDoc);

            // Boje
            DeviceRgb headerColor = new DeviceRgb(52, 73, 94);

            // Naslov
            Paragraph title = new Paragraph("IZVJEŠTAJ O KNJIGAMA")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(title);

            // Datum
            Paragraph date = new Paragraph("Datum: " + new java.util.Date().toString())
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontColor(ColorConstants.GRAY)
                    .setMarginBottom(20);
            document.add(date);

            // Tabela
            float[] columnWidths = {1f, 3f, 2.5f, 2f, 1.5f, 1.5f};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Zaglavlja
            String[] headers = {"ID", "Naslov", "Autor", "ISBN", "Godina", "Cijena (KM)"};
            for (String header : headers) {
                Cell cell = new Cell()
                        .add(new Paragraph(header).setBold().setFontColor(ColorConstants.WHITE))
                        .setBackgroundColor(headerColor)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(8);
                table.addHeaderCell(cell);
            }

            // Podaci
            for (Book book : books) {
                if (book != null) {
                    table.addCell(createCell(String.valueOf(book.getId()), TextAlignment.CENTER));
                    table.addCell(createCell(book.getTitle() != null ? book.getTitle() : "", TextAlignment.LEFT));
                    table.addCell(createCell(book.getAuthor() != null ? book.getAuthor() : "", TextAlignment.LEFT));
                    table.addCell(createCell(book.getIsbn() != null ? book.getIsbn() : "", TextAlignment.CENTER));
                    table.addCell(createCell(String.valueOf(book.getYear()), TextAlignment.CENTER));
                    table.addCell(createCell(String.format("%.2f", book.getPrice()), TextAlignment.RIGHT));
                }
            }

            document.add(table);

            // Statistika
            Paragraph stats = new Paragraph("\n\nUkupno knjiga: " + books.size())
                    .setFontSize(12)
                    .setBold();
            document.add(stats);
        } finally {
            if (document != null) {
                document.close();
            }
            // PdfDocument i PdfWriter se zatvaraju automatski kada se Document zatvori
        }
    }

    private static Cell createCell(String content, TextAlignment alignment) {
        return new Cell()
                .add(new Paragraph(content).setFontSize(9))
                .setTextAlignment(alignment)
                .setPadding(5);
    }
}