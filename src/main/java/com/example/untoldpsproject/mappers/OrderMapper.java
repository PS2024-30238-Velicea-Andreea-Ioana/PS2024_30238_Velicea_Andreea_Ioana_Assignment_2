package com.example.untoldpsproject.mappers;

import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.dtos.OrderDtoIds;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;

import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDtoIds toOrderDto(Order order){
        return OrderDtoIds.builder().id(order.getId())
                .totalPrice(order.getTotalPrice())
                .user(order.getUser().getId())
                .tickets(order.getTickets().stream().map(Ticket::getId).collect(Collectors.toList()))
                .build();
    }

    public static Order toOrder(OrderDto orderDto){
        return Order.builder().id(orderDto.getId())
                .totalPrice(orderDto.getTotalPrice())
                .user(orderDto.getUser())
                .tickets(orderDto.getTickets())
                .build();
    }

}
