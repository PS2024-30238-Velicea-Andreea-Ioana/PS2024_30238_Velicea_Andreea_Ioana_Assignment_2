package com.example.untoldpsproject.mappers;

import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.TicketDtoIds;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import java.util.stream.Collectors;


public class TicketMapper{
    public static TicketDto toTicketDto(Ticket ticket){
        return TicketDto.builder().id(ticket.getId())
                .category(ticket.getCategory())
                .price(ticket.getPrice())
                .available(ticket.getAvailable())
                .orders(ticket.getOrders())
                .cartItem(ticket.getCartItems())
                .build();
    }
    public static Ticket toTicket(TicketDto ticketDto){
        return Ticket.builder().id(ticketDto.getId())
                .category(ticketDto.getCategory())
                .price(ticketDto.getPrice())
                .available(ticketDto.getAvailable())
                .orders(ticketDto.getOrders())
                .cartItems(ticketDto.getCartItem())
                .build();
    }

}
