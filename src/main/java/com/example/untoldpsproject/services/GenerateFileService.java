package com.example.untoldpsproject.services;

import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.repositories.OrderRepository;
import com.example.untoldpsproject.repositories.UserRepository;
import com.example.untoldpsproject.strategies.*;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private UserRepository userRepository;

    public String generateFile(String type, String orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        String generatedFilePath = new String();
        if(order.isPresent()) {
            if (type.equals("txt")) {
                Context context = new Context(new TXTFileStrategy());
                generatedFilePath = context.generate(order.get()); // Store the generated file path
            } else if (type.equals("pdf")) {
                Context context = new Context(new PDFFileStrategy());
                generatedFilePath = context.generate(order.get()); // Store the generated file path
            } else if (type.equals("csv")) {
                Context context = new Context(new CSVFileStrategy());
                generatedFilePath = context.generate(order.get()); // Store the generated file path
            }
            return generatedFilePath;
        }
        return null;
    }

    public List<Order> getOrders(){
        return orderRepository.findAll();
    }
    public Order getOrder(String orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()) {
            return order.get();
        }else{
            return null;
        }
    }
    public User getUser(String userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            return user.get();
        }else{
            return null;
        }
    }
}
