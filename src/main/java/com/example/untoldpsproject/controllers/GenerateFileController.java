package com.example.untoldpsproject.controllers;
import com.example.untoldpsproject.dtos.NotificationRequestDto;
import com.example.untoldpsproject.dtos.Payload;
import com.example.untoldpsproject.dtos.PaymentDto;
import com.example.untoldpsproject.dtos.ResponseMessageDto;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Role;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.services.GenerateFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Controller
@AllArgsConstructor
public class GenerateFileController {
    public GenerateFileService generateFileService;
    public RestTemplate restTemplate;

//    @GetMapping("/generateFile/add/{userId}/{orderId}/{type}")
//    public ModelAndView addFileForm(@PathVariable("userId") String userId,@PathVariable("orderId") String orderId,  @PathVariable("type")String type) {
//        ModelAndView mav = new ModelAndView("order-list");
//        mav.addObject("orders", generateFileService.getOrders());
//        return mav;
//    }
    @PostMapping("/generateFile/add/{userId}/{orderId}/{type}")
    public ModelAndView generateFileForUserPost(@PathVariable("userId") String userId,@PathVariable("orderId") String orderId, @PathVariable("type")String type) {
        return generateFileForUser(userId,orderId,type);
    }
    @GetMapping("/generateFile/add/{userId}/{orderId}/{type}")
    public ModelAndView generateFileForUser(@PathVariable("userId") String userId,@PathVariable("orderId") String orderId, @PathVariable("type")String type) {
        User user = generateFileService.getUser(userId);
        ModelAndView modelAndView = new ModelAndView();
        if(user.getRole().equals(Role.ADMINISTRATOR)){
           modelAndView = new ModelAndView("redirect:/order/list/" + userId);
        }else{
            modelAndView = new ModelAndView("redirect:/user/visualizeOrdersStatus/" + userId);
        }

        try {
            String filePath = generateFileService.generateFile(type, orderId);
            Order order = generateFileService.getOrder(orderId);
            Payload payload = new Payload(order.getUser().getId(), order.getUser().getFirstName(), user.getEmail());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            httpHeaders.setBearerAuth(payload.getId() + payload.getId());

            NotificationRequestDto notificationRequestDto = new NotificationRequestDto(payload.getId(), payload.getName(), payload.getEmail(), "adminFile", filePath);
            HttpEntity<NotificationRequestDto> entity = new HttpEntity<>(notificationRequestDto, httpHeaders);

            String emailServiceUrl = "http://localhost:8081/receiver/sendEmail";
            ResponseEntity<ResponseMessageDto> responseEntity = restTemplate.exchange(
                    emailServiceUrl, HttpMethod.POST, entity, ResponseMessageDto.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ResponseMessageDto responseMessageDto = responseEntity.getBody();
                System.out.println(responseMessageDto.getMessage());
            } else {
                System.out.println("Failed to send email notification");
            }
            return modelAndView;
        } catch (Exception e) {
            System.out.println("An error occurred while generating the file: " + e.getMessage());
            // Add error message to the model if needed
            modelAndView.addObject("error", "Failed to generate the file: " + e.getMessage());
            return modelAndView;
        }
    }

    @GetMapping("/selectType/{userId}/{orderId}")
    public ModelAndView selectFileType(@PathVariable("userId") String userId,@PathVariable("orderId") String orderId) {
        ModelAndView mav = new ModelAndView("generateFile-selection");
        mav.addObject("orderId", orderId);
        mav.addObject("userId", userId);
        return mav;
    }

    @PostMapping("/generateFile/{userId}/{orderId}")
    public ModelAndView generateFile(@PathVariable("userId") String userId, @RequestParam("type") String type,
                                     @PathVariable("orderId") String orderId) {
        if ("txt".equals(type)) {
            return new ModelAndView("redirect:/generateFile/add/"+userId + "/"+ orderId+ "/" + type);
        }
        if ("csv".equals(type)) {
            return new ModelAndView("redirect:/generateFile/add/" +userId + "/"+ orderId + "/" + type);
        }
        if ("pdf".equals(type)) {
            return new ModelAndView("redirect:/generateFile/add/" +userId + "/"+ orderId + "/" + type);
        }
        return new ModelAndView("redirect:/error");
    }
}
