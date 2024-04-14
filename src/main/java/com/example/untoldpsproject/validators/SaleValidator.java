package com.example.untoldpsproject.validators;

import com.example.untoldpsproject.constants.OrderConstants;
import com.example.untoldpsproject.constants.SaleConstants;
import com.example.untoldpsproject.dtos.SaleDto;
import com.example.untoldpsproject.entities.Ticket;

import java.util.List;

public class SaleValidator {
    public boolean ticketsValidator(List<Ticket> tickets) throws Exception {
        if(tickets == null || tickets.isEmpty()){
            throw new Exception(SaleConstants.TICKETS_NULL);
        }else{
            return true;
        }
    }
    public boolean percentageValidator(Double percentage) throws Exception{
        if(percentage<0){
            throw new Exception(SaleConstants.INVALID_PERCENTAGE);
        }else{
            return true;
        }
    }
    public boolean saleDtoValidator(SaleDto saleDto)throws Exception{
        return ticketsValidator(saleDto.getTickets()) && percentageValidator(saleDto.getDiscountPercentage());
    }
}
