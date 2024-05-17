package com.example.untoldpsproject.strategies;

import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CSVFileStrategy implements GenerateFileStrategy {

    private static final String FILE_NAME = "order.csv";

    @Override
    public String generateFile(Order order) {
        String filePath = GenerateFileStrategy.directoryPath + File.separator + FILE_NAME;

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Ticket,Type,Start Date,Finish Date,Price,Ticket Number\n");

            List<Ticket> tickets = order.getTickets();
            if (tickets != null && !tickets.isEmpty()) {
                for (int i = 0; i < tickets.size(); i++) {
                    Ticket ticket = tickets.get(i);
                    writer.write((i + 1) + ",");
                    writer.write(ticket.getCategory().getTip() + ",");
                    writer.write(ticket.getCategory().getStartDate() + ",");
                    writer.write(ticket.getCategory().getFinishDate() + ",");
                    writer.write(ticket.getDiscountedPrice() + ",");
                    writer.write(ticket.getId() + "\n");
                }
                System.out.println("Order CSV generated successfully. File: " + filePath);
                return filePath;
            } else {
                System.out.println("No tickets found in the order. File: " + filePath);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while generating the order CSV. File: " + filePath);
            e.printStackTrace();
        }
        return null;
    }
}
