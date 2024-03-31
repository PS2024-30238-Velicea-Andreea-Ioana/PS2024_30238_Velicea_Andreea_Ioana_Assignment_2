package com.example.untoldpsproject.validators;

import com.example.untoldpsproject.constants.OrderConstants;
import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.dtos.OrderDtoIds;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;

import java.util.List;

public class OrderValidator {
    public boolean userValidator(User user) throws Exception {
        if(user == null){
            throw new Exception(OrderConstants.INVALID_USER);
        }else{
            return true;
        }
    }
    public boolean ticketsValidator(List<Ticket> tickets) throws Exception {
        if(tickets == null || tickets.isEmpty()){
            throw new Exception(OrderConstants.INVALID_TICKETS);
        }else{
            return true;
        }
    }
    public boolean OrderDtoValidator(OrderDto orderDto) throws Exception {
        if(userValidator(orderDto.getUser()) || ticketsValidator(orderDto.getTickets())){
            return false;
        }else{
            return true;
        }
    }
}
