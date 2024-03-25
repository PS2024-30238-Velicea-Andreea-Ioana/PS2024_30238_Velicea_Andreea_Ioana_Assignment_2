package com.example.untoldpsproject.mappers;

import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.TicketDtoIds;
import com.example.untoldpsproject.entities.Category;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.services.CategoryService;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class TicketMapper{
    public static TicketDtoIds toTicketDto(Ticket ticket){
        return TicketDtoIds.builder().id(ticket.getId())
                .category(ticket.getCategory().getId())
                .price(ticket.getPrice())
                .quantity(ticket.getQuantity())
                .available(ticket.getAvailable())
                .orders(ticket.getOrders().stream().map(Order::getId).collect(Collectors.toList()))
                .build();
    }
    public static Ticket toTicket(TicketDto ticketDto){
        return Ticket.builder().id(ticketDto.getId())
                .category(ticketDto.getCategory())
                .price(ticketDto.getPrice())
                .quantity(ticketDto.getQuantity())
                .available(ticketDto.getAvailable())
                .orders(ticketDto.getOrders())
                .build();
    }
    public static Ticket toTicket(TicketDtoIds ticketDtoIds){
        return Ticket.builder().id(ticketDtoIds.getId())
                .price(ticketDtoIds.getPrice())
                .quantity(ticketDtoIds.getQuantity())
                .available(ticketDtoIds.getAvailable())
                .build();
    }
}
