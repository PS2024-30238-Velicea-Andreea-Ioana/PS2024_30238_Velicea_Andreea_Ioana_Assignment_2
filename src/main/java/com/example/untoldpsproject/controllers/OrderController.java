package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.dtos.OrderDtoIds;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Ticket;
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

    @GetMapping("/list")
    public ModelAndView ordersList() {
        ModelAndView mav = new ModelAndView("order-list");
        List<OrderDto> orders = orderService.findOrders();
        mav.addObject("orders", orders);
        return mav;
    }
    @GetMapping("/add")
    public ModelAndView addOrderForm() {
        ModelAndView mav = new ModelAndView("order-add");
        mav.addObject("orderDto", new OrderDto());
        List<UserDto> users = orderService.findUsers();
        mav.addObject("users", users);
        List<TicketDto> tickets = orderService.findTickets();
        mav.addObject("tickets", tickets);
        return mav;
    }

    @PostMapping("/add")
    public ModelAndView addOrder(@ModelAttribute("orderDto") OrderDtoIds orderDto) {
        UserDto user = orderService.findUserById(orderDto.getUser());
        List<Ticket> tickets = new ArrayList<>();
        for(String ticketId : orderDto.getTickets() ){
            TicketDto ticket = orderService.findTicketById(ticketId);
            tickets.add(TicketMapper.toTicket(ticket));
        }
        OrderDto order = new OrderDto();
        order.setUser(UserMapper.toUser(user));
        order.setTickets(tickets);
        order.setTotalPrice(orderService.calculateTotalPrice(order.getTickets()));
        orderService.insert(order);
        return new ModelAndView("redirect:/order/list");
    }

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

    @PostMapping("/edit/{id}")
    public ModelAndView updateOrder(@ModelAttribute("orderDto") OrderDto orderDto) {
        orderService.updateOrderById(orderDto.getId(), orderDto);
        return new ModelAndView("redirect:/order/list");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteOrder(@PathVariable("id") String id) {
        orderService.deleteOrderById(id);
        return new ModelAndView("redirect:/order/list");
    }

}
