package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.constants.UserConstants;
import com.example.untoldpsproject.dtos.*;
import com.example.untoldpsproject.entities.Category;
import com.example.untoldpsproject.entities.Role;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.factory.ArtistFactory;
import com.example.untoldpsproject.mappers.CartMapper;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.services.HomePageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Controller class for managing home page and redirection.
 */
@Controller
@AllArgsConstructor
public class HomePageController {

    private final HomePageService homePageService;
    private RestTemplate restTemplate;


    /**
     * Displays the start page for administrators.
     *
     * @param userId  The ID of the user.
     * @param request The HTTP servlet request.
     * @return The view name for the administrator start page.
     */
    @GetMapping("/administrator/{userId}")
    public String showAdministratorPage(@PathVariable("userId")String userId,HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userRole") && cookie.getValue().equals("ADMINISTRATOR")) {
                    return "administrator";
                }
            }
        }
        return "redirect:/login";
    }

    /**
     * Displays the start page for customers.
     *
     * @param userId  The ID of the user.
     * @param request The HTTP servlet request.
     * @return The view name for the customer start page.
     */
    @GetMapping("/customer/{userId}")
    public String showCustomerPage(@PathVariable("userId")String userId, HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userRole") && cookie.getValue().equals("CUSTOMER")) {
                    return "redirect:/customer/seetickets/" + userId;
                }
            }
        }
        return "redirect:/login";
    }

    /**
     * Displays the login form.
     *
     * @return A ModelAndView object for displaying the login form.
     */
    @GetMapping("/login")
    public ModelAndView loginForm(){
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("userDto", new UserDto());
        return mav;
    }

    /**
     * Handles user login.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @param response The HTTP servlet response.
     * @return A ModelAndView object for redirecting to the appropriate page after login.
     */
    @PostMapping("/login")
    public ModelAndView login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        User user = homePageService.findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            if (user.getRole() == Role.CUSTOMER) {
                response.addCookie(new Cookie("userRole", "CUSTOMER"));
                mav.setViewName("redirect:/customer/" + user.getId());
            } else if (user.getRole() == Role.ADMINISTRATOR) {
                response.addCookie(new Cookie("userRole", "ADMINISTRATOR"));
                mav.setViewName("redirect:/administrator/" + user.getId());
            }
        } else {
            mav.addObject("error", "Invalid username or password");
            mav.setViewName("login");
        }
        return mav;
    }

    /**
     * Displays the registration form.
     *
     * @return A ModelAndView object for displaying the registration form.
     */
    @GetMapping("/register")
    public ModelAndView registerForm(){
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("userDto", new UserDto());
        return mav;
    }

    /**
     * Handles user registration.
     *
     * @param userDto The user DTO containing registration information.
     * @return A ModelAndView object for redirecting to the appropriate page after registration.
     */
    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("userDto") UserDto userDto){
        ModelAndView mav = new ModelAndView();
        if (userDto == null) {
            mav.addObject("error", "User data is null");
            mav.setViewName("register");
            return mav;
        }

        if (homePageService.findUserByEmail(userDto.getEmail()) != null) {
            mav.addObject("error", "Email already registered");
            mav.setViewName("register");
            return mav;
        }

        String result = homePageService.insertUser(userDto);
        if(!result.equals(UserConstants.USER_INSERTED)){
            mav.addObject("error", result);
            mav.setViewName("register");
            return mav;
        }

        Payload payload = new Payload(homePageService.findUserByEmail(userDto.getEmail()).getId(),userDto.getFirstName(),userDto.getEmail());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setBearerAuth(payload.getId()+payload.getId());

        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(payload.getId(),payload.getName(),payload.getEmail(), "register","");
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
        return mav;
    }

    /**
     * Visualizes tickets for the customer.
     *
     * @param userId   The ID of the user.
     * @param category The category of tickets to filter.
     * @param sort     The sorting option.
     * @param request  The HTTP servlet request.
     * @return A ModelAndView object for displaying the ticket visualization.
     */
    @GetMapping("/customer/seetickets/{userId}")
    public ModelAndView visualiseTicketsByCategory(@PathVariable("userId") String userId, @RequestParam(value = "category", required = false) String category, @RequestParam(value = "sort", required = false) String sort, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("user-interface");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userRole") && cookie.getValue().equals("ADMINISTRATOR")) {
                    mav.setViewName("redirect:/login");
                    return mav;
                }
            }
        }
        UserDto user = homePageService.findUserById(userId);
        if (user.getCart() == null) {
            CartDto cart = new CartDto();
            cart.setCartItems(new ArrayList<>());
            cart.setUser(UserMapper.toUser(user));
            cart.setTotalPrice(0.0);
            cart = homePageService.insertCart(cart);
            user.setCart(CartMapper.toCart(cart));
            homePageService.updateUserById(userId, user);
        }
        List<TicketDto> tickets = new ArrayList<>();
        if (category != null && !category.isEmpty()) {
            for (TicketDto ticket : homePageService.findTickets()) {
                if (ticket.getAvailable() >= 0 && ticket.getCategory().getTip().equals(category)) {
                    tickets.add(ticket);
                }
            }
        } else {
            for (TicketDto ticket : homePageService.findTickets()) {
                if (ticket.getAvailable() >= 0)
                    tickets.add(ticket);
            }
        }
        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "price-asc":
                    tickets.sort(Comparator.comparing(TicketDto::getDiscountedPrice));
                    break;
                case "price-desc":
                    tickets.sort(Comparator.comparing(TicketDto::getDiscountedPrice).reversed());
                    break;
                case "availability-asc":
                    tickets.sort(Comparator.comparing(TicketDto::getAvailable));
                    break;
                case "availability-desc":
                    tickets.sort(Comparator.comparing(TicketDto::getAvailable).reversed());
                    break;
                default:
                    break;
            }
        }
        List<Category> categories = homePageService.findCategories();
        mav.addObject("tickets", tickets);
        mav.addObject("userId", userId);
        mav.addObject("categories", categories);
        mav.addObject("cartId", user.getCart().getId());
        return mav;
    }

    /**
     * Displays the artists page for the user.
     *
     * @param userId The ID of the user.
     * @return A ModelAndView object for displaying the artists page.
     */
    @GetMapping("/vizualizeArtists/{userId}")
    public ModelAndView seeArtists(@PathVariable("userId") String userId){
        ModelAndView mav = new ModelAndView("user-artists");
        mav.addObject("userId", userId);
        mav.addObject("artists", homePageService.findArtists());
        return mav;
    }
}
