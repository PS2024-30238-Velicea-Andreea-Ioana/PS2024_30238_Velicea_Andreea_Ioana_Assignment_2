package com.example.untoldpsproject.services;

import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.repositories.TicketRepository;
import com.example.untoldpsproject.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@Service
public class TicketService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketService.class);
    private final TicketRepository ticketRepository;

    public UUID insert(TicketDto ticketDto){
        Ticket ticket = TicketMapper.toTicket(ticketDto);
        ticket = ticketRepository.save(ticket);
        LOGGER.debug("Ticket with id {} was inserted in db",ticket.getId());
        return ticket.getId();
    }

    public List<TicketDto> findTickets(){
        List<Ticket> ticketList = ticketRepository.findAll();
        return ticketList.stream().map(TicketMapper::toTicketDto).collect(Collectors.toList());
    }

    public TicketDto findTicketById(UUID id){
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(!ticketOptional.isPresent()){
            LOGGER.error("Ticket with id {} was not found in db", id);
        }
        return TicketMapper.toTicketDto(ticketOptional.get());
    }

    public Ticket updateTicketById(UUID id, TicketDto updatedTicketDto){
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(!ticketOptional.isPresent()){
            LOGGER.error("Ticket with id {} was not found in db", id);
        }else{
            Ticket ticket = ticketOptional.get();
            Ticket updatedTicket = TicketMapper.toTicket(updatedTicketDto);
            ticket.setType(updatedTicket.getType());
            ticket.setPrice(updatedTicket.getPrice());
            ticket.setBuyDate(updatedTicket.getBuyDate());
            ticket.setQuantity(updatedTicket.getQuantity());
            ticketRepository.save(ticket);
            LOGGER.debug("Ticket with id {} was successfully updated", id);

        }
        return ticketOptional.get();
    }

    public void deleteTicketById(UUID id){
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(!ticketOptional.isPresent()){
            LOGGER.error("Ticket with id {} was not found in db", id);
        }else{
            ticketRepository.delete(ticketOptional.get());
        }
    }
}
