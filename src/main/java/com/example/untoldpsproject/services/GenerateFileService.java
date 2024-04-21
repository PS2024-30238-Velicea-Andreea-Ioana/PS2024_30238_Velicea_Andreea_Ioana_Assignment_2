package com.example.untoldpsproject.services;

import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.repositories.OrderRepository;
import com.example.untoldpsproject.strategies.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Setter
@Getter
@Service
@AllArgsConstructor
public class GenerateFileService {
    private OrderRepository orderRepository;

    public String generateFile(String type, String orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()) {
            if (type.equals("txt")) {
                Context context = new Context(new TXTFileStrategy());
                context.generate(order.get());
            }else if (type.equals("pdf")) {
                Context context = new Context(new PDFFileStrategy());
                context.generate(order.get());
            }else if (type.equals("csv")) {
                Context context = new Context(new CSVFileStrategy());
                context.generate(order.get());
            }
            return "The File was successfully generated.";
        }
        return "The Order was not found.";
    }

    public List<Order> getOrders(){
        return orderRepository.findAll();
    }
}
