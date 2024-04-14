package com.example.untoldpsproject.mappers;

import com.example.untoldpsproject.dtos.CartItemDto;
import com.example.untoldpsproject.entities.CartItem;

public class CartItemMapper {
    public static CartItem toCartItem(CartItemDto cartItemDto){
        return CartItem.builder()
                .id(cartItemDto.getId())
                .cart(cartItemDto.getCart())
                .ticket(cartItemDto.getTicket())
                .quantity(cartItemDto.getQuantity())
                .build();
    }
    public static CartItemDto toCartItemDto(CartItem cartItem){
        return CartItemDto.builder()
                .id(cartItem.getId())
                .cart(cartItem.getCart())
                .ticket(cartItem.getTicket())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
