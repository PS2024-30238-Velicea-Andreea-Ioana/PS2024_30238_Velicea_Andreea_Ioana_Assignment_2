package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Role;
import com.example.untoldpsproject.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@AllArgsConstructor
public class HomePageController {
    private final UserService userService;
    @GetMapping("/administrator")
    public String showStartPage() {
        return "administrator";
    }
    @GetMapping("/home")
    public ModelAndView showHomePage() {
        ModelAndView mav = new ModelAndView("home");
        List<UserDto> users = userService.findUsers();
        mav.addObject("users", users);
        return mav;
    }

    @PostMapping("/redirect")
    public ModelAndView redirectToInterface(@RequestParam("userId") String userId) {
        ModelAndView mav = new ModelAndView();
        UserDto userDto = userService.findUserById(userId);
        if (userDto.getRole() == Role.CUSTOMER) {
            mav.setViewName("redirect:/cart/customer/seetickets/"+userId);
        } else if (userDto.getRole() == Role.ADMINISTRATOR) {
            mav.setViewName("redirect:/administrator?userId="+userId);
        } else {
            mav.setViewName("redirect:/");
        }
        return mav;
    }


}
