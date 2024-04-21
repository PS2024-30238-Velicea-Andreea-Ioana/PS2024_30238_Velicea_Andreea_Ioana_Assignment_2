package com.example.untoldpsproject.strategies;

import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class PDFFileStrategy implements GenerateFileStrategy {

    @Override
    public void generateFile(Order order) {
        String fileName = "order.pdf";
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Order Details");
            contentStream.newLine();
            contentStream.newLine();
            contentStream.showText("Order made by: " + order.getUser().getFirstName() + " " + order.getUser().getLastName());
            contentStream.newLine();
            contentStream.newLine();
            contentStream.showText("Tickets: ");
            contentStream.newLine();
            contentStream.newLine();
            for (int i = 0; i < order.getTickets().size(); i++) {
                Ticket ticket = order.getTickets().get(i);
                contentStream.showText("Ticket " + (i + 1) + ":");
                contentStream.newLine();
                contentStream.showText("Type: " + ticket.getCategory().getTip());
                contentStream.newLine();
                contentStream.showText("Start Date: " + ticket.getCategory().getStartDate());
                contentStream.newLine();
                contentStream.showText("Finish Date: " + ticket.getCategory().getFinishDate());
                contentStream.newLine();
                contentStream.showText("Price: " + ticket.getDiscountedPrice());
                contentStream.newLine();
                contentStream.showText("Ticket Number: " + ticket.getId());
                contentStream.newLine();
                contentStream.newLine();
            }
            contentStream.showText("Total amount for order " + order.getId() + ": " + order.getTotalPrice());
            contentStream.endText();

            contentStream.close();
            document.save(fileName);
            document.close();
            System.out.println("Order generated successfully. Check '" + fileName + "'.");
        } catch (IOException e) {
            System.out.println("An error occurred while generating the order.");
            e.printStackTrace();
        }
    }
}