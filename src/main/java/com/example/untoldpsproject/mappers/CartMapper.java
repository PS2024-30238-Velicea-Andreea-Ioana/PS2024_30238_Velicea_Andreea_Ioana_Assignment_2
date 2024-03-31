package com.example.untoldpsproject.mappers;

import com.example.untoldpsproject.dtos.CartDto;
import com.example.untoldpsproject.entities.Cart;

public class CartMapper {
    public static Cart toCart(CartDto cartDto){
        return Cart.builder().id(cartDto.getId())
                .user(cartDto.getUser())
                .cartItems(cartDto.getCartItems())
                .totalPrice(cartDto.getTotalPrice())
                .build();
    }
    public static CartDto toCartDto(Cart cart){
        return CartDto.builder().id(cart.getId())
                .user(cart.getUser())
                .cartItems(cart.getCartItems())
                .totalPrice(cart.getTotalPrice())
                .build();
    }
}
