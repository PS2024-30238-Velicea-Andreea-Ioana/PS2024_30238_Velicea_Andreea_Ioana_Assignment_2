package com.example.untoldpsproject.strategies;

import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TXTFileStrategy implements GenerateFileStrategy{
    @Override
    public String generateFile(Order order) {
        String fileName = "order.txt";
        try {
            String filePath = GenerateFileStrategy.directoryPath + File.separator + fileName;
            FileWriter writer = new FileWriter(filePath);
            writer.write("Order Details\n\n");

            writer.write("Order made by: " + order.getUser().getFirstName() + " " + order.getUser().getLastName() + "\n\n");

            writer.write("Tickets: \n\n");
            for (int i = 0; i < order.getTickets().size(); i++) {
                Ticket ticket = order.getTickets().get(i);
                writer.write("Ticket " + (i + 1) + ":\n");
                writer.write("Type: " + ticket.getCategory().getTip() + "\n");
                writer.write("Start Date: " + ticket.getCategory().getStartDate() + "\n");
                writer.write("Finish Date: " + ticket.getCategory().getFinishDate() + "\n");
                writer.write("Price: " + ticket.getDiscountedPrice()+ "\n");
                writer.write("Ticket Number: " + ticket.getId() + "\n\n");
            }
            writer.write("Total amount for order " + order.getId() + ": " + order.getTotalPrice());
            writer.close();
            System.out.println("Order generated successfully. Check '" + fileName + "'.");
            return filePath;
        } catch (IOException e) {
            System.out.println("An error occurred while generating the order.");
            e.printStackTrace();
        }
        return null;
    }
}
