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

/**
 * This service class provides methods to manage cart items in the system.
 */
@Service
@AllArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    /**
     * Inserts a new cart item into the database.
     *
     * @param cartItem The cart item to be inserted.
     * @return The ID of the inserted cart item.
     */
    public String insert(CartItem cartItem){
        cartItemRepository.save(cartItem);
        return cartItem.getId();
    }

    /**
     * Finds a cart item by its ID.
     *
     * @param id The ID of the cart item to find.
     * @return The found cart item, or null if not found.
     */
    public CartItem findCartItemById(String id){
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(id);
        return cartItemOptional.orElse(null);
    }

    /**
     * Updates an existing cart item.
     *
     * @param cartItem The cart item with updated information.
     */
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

    /**
     * Finds all cart items belonging to a particular cart.
     *
     * @param cartId The ID of the cart to find cart items for.
     * @return A list of cart items belonging to the specified cart.
     */
    public List<CartItem> findCartItemsByCartId(String cartId){
        return cartItemRepository.findCartItemsByCartId(cartId);
    }

    /**
     * Finds a cart item by its ticket ID and cart ID.
     *
     * @param ticketId The ID of the ticket.
     * @param cartId The ID of the cart.
     * @return The found cart item, or null if not found.
     */
    public CartItem findCartItemByTicketIdAndCartId(String ticketId, String cartId){
        return cartItemRepository.findCartItemByTicketIdAndAndCartId(ticketId,cartId);
    }

    /**
     * Deletes a cart item by its ID. If the quantity of the cart item is greater than 1,
     * it decreases the quantity by 1; otherwise, it deletes the cart item entirely.
     *
     * @param cartItemId The ID of the cart item to delete.
     */
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
