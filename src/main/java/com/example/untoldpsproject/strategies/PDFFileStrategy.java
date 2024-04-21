package com.example.untoldpsproject.strategies;

import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.FileNotFoundException;

public class PDFFileStrategy implements GenerateFileStrategy {

    @Override
    public void generateFile(Order order) {
        String fileName = "order.pdf";

        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new File(fileName)));
            Document document = new Document(pdfDocument, PageSize.A4);

            PdfFont font = PdfFontFactory.createFont("Helvetica");

            // Set colors
            Color primaryColor = new DeviceRgb(102, 0, 153); // Purple
            Color textColor = new DeviceRgb(0, 0, 0); // Black

            // Adding order details
            Paragraph orderDetails = new Paragraph("Order Details")
                    .setFont(font)
                    .setFontSize(20)
                    .setFontColor(primaryColor)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(orderDetails);

            document.add(new Paragraph("Order made by: " + order.getUser().getFirstName() + " " + order.getUser().getLastName())
                    .setFont(font)
                    .setFontSize(12)
                    .setFontColor(textColor));

            // Adding tickets
            Paragraph ticketsParagraph = new Paragraph("Tickets:")
                    .setFont(font)
                    .setFontSize(16)
                    .setFontColor(primaryColor);
            document.add(ticketsParagraph);

            for (Ticket ticket : order.getTickets()) {
                // Create a custom-designed ticket
                Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                        .setWidth(UnitValue.createPercentValue(80))
                        .setMarginBottom(20);

                // Add ticket details
                table.addCell(new Paragraph("Ticket ID: " + ticket.getId())
                        .setFont(font)
                        .setFontSize(12)
                        .setFontColor(textColor));

                table.addCell(new Paragraph("Category: " + ticket.getCategory().getTip())
                        .setFont(font)
                        .setFontSize(12)
                        .setFontColor(textColor));

                table.addCell(new Paragraph("Valid from: " + ticket.getCategory().getStartDate())
                        .setFont(font)
                        .setFontSize(12)
                        .setFontColor(textColor));

                table.addCell(new Paragraph("Valid until: " + ticket.getCategory().getFinishDate())
                        .setFont(font)
                        .setFontSize(12)
                        .setFontColor(textColor));
                table.addCell(new Paragraph("Price: " + ticket.getDiscountedPrice())
                        .setFont(font)
                        .setFontSize(12)
                        .setFontColor(textColor));
                document.add(table);
                // Add some space between tickets
                document.add(new Paragraph("\n"));
            }

            // Adding total amount
            document.add(new Paragraph("Total amount for order " + order.getId() + ": " + order.getTotalPrice())
                    .setFont(font)
                    .setFontSize(16)
                    .setFontColor(primaryColor)
                    .setMarginTop(20));

            document.close();
            System.out.println("Order generated successfully. Check '" + fileName + "'.");
        } catch (IOException e) {
            System.out.println("An error occurred while generating the order.");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

}