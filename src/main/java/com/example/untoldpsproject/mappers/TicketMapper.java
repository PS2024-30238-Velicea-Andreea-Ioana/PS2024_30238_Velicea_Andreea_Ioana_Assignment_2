package com.example.untoldpsproject.mappers;

import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;

public class TicketMapper {
    public static TicketDto toTicketDto(Ticket ticket){
        return TicketDto.builder().id(ticket.getId())
                .type(ticket.getType())
                .price(ticket.getPrice())
                .buyDate(ticket.getBuyDate())
                .quantity(ticket.getQuantity())
                .user(ticket.getUser())
                .order(ticket.getOrder())
                .build();
    }
    public static Ticket toTicket(TicketDto ticketDto){
        return Ticket.builder().id(ticketDto.getId())
                .type(ticketDto.getType())
                .price(ticketDto.getPrice())
                .buyDate(ticketDto.getBuyDate())
                .quantity(ticketDto.getQuantity())
                .user(ticketDto.getUser())
                .order(ticketDto.getOrder())
                .build();
    }
}
