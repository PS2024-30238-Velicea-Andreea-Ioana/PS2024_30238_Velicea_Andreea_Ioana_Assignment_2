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
                .discountedPrice(ticket.getDiscountedPrice())
                .available(ticket.getAvailable())
                .orders(ticket.getOrders())
                .cartItem(ticket.getCartItems())
                .sale(ticket.getSale())
                .imageUrl(ticket.getImageUrl())
                .build();
    }
    public static Ticket toTicket(TicketDto ticketDto){
        return Ticket.builder().id(ticketDto.getId())
                .category(ticketDto.getCategory())
                .price(ticketDto.getPrice())
                .discountedPrice(ticketDto.getDiscountedPrice())
                .available(ticketDto.getAvailable())
                .orders(ticketDto.getOrders())
                .cartItems(ticketDto.getCartItem())
                .sale(ticketDto.getSale())
                .imageUrl(ticketDto.getImageUrl())
                .build();
    }

}
