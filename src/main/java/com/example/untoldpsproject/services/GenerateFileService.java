package com.example.untoldpsproject.services;

import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.repositories.OrderRepository;
import com.example.untoldpsproject.strategies.CSVFileStrategy;
import com.example.untoldpsproject.strategies.PDFFileStrategy;
import com.example.untoldpsproject.strategies.TXTFileStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Setter
@Getter
@NoArgsConstructor
@Service
public class GenerateFileService {
    private TXTFileStrategy txtFileStrategy;
    private PDFFileStrategy pdfFileStrategy;
    private CSVFileStrategy csvFileStrategy;

    public String generateFile(String type, Order order) {
        if (type.equals("txt")) {
            txtFileStrategy.generateFile(order);
        }else if (type.equals("pdf")) {
            pdfFileStrategy.generateFile(order);
        }else if (type.equals("csv")) {
            csvFileStrategy.generateFile(order);
        }
        return "The File was successfully generated.";
    }


}
