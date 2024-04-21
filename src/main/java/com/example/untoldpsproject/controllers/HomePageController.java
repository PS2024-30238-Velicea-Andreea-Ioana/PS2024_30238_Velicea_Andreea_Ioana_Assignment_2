package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.constants.UserConstants;
import com.example.untoldpsproject.dtos.NotificationRequestDto;
import com.example.untoldpsproject.dtos.Payload;
import com.example.untoldpsproject.dtos.ResponseMessageDto;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Role;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.services.RabbitMQSender;
import com.example.untoldpsproject.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

/**
 * Controller class for managing home page and redirection.
 */
@Controller
@AllArgsConstructor
public class HomePageController {

    private final UserService userService;
    private final RabbitMQSender rabbitMQSender;
    private RestTemplate restTemplate;

    /**
     * Displays the start page for administrators.
     *
     * @return The view name for the administrator start page.
     */
    @GetMapping("/administrator")
    public String showAdministratorPage(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userRole") && cookie.getValue().equals("ADMINISTRATOR")) {
                    return "administrator";
                }
            }
        }
        // If the user doesn't have the ADMIN role cookie, redirect them to the login page or show an access denied message
        return "redirect:/login";
    }
    @GetMapping("/customer/{userId}")
    public String showCustomerPage(@PathVariable("userId")String userId, HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userRole") && cookie.getValue().equals("CUSTOMER")) {
                    return "redirect:/cart/customer/seetickets/" + userId;
                }
            }
        }
        // If the user doesn't have the ADMIN role cookie, redirect them to the login page or show an access denied message
        return "redirect:/login";
    }

//    /**
//     * Displays the home page.
//     *
//     * @return A ModelAndView object containing the view name and the list of users.
//     */
//    @GetMapping("/home")
//    public ModelAndView showHomePage() {
//        ModelAndView mav = new ModelAndView("home");
//        List<UserDto> users = userService.findUsers();
//        mav.addObject("users", users);
//        return mav;
//    }

//    /**
//     * Redirects to different interfaces based on the user's role.
//     *
//     * @param userId The ID of the user.
//     * @return A ModelAndView object containing the redirection URL based on the user's role.
//     */
//    @PostMapping("/redirect")
//    public ModelAndView redirectToInterface(@RequestParam("userId") String userId) {
//        ModelAndView mav = new ModelAndView();
//        UserDto userDto = userService.findUserById(userId);
//        if (userDto.getRole() == Role.CUSTOMER) {
//            mav.setViewName("redirect:/cart/customer/seetickets/" + userId);
//        } else if (userDto.getRole() == Role.ADMINISTRATOR) {
//            mav.setViewName("redirect:/administrator?userId=" + userId);
//        } else {
//            mav.setViewName("redirect:/");
//        }
//        return mav;
//    }
    @GetMapping("/login")
    public ModelAndView loginForm(){
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("userDto", new UserDto());
        return mav;
    }
    @PostMapping("/login")
    public ModelAndView login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        User user = userService.findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            if (user.getRole() == Role.CUSTOMER) {
                response.addCookie(new Cookie("userRole", "CUSTOMER"));
                mav.setViewName("redirect:/customer/" + user.getId());
            } else if (user.getRole() == Role.ADMINISTRATOR) {
                response.addCookie(new Cookie("userRole", "ADMINISTRATOR"));
                mav.setViewName("redirect:/administrator?userId=" + user.getId());
            }
        } else {
            mav.addObject("error", "Invalid username or password");
            mav.setViewName("login");
        }
        return mav;
    }
    @GetMapping("/register")
    public ModelAndView registerForm(){
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("userDto", new UserDto());
        return mav;
    }
    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("userDto") UserDto userDto){
        ModelAndView mav = new ModelAndView();
        if (userDto == null) {
            mav.addObject("error", "User data is null");
            mav.setViewName("register");
            return mav;
        }

        // Validate if the email is already registered
        if (userService.findUserByEmail(userDto.getEmail()) != null) {
            mav.addObject("error", "Email already registered");
            mav.setViewName("register");
            return mav;
        }

        // Insert the user into the database
        String result = userService.insert(userDto);
        if(!result.equals(UserConstants.USER_INSERTED)){
            mav.addObject("error", result);
            mav.setViewName("register");
            return mav;
        }

        Payload payload = new Payload(userService.findUserByEmail(userDto.getEmail()).getId(),userDto.getFirstName(),userDto.getEmail());
        //rabbitMQSender.send(payload);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setBearerAuth(payload.getId()+payload.getId());

        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(payload.getId(),payload.getName(),payload.getEmail(), "register");
        HttpEntity<NotificationRequestDto> entity = new HttpEntity<>(notificationRequestDto, httpHeaders);

        String emailServiceUrl = "http://localhost:8081/receiver/sendEmail";
        ResponseEntity<ResponseMessageDto> responseEntity = restTemplate.exchange(
                emailServiceUrl,                        // URL
                HttpMethod.POST,                       // HTTP method
                entity,                                // Request entity (body and headers)
                ResponseMessageDto.class                           // Response type
        );
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            ResponseMessageDto responseMessageDto = responseEntity.getBody();
            System.out.println(responseMessageDto.getMessage());
        } else {
            System.out.println("Failed to send email notification");
        }
        mav.setViewName("redirect:/login");
        //mav.setViewName("redirect:/email/send?email=" + userDto.getEmail());
        // Redirect to the registration success page
        return mav;
    }

}
