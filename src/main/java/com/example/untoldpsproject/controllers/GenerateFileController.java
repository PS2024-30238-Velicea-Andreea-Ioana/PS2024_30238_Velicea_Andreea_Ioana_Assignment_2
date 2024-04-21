package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.mappers.OrderMapper;
import com.example.untoldpsproject.services.GenerateFileService;
import com.example.untoldpsproject.services.OrderService;
import com.example.untoldpsproject.strategies.GenerateFileStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
public class GenerateFileController {
    public GenerateFileService generateFileService;
    public OrderService orderService;
    @GetMapping("/generate-file")
    public String showGenerateFileForm() {
        return "order-list";
    }
    @PostMapping("/generate-file")
    public ModelAndView generateFile(@RequestParam("type") String type,
                                     @RequestParam("orderId") String orderId) {
        ModelAndView modelAndView = new ModelAndView();
        OrderDto order = orderService.findOrderById(orderId);
        String message = generateFileService.generateFile(type, OrderMapper.toOrder(order));
        modelAndView.setViewName("order-list");
        return modelAndView;
    }

}
