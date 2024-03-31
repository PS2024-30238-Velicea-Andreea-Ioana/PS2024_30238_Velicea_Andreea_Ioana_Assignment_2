package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.*;
import com.example.untoldpsproject.mappers.CategoryMapper;
import com.example.untoldpsproject.services.TicketService;
import com.example.untoldpsproject.services.UserService;
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
    private final UserService userService;
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
        List<CategoryDto> categories = ticketService.findCategories();
        mav.addObject("categories", categories);
        return mav;
    }
    @PostMapping("/add")
    public ModelAndView addTicket(@ModelAttribute("ticketDto") TicketDtoIds ticketDtoIds) {
        CategoryDto category = ticketService.findCategoryById(ticketDtoIds.getCategory());
        TicketDto ticket = new TicketDto();
        ticket.setCategory(CategoryMapper.toCategory(category));
        ticket.setAvailable(ticketDtoIds.getAvailable());
        ticket.setPrice(ticketDtoIds.getPrice());
        ticketService.insert(ticket);
        return new ModelAndView("redirect:/ticket/list");
    }
    @GetMapping("/edit/{id}")
    public ModelAndView editTicketForm(@PathVariable("id") String ticketId) {
        ModelAndView mav = new ModelAndView("ticket-edit");
        TicketDto ticketDto = ticketService.findTicketById(ticketId);
        mav.addObject("ticketDto", ticketDto);
        List<CategoryDto> categories = ticketService.findCategories();
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
