package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.dtos.OrderDtoIds;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.services.OrderService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
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

    /**
     * Retrieves a list of orders and displays them.
     *
     * @return A ModelAndView object containing the view name and the list of orders.
     */
    @GetMapping("/list")
    public ModelAndView ordersList() {
        ModelAndView mav = new ModelAndView("order-list");
        List<OrderDto> orders = orderService.findOrders();
        mav.addObject("orders", orders);
        return mav;
    }

    /**
     * Displays the form for adding a new order.
     *
     * @return A ModelAndView object containing the view name and an empty OrderDto object.
     */
    @GetMapping("/add")
    public ModelAndView addOrderForm() {
        ModelAndView mav = new ModelAndView("order-add");
        mav.addObject("orderDto", new OrderDto());
        List<UserDto> users = orderService.findUsers();
        mav.addObject("users", users);
        List<TicketDto> tickets = orderService.findTickets().stream()
                .filter(ticket -> ticket.getAvailable() > 0)
                .collect(Collectors.toList());
        mav.addObject("tickets", tickets);
        return mav;
    }

    /**
     * Adds a new order.
     *
     * @param orderDto The OrderDtoIds object representing the order to be added.
     * @return A redirection to the order list view.
     */
    @PostMapping("/add")
    public ModelAndView addOrder(@ModelAttribute("orderDto") OrderDtoIds orderDto) {
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
        return new ModelAndView("redirect:/order/list");
    }

    /**
     * Displays the form for editing an existing order.
     *
     * @param orderId The ID of the order to be edited.
     * @return A ModelAndView object containing the view name and the OrderDto object to be edited.
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editOrderForm(@PathVariable("id") String orderId) {
        ModelAndView mav = new ModelAndView("order-edit");
        OrderDto orderDto = orderService.findOrderById(orderId);
        mav.addObject("orderDto", orderDto);
        List<UserDto> users = orderService.findUsers();
        mav.addObject("users", users);
        List<TicketDto> tickets = orderService.findTickets();
        mav.addObject("tickets", tickets);
        return mav;
    }

    /**
     * Updates an existing order.
     *
     * @param orderDto The OrderDto object representing the updated order information.
     * @return A redirection to the order list view.
     */
    @PostMapping("/edit/{id}")
    public ModelAndView updateOrder(@ModelAttribute("orderDto") OrderDto orderDto) {
        orderService.updateOrderById(orderDto.getId(), orderDto);
        return new ModelAndView("redirect:/order/list");
    }

    /**
     * Deletes an order.
     *
     * @param id The ID of the order to be deleted.
     * @return A redirection to the order list view.
     */
    @GetMapping("/delete/{id}")
    public ModelAndView deleteOrder(@PathVariable("id") String id) {
        orderService.deleteOrderById(id);
        return new ModelAndView("redirect:/order/list");
    }


}
