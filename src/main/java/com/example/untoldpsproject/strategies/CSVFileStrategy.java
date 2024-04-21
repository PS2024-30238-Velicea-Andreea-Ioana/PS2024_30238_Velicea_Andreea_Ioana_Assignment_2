package com.example.untoldpsproject.strategies;

import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

@Service
public class CSVFileStrategy implements GenerateFileStrategy{
    @Override
    public void generateFile(Order order) {
        String fileName = "order.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Ticket,Type,Start Date,Finish Date,Price,Ticket Number\n");
            for (int i = 0; i < order.getTickets().size(); i++) {
                Ticket ticket = order.getTickets().get(i);
                writer.write((i + 1) + ",");
                writer.write(ticket.getCategory().getTip() + ",");
                writer.write(ticket.getCategory().getStartDate() + ",");
                writer.write(ticket.getCategory().getFinishDate() + ",");
                writer.write(ticket.getDiscountedPrice() + ",");
                writer.write(ticket.getId() + "\n");
            }
            System.out.println("Order CSV generated successfully. Check '" + fileName + "'.");
        } catch (IOException e) {
            System.out.println("An error occurred while generating the order CSV.");
            e.printStackTrace();
        }
    }
}
