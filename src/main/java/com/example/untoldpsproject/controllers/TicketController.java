package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.*;
import com.example.untoldpsproject.entities.Category;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.services.TicketService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

//    /**
//     * Inserts a new ticket into the system.
//     *
//     * @param ticketDto The ticket DTO containing information about the ticket to be inserted.
//     * @return The String of the inserted ticket.
//     */
//    @PostMapping("/insert")
//    public ResponseEntity<String> insertTicket(@RequestBody TicketDto ticketDto){
//        String ticketId = ticketService.insert(ticketDto);
//        return new ResponseEntity<>(ticketId, HttpStatus.CREATED);
//    }
//
//    /**
//     * Retrieves all tickets from the system.
//     *
//     * @return A list of ticket DTOs.
//     */
//    @GetMapping("/getAllTickets")
//    public ResponseEntity<List<TicketDtoIds>> getTickets(){
//        List<TicketDtoIds> dtos = ticketService.findTickets();
//        return new ResponseEntity<>(dtos,HttpStatus.OK);
//    }
//
//    /**
//     * Retrieves a ticket by its ID from the system.
//     *
//     * @param ticketId The ID of the ticket to retrieve.
//     * @return The ticket DTO.
//     */
//    @GetMapping(value = "/{id}")
//    public ResponseEntity<TicketDtoIds> getTicketById(@PathVariable("id") String ticketId){
//        TicketDtoIds dto = ticketService.findTicketById(ticketId);
//        return new ResponseEntity<>(dto, HttpStatus.OK);
//    }
//
//    /**
//     * Updates a ticket in the system by its ID.
//     *
//     * @param ticketId The ID of the ticket to update.
//     * @param ticketDto The updated ticket DTO.
//     * @return The updated ticket entity.
//     */
//    @PutMapping(value = "/{id}")
//    public ResponseEntity<Ticket> updateTicketById(@PathVariable("id") String ticketId, @RequestBody TicketDto ticketDto){
//        Ticket ticket = ticketService.updateTicketById(ticketId,ticketDto);
//        return new ResponseEntity<>(ticket, HttpStatus.OK);
//    }
//
//    /**
//     * Deletes a ticket from the system by its ID.
//     *
//     * @param ticketId The ID of the ticket to delete.
//     * @return ResponseEntity indicating the success of the operation.
//     */
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<String> deleteTicketById(@PathVariable("id") String ticketId){
//        ticketService.deleteTicketById(ticketId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
    @PersistenceContext
    private EntityManager entityManager;
    @GetMapping("/list")
    public ModelAndView ticketsList() {
        ModelAndView mav = new ModelAndView("ticket-list");
        List<TicketDtoIds> tickets = ticketService.findTickets();
        mav.addObject("tickets", tickets);
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editTicketForm(@PathVariable("id") String ticketId) {
        ModelAndView mav = new ModelAndView("category-edit");
        TicketDtoIds ticketDto = ticketService.findTicketById(ticketId);
        mav.addObject("ticketDto", ticketDto);
        return mav;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView updateTicket(@PathVariable("id") String id, @ModelAttribute TicketDto ticketDto) {
        ticketService.updateTicketById(id, ticketDto);
        return new ModelAndView("redirect:/ticket/list");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteTicket(@PathVariable("id") String id) {
        ticketService.deleteTicketById(id);
        return new ModelAndView("redirect:/ticket/list");
    }

    @GetMapping("/add")
    public ModelAndView addTicketForm() {
        ModelAndView mav = new ModelAndView("ticket-add");
        mav.addObject("ticketDto", new TicketDto());
        return mav;
    }

    @PostMapping("/add")
    public ModelAndView addTicket(@ModelAttribute TicketDtoIds ticketDto) {
        ticketService.insert(ticketDto);
        return new ModelAndView("redirect:/ticket/list");
    }
}
