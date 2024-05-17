package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.*;
import com.example.untoldpsproject.entities.CartItem;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.mappers.CartItemMapper;
import com.example.untoldpsproject.mappers.OrderMapper;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.services.OrderService;
import com.example.untoldpsproject.services.RabbitMQSender;
import com.example.untoldpsproject.strategies.CSVFileStrategy;
import com.example.untoldpsproject.strategies.TXTFileStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class for managing order operations.
 */
@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/order")
public class OrderController {

    private final OrderService orderService;
    private RabbitMQSender rabbitMQSender;
    private RestTemplate restTemplate;

    /**
     * Retrieves a list of orders and displays them.
     *
     * @return A ModelAndView object containing the view name and the list of orders.
     */
    @GetMapping("/list/{userId}")
    public ModelAndView ordersList(@PathVariable("userId")String userId) {
        ModelAndView mav = new ModelAndView("order-list");
        List<OrderDto> orders = orderService.findOrders();
        mav.addObject("orders", orders);
        mav.addObject("userId", userId);
        return mav;
    }

    /**
     * Displays the form for adding a new order.
     *
     * @return A ModelAndView object containing the view name and an empty OrderDto object.
     */
    @GetMapping("/add/{userId}")
    public ModelAndView addOrderForm(@PathVariable("userId") String userId) {
        ModelAndView mav = new ModelAndView("order-add");
        mav.addObject("orderDto", new OrderDto());
        List<UserDto> users = orderService.findUsers();
        mav.addObject("users", users);
        List<TicketDto> tickets = orderService.findTickets().stream()
                .filter(ticket -> ticket.getAvailable() > 0)
                .collect(Collectors.toList());
        mav.addObject("tickets", tickets);
        mav.addObject("userId", userId);
        return mav;
    }

    /**
     * Adds a new order.
     *
     * @param orderDto The OrderDtoIds object representing the order to be added.
     * @return A redirection to the order list view.
     */
    @PostMapping("/add/{userId}")
    public ModelAndView addOrder(@PathVariable("userId") String userId, @ModelAttribute("orderDto") OrderDtoIds orderDto) {
        UserDto user = orderService.findUserById(orderDto.getUser());
        List<Ticket> tickets = new ArrayList<>();
        if(orderDto.getTickets()!=null){
            for(String ticketId : orderDto.getTickets() ){
                TicketDto ticket = orderService.findTicketById(ticketId);
                tickets.add(TicketMapper.toTicket(ticket));
            }
        }
        OrderDto order = new OrderDto();
        order.setUser(UserMapper.toUser(user));
        order.setTickets(tickets);
        order.setTotalPrice(orderService.calculateTotalPrice(order.getTickets()));
        orderService.insert(order);
        return new ModelAndView("redirect:/order/list/"+userId);
    }

    /**
     * Displays the form for editing an existing order.
     *
     * @param orderId The ID of the order to be edited.
     * @return A ModelAndView object containing the view name and the OrderDto object to be edited.
     */
    @GetMapping("/edit/{id}/{userId}")
    public ModelAndView editOrderForm(@PathVariable("userId") String userId, @PathVariable("id") String orderId) {
        ModelAndView mav = new ModelAndView("order-edit");
        OrderDto orderDto = orderService.findOrderById(orderId);
        mav.addObject("orderDto", orderDto);
        List<UserDto> users = orderService.findUsers();
        mav.addObject("users", users);
        List<TicketDto> tickets = orderService.findTickets();
        mav.addObject("tickets", tickets);
        mav.addObject("userId", userId);
        return mav;
    }

    /**
     * Updates an existing order.
     *
     * @param orderDto The OrderDto object representing the updated order information.
     * @return A redirection to the order list view.
     */
    @PostMapping("/edit/{id}/{userId}")
    public ModelAndView updateOrder(@PathVariable("userId") String userId, @ModelAttribute("orderDto") OrderDto orderDto) {
        orderService.updateOrderById(orderDto.getId(), orderDto);
        return new ModelAndView("redirect:/order/list/"+userId);
    }

    /**
     * Deletes an order.
     *
     * @param id The ID of the order to be deleted.
     * @return A redirection to the order list view.
     */
    @GetMapping("/delete/{id}/{userId}")
    public ModelAndView deleteOrder(@PathVariable("userId") String userId, @PathVariable("id") String id) {
        orderService.deleteOrderById(id);
        return new ModelAndView("redirect:/order/list/"+userId);
    }
    @GetMapping("/place-order/{userId}/{cartId}")
    public ModelAndView placeOrderForm(@PathVariable("userId") String userId, @PathVariable("cartId") String cartId) {
        List<CartItem> cartItems = orderService.findCartItemsByCartId(cartId);
        List<Ticket> tickets = new ArrayList<>();
        if (!cartItems.isEmpty()) {
            tickets = orderService.placeOrder(cartItems,tickets, userId, cartId);
            double totalPrice = orderService.findCartById(cartId).getTotalPrice();
            UserDto user = orderService.findUserById(userId);
            Order newOrder = new Order();
            newOrder.setTickets(tickets);
            newOrder.setTotalPrice(totalPrice);
            newOrder.setUser(UserMapper.toUser(user));
            newOrder.setId(orderService.insert(OrderMapper.toOrderDto(newOrder)));
            ModelAndView mav = new ModelAndView();
            mav.addObject("userId", userId);
            mav.addObject("cartId", cartId);
            mav.addObject("orderId", newOrder.getId());
            mav.setViewName("redirect:/payment/select-method/" + newOrder.getId());
            return mav;
        }else{
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

        Payload payload = new Payload(orderService.findUserByEmail(order.getUser().getEmail()).getId(),order.getUser().getFirstName(),order.getUser().getEmail());
        rabbitMQSender.send(payload);
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
