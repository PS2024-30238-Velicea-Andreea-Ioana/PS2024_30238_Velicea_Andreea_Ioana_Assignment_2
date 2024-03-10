package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.TicketDtoIds;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.services.TicketService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
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

    /**
     * Inserts a new ticket into the system.
     *
     * @param ticketDto The ticket DTO containing information about the ticket to be inserted.
     * @return The UUID of the inserted ticket.
     */
    @PostMapping("/insert")
    public ResponseEntity<UUID> insertTicket(@RequestBody TicketDto ticketDto){
        UUID ticketId = ticketService.insert(ticketDto);
        return new ResponseEntity<>(ticketId, HttpStatus.CREATED);
    }

    /**
     * Retrieves all tickets from the system.
     *
     * @return A list of ticket DTOs.
     */
    @GetMapping("/getAllTickets")
    public ResponseEntity<List<TicketDtoIds>> getTickets(){
        List<TicketDtoIds> dtos = ticketService.findTickets();
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }

    /**
     * Retrieves a ticket by its ID from the system.
     *
     * @param ticketId The ID of the ticket to retrieve.
     * @return The ticket DTO.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<TicketDtoIds> getTicketById(@PathVariable("id") UUID ticketId){
        TicketDtoIds dto = ticketService.findTicketById(ticketId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Updates a ticket in the system by its ID.
     *
     * @param ticketId The ID of the ticket to update.
     * @param ticketDto The updated ticket DTO.
     * @return The updated ticket entity.
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Ticket> updateTicketById(@PathVariable("id") UUID ticketId, @RequestBody TicketDto ticketDto){
        Ticket ticket = ticketService.updateTicketById(ticketId,ticketDto);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    /**
     * Deletes a ticket from the system by its ID.
     *
     * @param ticketId The ID of the ticket to delete.
     * @return ResponseEntity indicating the success of the operation.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteTicketById(@PathVariable("id") UUID ticketId){
        ticketService.deleteTicketById(ticketId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
