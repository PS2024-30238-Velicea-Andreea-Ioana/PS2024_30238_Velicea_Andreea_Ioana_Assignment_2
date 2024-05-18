
package com.example.untoldpsproject.services;
import com.example.untoldpsproject.dtos.CartItemDto;
import com.example.untoldpsproject.entities.*;
import com.example.untoldpsproject.mappers.CartItemMapper;
import com.example.untoldpsproject.repositories.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
     * @param cartItemDto The cart item to be inserted.
     * @return The ID of the inserted cart item.
     */
    public String insert(CartItemDto cartItemDto){
        cartItemRepository.save(CartItemMapper.toCartItem(cartItemDto));
        return cartItemDto.getId();
    }

    /**
     * Updates an existing cart item.
     *
     * @param cartItemDto The cart item with updated information.
     */
    public void update(CartItemDto cartItemDto){
        Optional<CartItem> cartOptional = cartItemRepository.findById(cartItemDto.getId());
        if(cartOptional.isPresent()){
            CartItem cartItem1 = cartOptional.get();
            cartItem1.setId(cartItemDto.getId());
            cartItem1.setTicket(cartItemDto.getTicket());
            cartItem1.setQuantity(cartItemDto.getQuantity());
            cartItem1.setCart(cartItemDto.getCart());
            cartItemRepository.save(CartItemMapper.toCartItem(cartItemDto));
        }
    }
}
