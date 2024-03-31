package com.example.untoldpsproject.services;

import com.example.untoldpsproject.dtos.CartDto;
import com.example.untoldpsproject.dtos.CategoryDto;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.*;
import com.example.untoldpsproject.mappers.CartMapper;
import com.example.untoldpsproject.mappers.CategoryMapper;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.repositories.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    public String insert(CartItem cartItem){
        System.out.println(cartItem.getTicket().getId());
        cartItemRepository.save(cartItem);
        return cartItem.getId();
    }

    public CartItem findCartItemById(String id){
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(id);
        return cartItemOptional.orElse(null);
    }
    public void update(CartItem cartItem){
        Optional<CartItem> cartOptional = cartItemRepository.findById(cartItem.getId());
        if(cartOptional.isPresent()){
            CartItem cartItem1 = cartOptional.get();
            cartItem1.setId(cartItem.getId());
            cartItem1.setTicket(cartItem.getTicket());
            cartItem1.setQuantity(cartItem.getQuantity());
            cartItem1.setCart(cartItem.getCart());
            cartItemRepository.save(cartItem);
        }
    }
    public List<CartItem> findCartItemsByCartId(String cartId){
        return cartItemRepository.findCartItemsByCartId(cartId);
    }
    public CartItem findCartItemByTicketIdAndCartId(String ticketId, String cartId){
        return cartItemRepository.findCartItemByTicketIdAndAndCartId(ticketId,cartId);
    }
    @Transactional
    public void deleteCartItemById(String cartItemId){
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if(cartItem.isPresent()){
            CartItem cartItem1 = cartItem.get();
            if(cartItem1.getQuantity()>1){
                cartItem1.setQuantity(cartItem1.getQuantity()-1);
                cartItem1.getTicket().setAvailable(cartItem1.getTicket().getAvailable());
                cartItem1.setId(cartItemId);
                cartItemRepository.save(cartItem1);
            }else{
                cartItemRepository.deleteById(cartItemId);
            }
        }
    }
}
