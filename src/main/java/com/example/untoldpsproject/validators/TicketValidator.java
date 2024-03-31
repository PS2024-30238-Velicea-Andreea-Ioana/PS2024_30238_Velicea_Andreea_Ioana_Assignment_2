package com.example.untoldpsproject.validators;

import com.example.untoldpsproject.constants.TicketConstants;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.TicketDtoIds;
import com.example.untoldpsproject.dtos.UserDtoIds;

public class TicketValidator {
    public boolean validatePrice(Double price) throws Exception {
        if (price == null || price <= 0) {
            throw new Exception(TicketConstants.INVALID_PRICE);
        }else{
            return true;
        }
    }
    public boolean validateAvailable(int available) throws Exception {
        if (available < 0) {
            throw new Exception(TicketConstants.INVALID_AVAILABILITY);
        }
        return true;
    }
    public boolean ticketDtoValidator(TicketDto ticketDto) throws Exception{
        if(validatePrice(ticketDto.getPrice()) && validateAvailable(ticketDto.getAvailable())){
            return true;
        } else {
            return false;
        }
    }

}
