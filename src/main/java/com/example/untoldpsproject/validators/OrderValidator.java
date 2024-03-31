package com.example.untoldpsproject.validators;

import com.example.untoldpsproject.constants.OrderConstants;
import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.dtos.OrderDtoIds;
import com.example.untoldpsproject.entities.User;

public class OrderValidator {
    public boolean userValidator(User user) throws Exception {
        if(user == null){
            throw new Exception(OrderConstants.INVALID_USER);
        }else{
            return true;
        }
    }
    public boolean OrderDtoValidator(OrderDto orderDto) throws Exception {
        if(userValidator(orderDto.getUser())){
            return false;
        }else{
            return true;
        }
    }
}
