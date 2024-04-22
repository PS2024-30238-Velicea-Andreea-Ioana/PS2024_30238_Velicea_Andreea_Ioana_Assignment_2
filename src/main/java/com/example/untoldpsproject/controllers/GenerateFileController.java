package com.example.untoldpsproject.controllers;
import com.example.untoldpsproject.services.GenerateFileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
public class GenerateFileController {
    public GenerateFileService generateFileService;

    @GetMapping("/generate-file")
    public String showGenerateFileForm() {
        return "order-list";
    }
    @PostMapping("/generate-file")
    public ModelAndView generateFile(@RequestParam("type") String type,
                                     @RequestParam("orderId") String orderId) {
        ModelAndView modelAndView = new ModelAndView();
        String message = generateFileService.generateFile(type, orderId);
        modelAndView.setViewName("order-list");
        modelAndView.addObject("orders", generateFileService.getOrders());
        return modelAndView;
    }

}
