package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.CartDto;
import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Cart;
import com.example.untoldpsproject.entities.CartItem;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.mappers.CartMapper;
import com.example.untoldpsproject.mappers.OrderMapper;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.services.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/cart")
public class CartController {
    private final OrderService orderService;
    private final CartItemService cartItemService;
    private final CartService cartService;
    private final TicketService ticketService;
    private final UserService userService;
    @GetMapping("/customer/seetickets/{userId}")
    public ModelAndView visualiseTickets(@PathVariable("userId")String userId){
        ModelAndView mav = new ModelAndView("user-interface");
        List<TicketDto> tickets = userService.findTickets();
        UserDto user = userService.findUserById(userId);
        if (user.getCart() == null) {
            CartDto cart = new CartDto();
            cart.setCartItems(new ArrayList<>());
            cart.setUser(UserMapper.toUser(user));
            cart.setTotalPrice(0.0);
            cart = cartService.insert(cart);
            user.setCart(CartMapper.toCart(cart));
            userService.updateUserById(userId,user);
        }
        mav.addObject("tickets", tickets);
        mav.addObject("userId", userId);
        mav.addObject("cartId",user.getCart().getId());
        return mav;
    }

    @GetMapping("/customer/tickets/{userId}/addToCart/{ticketId}/{cartId}")
    public ModelAndView addTicketToCartForm(@PathVariable("ticketId") String id, @PathVariable("userId") String userId, @PathVariable("cartId") String cartId) {
        TicketDto ticket = ticketService.findTicketById(id);

        // Check if the ticket is available
        if (ticket != null && ticket.getAvailable() > 0) {
            UserDto user = userService.findUserById(userId);
            Cart cartDto = cartService.findCartById(cartId);

            if (cartDto == null) {
                cartDto = new Cart();
                cartDto.setUser(UserMapper.toUser(user));
                cartDto.setCartItems(new ArrayList<>());
                cartDto.setTotalPrice(0.0);
                cartService.insert(CartMapper.toCartDto(cartDto));
            }

            CartItem existingCartItem = cartItemService.findCartItemByTicketIdAndCartId(id, cartId);

            if (existingCartItem == null) {
                CartItem newCartItem = new CartItem();
                newCartItem.setTicket(TicketMapper.toTicket(ticket));
                newCartItem.setQuantity(1.0);
                newCartItem.setCart(cartDto);
                cartItemService.insert(newCartItem);
//                ticket.setAvailable(ticket.getAvailable() - 1);
//                ticketService.updateTicketById(id, ticket);
            } else {
                // If the ticket already exists in the cart, update the quantity
                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1.0);
                cartItemService.update(existingCartItem);
//                ticket.setAvailable(ticket.getAvailable() - 1);
//                ticketService.updateTicketById(id, ticket);
            }

            cartService.updateTotalPrice(cartId);
            return new ModelAndView("redirect:/cart/visualizeCart/" + cartId );
        } else {
            // Redirect or show an error message indicating that the ticket is not available
            // You can customize this based on your application's requirements
            return new ModelAndView("redirect:/cart/customer/seetickets/" + userId);
        }
    }

    @GetMapping("/visualizeCart/{cartId}")
    public ModelAndView visualizeCart(@PathVariable ("cartId") String cartId){
        ModelAndView mav = new ModelAndView("cart");
        Cart cart = cartService.findCartById(cartId);
        cartService.updateTotalPrice(cart.getId());
        List<CartItem> cartItems = cart.getCartItems();
        String userId = cart.getUser().getId();
        mav.addObject("cart", cart);
        mav.addObject("cartItems",cartItems);
        mav.addObject("userId",userId);
        return mav;
    }


    @PostMapping("/customer/tickets/{userId}/addToCart/{ticketId}/{cartId}")
    public ModelAndView addTicketToCart(@PathVariable("ticketId") String id, @PathVariable("userId") String userId,@PathVariable("cartId") String cartId) {
        return addTicketToCartForm(id, userId,cartId);
    }
    @GetMapping("/removeItem/{cartItemId}")
    public ModelAndView removeItemFromCart(@PathVariable("cartItemId") String cartItemId) {
        CartItem cartItem = cartItemService.findCartItemById(cartItemId);
        if (cartItem != null) {
            String cartId = cartItem.getCart().getId();
            cartItemService.deleteCartItemById(cartItemId);
//            TicketDto ticketDto = ticketService.findTicketById(cartItem.getTicket().getId());
//            ticketDto.setAvailable(ticketDto.getAvailable()+1);
//            ticketService.updateTicketById(ticketDto.getId(),ticketDto);
            cartService.updateTotalPrice(cartId);
            return new ModelAndView("redirect:/cart/visualizeCart/"+ cartId);
        }
        return null;
    }

    @GetMapping("/place-order/{userId}/{cartId}")
    public ModelAndView placeOrderForm(@PathVariable("userId") String userId,@PathVariable("cartId") String cartId) {
        List<CartItem> cartItems = cartItemService.findCartItemsByCartId(cartId);
        List<Ticket> tickets = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Double quantity = cartItem.getQuantity();
            while(quantity >0){
                tickets.add(cartItem.getTicket());
                quantity--;
            }
            cartItem.setQuantity(0.0);
            cartItemService.update(cartItem);
            cartItemService.deleteCartItemById(cartItem.getId());
        }
        cartService.updateTotalPrice(cartId);
        double totalPrice = cartService.findCartById(cartId).getTotalPrice();
        UserDto user = userService.findUserById(userId);
        Order newOrder = new Order();
        newOrder.setTickets(tickets);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setUser(UserMapper.toUser(user));
        newOrder.setId(orderService.insert(OrderMapper.toOrderDto(newOrder)));
        return new ModelAndView("redirect:/cart/visualizeOrder/" + newOrder.getId());
    }
    @PostMapping("/place-order/{userId}/{cartId}")
    public ModelAndView placeOrder(@PathVariable("userId") String userId,@PathVariable("cartId") String cartId) {
        return placeOrderForm(userId,cartId);
    }
    @GetMapping("/visualizeOrder/{orderId}")
    public ModelAndView visualizeOrder(@PathVariable ("orderId") String orderId){
        ModelAndView mav = new ModelAndView("order-placed");
        OrderDto order = orderService.findOrderById(orderId);
        List<Ticket> tickets = order.getTickets();
        String userId = order.getUser().getId();
        mav.addObject("order", order);
        mav.addObject("tickets",tickets);
        mav.addObject("userId",userId);
        return mav;
    }

}
