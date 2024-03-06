package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.TicketDto;
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

@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/ticket")
public class TicketController {
    private final TicketService ticketService;
    @PostMapping("/insert")
    public ResponseEntity<UUID> insertTicket(@RequestBody TicketDto ticketDto){
        UUID ticketId = ticketService.insert(ticketDto);
        return new ResponseEntity<>(ticketId, HttpStatus.CREATED);
    }
    @GetMapping("/getAllTickets")
    public ResponseEntity<List<TicketDto>> getTickets(){
        List<TicketDto> dtos = ticketService.findTickets();
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable("id") UUID ticketId){
        TicketDto dto = ticketService.findTicketById(ticketId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Ticket> updateTicketById(@PathVariable("id") UUID ticketId, @RequestBody TicketDto ticketDto){
        Ticket ticket = ticketService.updateTicketById(ticketId,ticketDto);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteTicketById(@PathVariable("id") UUID ticketId){
        ticketService.deleteTicketById(ticketId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
