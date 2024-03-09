package com.example.untoldpsproject.mappers;

import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.TicketDtoIds;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;

public class TicketMapper {
    public static TicketDtoIds toTicketDto(Ticket ticket){
        return TicketDtoIds.builder().id(ticket.getId())
                .type(ticket.getType())
                .price(ticket.getPrice())
                .quantity(ticket.getQuantity())
                .orders(ticket.convertOrdersToIds())
                .build();
    }
    public static Ticket toTicket(TicketDto ticketDto){
        return Ticket.builder().id(ticketDto.getId())
                .type(ticketDto.getType())
                .price(ticketDto.getPrice())
                .quantity(ticketDto.getQuantity())
                .orders(ticketDto.getOrders())
                .build();
    }
}
