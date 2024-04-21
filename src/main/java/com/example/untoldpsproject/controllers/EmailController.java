package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.services.RabbitMQSender;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/email")
public class EmailController {

    private final RestTemplate restTemplate; // Inject RestTemplate

    private static final String RABBITMQ_BASE_URL = "http://localhost:8080/rabbitmq/sendEmail"; // Assuming RabbitMQController runs on localhost:8080

    @Autowired
    public EmailController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/send")
    public String sendEmail(String email) {
        restTemplate.postForEntity(RABBITMQ_BASE_URL, email, String.class);
        return "redirect:/login";
    }
}