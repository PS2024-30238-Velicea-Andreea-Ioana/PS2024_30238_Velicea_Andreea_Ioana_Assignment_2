package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.dtos.OrderDtoIds;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.services.OrderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    @PersistenceContext
    private EntityManager entityManager;

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
        List<User> users = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        mav.addObject("users", users);
        List<Ticket> tickets = entityManager.createQuery("SELECT t FROM Ticket t", Ticket.class).getResultList();
        mav.addObject("tickets", tickets);
        return mav;
    }

    @PostMapping("/add")
    public ModelAndView addOrder(@ModelAttribute("orderDto") OrderDtoIds orderDto) {
        String userId = orderDto.getUser();
        List<String> ticketIds = orderDto.getTickets();
        User user = entityManager.find(User.class, userId);

        List<Ticket> tickets = new ArrayList<>();
        for(String ticketId : ticketIds ){
            Ticket ticket = entityManager.find(Ticket.class, ticketId);
            tickets.add(ticket);
        }
        OrderDto order = new OrderDto();
        order.setUser(user);
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
        List<User> users = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        mav.addObject("users", users);
        List<Ticket> tickets = entityManager.createQuery("SELECT t FROM Ticket t", Ticket.class).getResultList();
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
