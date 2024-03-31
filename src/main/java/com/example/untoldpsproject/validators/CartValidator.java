package com.example.untoldpsproject.validators;

import com.example.untoldpsproject.constants.CartConstants;
import com.example.untoldpsproject.dtos.CartDto;
import com.example.untoldpsproject.entities.CartItem;
import com.example.untoldpsproject.entities.User;

import java.util.List;

public class CartValidator {
    public boolean validateUser(User user) throws Exception {
        if(user == null){
            throw new Exception(CartConstants.INVALID_USER);
        }
        return true;
    }
    public boolean validateCartItems(List<CartItem> cartItems) throws Exception {
       if(cartItems==null || cartItems.isEmpty()){
           throw new Exception(CartConstants.INVALID_CART_ITEMS);
       }
       return true;
    }
    public boolean validateTotalPrice(Double price) throws Exception {
        if(price < 0.0){
            throw new Exception(CartConstants.INVALID_PRICE);
        }
        return true;
    }
    public boolean validateCartDto (CartDto cartDto) throws Exception {
        if(validateUser(cartDto.getUser()) || validateCartItems(cartDto.getCartItems()) || validateTotalPrice(cartDto.getTotalPrice()))
        {
            return false;
        }
        return true;
    }
}
