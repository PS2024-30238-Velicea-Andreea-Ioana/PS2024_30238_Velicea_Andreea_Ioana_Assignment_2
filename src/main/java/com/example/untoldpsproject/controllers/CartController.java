package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.*;
import com.example.untoldpsproject.entities.*;
import com.example.untoldpsproject.mappers.*;
import com.example.untoldpsproject.services.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Controller class for managing cart operations.
 */
@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/cart")
public class CartController {
    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;
    private RabbitMQSender rabbitMQSender;
    private RestTemplate restTemplate;

    /**
     * Displays the available tickets for the customer to visualize.
     *
     * @param userId The ID of the user.
     * @return A ModelAndView object containing the view name and the list of available tickets.
     */
    @GetMapping("/customer/seetickets/{userId}")
    public ModelAndView visualiseTickets(@PathVariable("userId") String userId, HttpServletRequest request) {
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
        UserDto user = userService.findUserById(userId);
        if (user.getCart() == null) {
            CartDto cart = new CartDto();
            cart.setCartItems(new ArrayList<>());
            cart.setUser(UserMapper.toUser(user));
            cart.setTotalPrice(0.0);
            cart = cartService.insert(cart);
            user.setCart(CartMapper.toCart(cart));
            userService.updateUserById(userId, user);
        }
        List<TicketDto> tickets = new ArrayList<>();
        for (TicketDto ticket : userService.findTickets()) {
            if (ticket.getAvailable() > 0)
                tickets.add(ticket);
        }
        tickets.sort(Comparator.comparing(TicketDto::getDiscountedPrice));
        mav.addObject("tickets", tickets);
        mav.addObject("userId", userId);
        mav.addObject("cartId", user.getCart().getId());
        return mav;
    }

    /**
     * Adds a ticket to the cart.
     *
     * @param id      The ID of the ticket to be added.
     * @param userId  The ID of the user.
     * @param cartId  The ID of the cart.
     * @return A ModelAndView object containing a redirection URL.
     */
    @GetMapping("/customer/tickets/{userId}/addToCart/{ticketId}/{cartId}")
    public ModelAndView addTicketToCartForm(@PathVariable("ticketId") String id, @PathVariable("userId") String userId, @PathVariable("cartId") String cartId) {
        Ticket ticket = cartService.findTicket(id);
        User user = cartService.findUser(userId);
        Cart cartDto = cartService.findCartById(cartId);
        if (ticket != null && ticket.getAvailable() > 0) {
            if (cartDto == null) {
                cartDto = new Cart();
                cartDto.setUser(user);
                cartDto.setCartItems(new ArrayList<>());
                cartDto.setTotalPrice(0.0);
                cartService.insert(CartMapper.toCartDto(cartDto));
            }
            CartItem existingCartItem = cartService.findCartItemByTicketIdAndCartId(id, cartId);
            if (existingCartItem == null) {
                CartItemDto newCartItemDto = new CartItemDto();
                newCartItemDto.setTicket(ticket);
                newCartItemDto.setQuantity(1.0);
                newCartItemDto.setCart(cartDto);
                cartService.insertCartItem(newCartItemDto);
            } else {
                if (ticket.getAvailable() > existingCartItem.getQuantity()) {
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + 1.0);
                    cartService.updateCartItem(CartItemMapper.toCartItemDto(existingCartItem));
                }
            }
            cartService.updateTotalPrice(cartId);
            return new ModelAndView("redirect:/cart/visualizeCart/" + cartId);
        } else {
            return new ModelAndView("redirect:/cart/customer/seetickets/" + userId);
        }
    }

    /**
     * Visualizes the content of the cart.
     *
     * @param cartId The ID of the cart.
     * @return A ModelAndView object containing the view name and the cart details.
     */
    @GetMapping("/visualizeCart/{cartId}")
    public ModelAndView visualizeCart(@PathVariable("cartId") String cartId) {
        ModelAndView mav = new ModelAndView("cart");
        Cart cart = cartService.findCartById(cartId);
        cartService.updateTotalPrice(cart.getId());
        List<CartItem> cartItems = cart.getCartItems();
        String userId = cart.getUser().getId();
        mav.addObject("cart", cart);
        mav.addObject("cartItems", cartItems);
        mav.addObject("userId", userId);
        return mav;
    }

    /**
     * Adds a ticket to the cart.
     *
     * @param id      The ID of the ticket to be added.
     * @param userId  The ID of the user.
     * @param cartId  The ID of the cart.
     * @return A ModelAndView object containing a redirection URL.
     */
    @PostMapping("/customer/tickets/{userId}/addToCart/{ticketId}/{cartId}")
    public ModelAndView addTicketToCart(@PathVariable("ticketId") String id, @PathVariable("userId") String userId, @PathVariable("cartId") String cartId) {
        return addTicketToCartForm(id, userId, cartId);
    }

    /**
     * Removes an item from the cart.
     *
     * @param cartItemId The ID of the cart item to be removed.
     * @return A ModelAndView object containing a redirection URL.
     */
    @GetMapping("/removeItem/{cartItemId}")
    public ModelAndView removeItemFromCart(@PathVariable("cartItemId") String cartItemId) {
            String cartId = cartService.removeCartItem(cartItemId);
            return new ModelAndView("redirect:/cart/visualizeCart/" + cartId);
    }

    /**
     * Places an order.
     *
     * @param userId The ID of the user.
     * @param cartId The ID of the cart.
     * @return A ModelAndView object containing a redirection URL.
     */
    @GetMapping("/place-order/{userId}/{cartId}")
    public ModelAndView placeOrderForm(@PathVariable("userId") String userId, @PathVariable("cartId") String cartId) {
        List<CartItem> cartItems = cartService.findCartItemsByCartId(cartId);
        List<Ticket> tickets = new ArrayList<>();
        if (!cartItems.isEmpty()) {
            for (CartItem cartItem : cartItems) {
                Double quantity = cartItem.getQuantity();
                if (cartItem.getTicket().getAvailable() >= cartItem.getQuantity()) {
                    while (quantity > 0) {
                        tickets.add(cartItem.getTicket());
                        quantity--;
                    }
                    cartItem.setQuantity(0.0);
                    cartService.updateCartItem(CartItemMapper.toCartItemDto(cartItem));
                    cartService.deleteCartItemById(cartItem.getId());
                }
            }
            cartService.updateTotalPrice(cartId);
            double totalPrice = cartService.findCartById(cartId).getTotalPrice();
            User user = cartService.findUser(userId);
            Order newOrder = new Order();
            newOrder.setTickets(tickets);
            newOrder.setTotalPrice(totalPrice);
            newOrder.setUser(user);
            newOrder.setId(orderService.insert(OrderMapper.toOrderDto(newOrder)));
            ModelAndView mav = new ModelAndView();
            mav.addObject("userId", userId);
            mav.addObject("cartId", cartId);
            mav.addObject("orderId", newOrder.getId());
            mav.setViewName("redirect:/payment/select-method/" + newOrder.getId());
            return mav;
//            return new ModelAndView("redirect:/payment/add/" + newOrder.getId());
        } else {
            return new ModelAndView("redirect:/cart/visualizeCart/" + cartId);
        }
    }

    /**
     * Places an order.
     *
     * @param userId The ID of the user.
     * @param cartId The ID of the cart.
     * @return A ModelAndView object containing a redirection URL.
     */
    @PostMapping("/place-order/{userId}/{cartId}")
    public ModelAndView placeOrder(@PathVariable("userId") String userId, @PathVariable("cartId") String cartId) {
        return placeOrderForm(userId, cartId);
    }

    /**
     * Visualizes the placed order.
     *
     * @param orderId The ID of the order.
     * @return A ModelAndView object containing the view name and the order details.
     */
    @GetMapping("/visualizeOrder/{orderId}")
    public ModelAndView visualizeOrder(@PathVariable("orderId") String orderId) {
        ModelAndView mav = new ModelAndView("order-placed");
        OrderDto order = orderService.findOrderById(orderId);

        Payload payload = new Payload(userService.findUserByEmail(order.getUser().getEmail()).getId(),order.getUser().getFirstName(),order.getUser().getEmail());
        rabbitMQSender.send(payload);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setBearerAuth(payload.getId()+payload.getId());

        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(payload.getId(),payload.getName(),payload.getEmail(), "order");
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
        List<Ticket> tickets = order.getTickets();
        String userId = order.getUser().getId();
        User user = order.getUser();
        mav.addObject("order", order);
        mav.addObject("tickets", tickets);
        mav.addObject("userId", userId);
        mav.addObject("user", user);
        return mav;
    }
}
