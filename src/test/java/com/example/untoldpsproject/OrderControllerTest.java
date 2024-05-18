package com.example.untoldpsproject;

import com.example.untoldpsproject.controllers.OrderController;
import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Cart;
import com.example.untoldpsproject.entities.CartItem;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.services.OrderService;
import com.example.untoldpsproject.services.RabbitMQSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private RabbitMQSender rabbitMQSender;

    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPlaceOrder_CartIsEmpty() {
        String userId = "user123";
        String cartId = "cart123";

        when(orderService.findCartItemsByCartId(cartId)).thenReturn(new ArrayList<>());

        ModelAndView result = orderController.placeOrder(userId, cartId, redirectAttributes);

        assertEquals("redirect:/cart/visualizeCart/" + cartId, result.getViewName());
    }

    @Test
    public void testPlaceOrder_TicketsAreSoldOut() {
        String userId = "user123";
        String cartId = "cart123";
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItems.add(cartItem);

        when(orderService.findCartItemsByCartId(cartId)).thenReturn(cartItems);
        when(orderService.placeOrder(anyList(), anyList(), anyString(), anyString())).thenReturn(null);

        ModelAndView result = orderController.placeOrder(userId, cartId, redirectAttributes);

        assertEquals("redirect:/cart/visualizeCart/" + cartId, result.getViewName());
        verify(redirectAttributes).addFlashAttribute("error", "One ticket is sold out!");
    }

    @Test
    public void testPlaceOrder_Success() {
        String userId = "user123";
        String cartId = "cart123";
        String orderId = "order123";
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItems.add(cartItem);

        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket();
        tickets.add(ticket);

        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setFirstName("John");
        userDto.setEmail("john@example.com");

        Order order = new Order();
        order.setId(orderId);
        order.setUser(UserMapper.toUser(userDto));
        order.setTickets(tickets);
        order.setTotalPrice(100.0);

        Cart cart = new Cart();
        cart.setTotalPrice(100.0);

        when(orderService.findCartItemsByCartId(cartId)).thenReturn(cartItems);
        when(orderService.placeOrder(cartItems, new ArrayList<>(), userId, cartId)).thenReturn(tickets);
        when(orderService.findCartById(cartId)).thenReturn(cart);
        when(orderService.findUserById(userId)).thenReturn(userDto);
        when(orderService.insert(any(OrderDto.class))).thenReturn(orderId);

        OrderController orderController = new OrderController(orderService, rabbitMQSender, null);
        ModelAndView result = orderController.placeOrder(userId, cartId, redirectAttributes);

        assertEquals("redirect:/payment/select-method/" + orderId, result.getViewName());
        assertEquals(userId, result.getModel().get("userId"));
        assertEquals(cartId, result.getModel().get("cartId"));
        assertEquals(orderId, result.getModel().get("orderId"));
    }
}
