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

    /**
     * Retrieves a list of tickets and displays them.
     *
     * @return A ModelAndView object containing the view name and the list of tickets.
     */
    @GetMapping("/list")
    public ModelAndView ticketsList() {
        ModelAndView mav = new ModelAndView("ticket-list");
        List<TicketDto> tickets = ticketService.findTickets();
        mav.addObject("tickets", tickets);
        return mav;
    }

    /**
     * Displays the form for adding a new ticket.
     *
     * @return A ModelAndView object containing the view name and an empty TicketDto object.
     */
    @GetMapping("/add")
    public ModelAndView addTicketForm() {
        ModelAndView mav = new ModelAndView("ticket-add");
        mav.addObject("ticketDto", new TicketDto());
        List<CategoryDto> categories = ticketService.findCategories();
        mav.addObject("categories", categories);
        return mav;
    }

    /**
     * Adds a new ticket.
     *
     * @param ticketDtoIds The TicketDtoIds object representing the ticket to be added.
     * @return A redirection to the ticket list view.
     */
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

    /**
     * Displays the form for editing an existing ticket.
     *
     * @param ticketId The ID of the ticket to be edited.
     * @return A ModelAndView object containing the view name and the TicketDto object to be edited.
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editTicketForm(@PathVariable("id") String ticketId) {
        ModelAndView mav = new ModelAndView("ticket-edit");
        TicketDto ticketDto = ticketService.findTicketById(ticketId);
        mav.addObject("ticketDto", ticketDto);
        List<CategoryDto> categories = ticketService.findCategories();
        mav.addObject("categories", categories);
        return mav;
    }

    /**
     * Updates an existing ticket.
     *
     * @param ticketDto The TicketDto object representing the updated ticket information.
     * @return A redirection to the ticket list view.
     */
    @PostMapping("/edit/{id}")
    public ModelAndView updateTicket(@ModelAttribute("ticketDto") TicketDto ticketDto) {
        ticketService.updateTicketById(ticketDto.getId(), ticketDto);
        return new ModelAndView("redirect:/ticket/list");
    }

    /**
     * Deletes a ticket.
     *
     * @param id The ID of the ticket to be deleted.
     * @return A redirection to the ticket list view.
     */
    @GetMapping("/delete/{id}")
    public ModelAndView deleteTicket(@PathVariable("id") String id) {
        ticketService.deleteTicketById(id);
        return new ModelAndView("redirect:/ticket/list");
    }
}