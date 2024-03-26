package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.*;
import com.example.untoldpsproject.entities.Category;
import com.example.untoldpsproject.services.TicketService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
/**
 * Controller class for managing ticket operations.
 */
@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/ticket")
public class TicketController {
    private final TicketService ticketService;

    @PersistenceContext
    private EntityManager entityManager;
    @GetMapping("/list")
    public ModelAndView ticketsList() {
        ModelAndView mav = new ModelAndView("ticket-list");
        List<TicketDto> tickets = ticketService.findTickets();
        mav.addObject("tickets", tickets);
        return mav;
    }
    @GetMapping("/add")
    public ModelAndView addTicketForm() {
        ModelAndView mav = new ModelAndView("ticket-add");
        mav.addObject("ticketDto", new TicketDto());
        List<Category> categories = entityManager.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        mav.addObject("categories", categories);
        return mav;
    }
    @PostMapping("/add")
    public ModelAndView addTicket(@ModelAttribute("ticketDto") TicketDtoIds ticketDtoIds) {
        String categoryId = ticketDtoIds.getCategory();
        Category category = entityManager.find(Category.class, categoryId);
        TicketDto ticket = new TicketDto();
        ticket.setCategory(category);
        ticket.setAvailable(ticketDtoIds.getAvailable());
        ticket.setPrice(ticketDtoIds.getPrice());
        ticketService.insert(ticket);
        return new ModelAndView("redirect:/ticket/list");
    }
    @GetMapping("/edit/{id}")
    public ModelAndView editTicketForm(@PathVariable("id") String ticketId) {
        ModelAndView mav = new ModelAndView("ticket-edit");
        TicketDtoIds ticketDto = ticketService.findTicketById(ticketId);
        mav.addObject("ticketDto", ticketDto);
        List<Category> categories = entityManager.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        mav.addObject("categories", categories);
        return mav;
    }
    @PostMapping("/edit/{id}")
    public ModelAndView updateTicket(@ModelAttribute("ticketDto") TicketDto ticketDto) {
        ticketService.updateTicketById(ticketDto.getId(), ticketDto);
        return new ModelAndView("redirect:/ticket/list");
    }
    @GetMapping("/delete/{id}")
    public ModelAndView deleteTicket(@PathVariable("id") String id) {
        ticketService.deleteTicketById(id);
        return new ModelAndView("redirect:/ticket/list");
    }
}
