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


/**
 * Service class responsible for generating files based on orders.
 */
@Setter
@Getter
@Service
@AllArgsConstructor
public class GenerateFileService {
    private OrderRepository orderRepository;
    private UserRepository userRepository;

    /**
     * Generates a file of the specified type for the given order.
     *
     * @param type     The type of file to generate (e.g., "txt", "pdf", "csv").
     * @param orderId  The ID of the order for which the file is generated.
     * @return The path to the generated file, or null if the order is not found or the type is invalid.
     */
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

    /**
     * Retrieves all orders.
     *
     * @return A list of all orders.
     */
    public List<Order> getOrders(){
        return orderRepository.findAll();
    }

    /**
     * Retrieves the order with the specified ID.
     *
     * @param orderId The ID of the order to retrieve.
     * @return The order with the specified ID, or null if not found.
     */
    public Order getOrder(String orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()) {
            return order.get();
        }else{
            return null;
        }
    }
    /**
     * Retrieves the user with the specified ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user with the specified ID, or null if not found.
     */
    public User getUser(String userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            return user.get();
        }else{
            return null;
        }
    }
}