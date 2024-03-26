package com.example.untoldpsproject.services;

import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.TicketDtoIds;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.repositories.TicketRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service class for managing tickets.
 */
@Setter
@Getter
@AllArgsConstructor
@Service
public class TicketService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketService.class);
    private final TicketRepository ticketRepository;

    /**
     * Inserts a new ticket into the database.
     *
     * @param ticketDto The ticket DTO containing information about the ticket.
     * @return The String of the inserted ticket.
     */
    public String insert(TicketDto ticketDto){
        Ticket ticket = TicketMapper.toTicket(ticketDto);
        ticket = ticketRepository.save(ticket);
        LOGGER.debug("Ticket with id {} was inserted in db",ticket.getId());
        return ticket.getId();
    }

    /**
     * Retrieves all tickets from the database.
     *
     * @return A list of ticket DTOs.
     */
    public List<TicketDto> findTickets(){
        List<Ticket> ticketList = ticketRepository.findAll();
        return ticketList.stream().map(TicketMapper::toTicketDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a ticket by its ID from the database.
     *
     * @param id The ID of the ticket to retrieve.
     * @return The ticket DTO.
     */
    public TicketDtoIds findTicketById(String id){
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(!ticketOptional.isPresent()){
            LOGGER.error("Ticket with id {} was not found in db", id);
        }
        return TicketMapper.toTicketDtoIds(ticketOptional.get());
    }

    /**
     * Updates a ticket in the database.
     *
     * @param id The ID of the ticket to update.
     * @param updatedTicketDto The updated ticket DTO.
     * @return The updated ticket entity.
     */
    public void updateTicketById(String id, TicketDto updatedTicketDto){
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(!ticketOptional.isPresent()){
            LOGGER.error("Ticket with id {} was not found in db", id);
        }else{
            Ticket ticket = ticketOptional.get();
            Ticket updatedTicket = TicketMapper.toTicket(updatedTicketDto);
            ticket.setCategory(updatedTicket.getCategory());
            ticket.setPrice(updatedTicket.getPrice());
            ticket.setAvailable(updatedTicket.getAvailable());
            ticketRepository.save(ticket);
            LOGGER.debug("Ticket with id {} was successfully updated", id);

        }
    }
    /**
     * Deletes a ticket from the database.
     *
     * @param id The ID of the ticket to delete.
     */
    public void deleteTicketById(String id){
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(!ticketOptional.isPresent()){
            LOGGER.error("Ticket with id {} was not found in db", id);
        }else{
            ticketRepository.delete(ticketOptional.get());
        }
    }
}
