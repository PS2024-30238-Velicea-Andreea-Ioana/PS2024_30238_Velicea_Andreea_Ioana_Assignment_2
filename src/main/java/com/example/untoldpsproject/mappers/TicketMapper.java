package com.example.untoldpsproject.mappers;

import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.TicketDtoIds;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;

import java.util.stream.Collectors;

public class TicketMapper {
    public static TicketDtoIds toTicketDto(Ticket ticket){
        return TicketDtoIds.builder().id(ticket.getId())
                .type(ticket.getType())
                .price(ticket.getPrice())
                .quantity(ticket.getQuantity())
                .available(ticket.getAvailable())
                .orders(ticket.getOrders().stream().map(Order::getId).collect(Collectors.toList()))
                .build();
    }
    public static Ticket toTicket(TicketDto ticketDto){
        return Ticket.builder().id(ticketDto.getId())
                .type(ticketDto.getType())
                .price(ticketDto.getPrice())
                .quantity(ticketDto.getQuantity())
                .available(ticketDto.getAvailable())
                .orders(ticketDto.getOrders())
                .build();
    }
}
