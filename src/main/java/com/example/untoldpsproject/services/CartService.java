package com.example.untoldpsproject.services;

import com.example.untoldpsproject.constants.CartConstants;
import com.example.untoldpsproject.dtos.CartDto;
import com.example.untoldpsproject.dtos.CartDtoIds;
import com.example.untoldpsproject.entities.*;
import com.example.untoldpsproject.mappers.CartMapper;
import com.example.untoldpsproject.repositories.CartItemRepository;
import com.example.untoldpsproject.repositories.CartRepository;
import com.example.untoldpsproject.repositories.UserRepository;
import com.example.untoldpsproject.validators.CartValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ibm.icu.text.PluralRules.Operand.c;

@Setter
@Getter
@AllArgsConstructor
@Service
public class CartService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartValidator cartValidator = new CartValidator();
    @Transactional
    public CartDto insert(CartDto cartDto) {
        try {
            cartValidator.validateCartDto(cartDto);
            Cart cart = CartMapper.toCart(cartDto);
            cart.setTotalPrice(calculateTotalPrice(cart.getCartItems()));
            cart = cartRepository.save(cart);
            LOGGER.debug(CartConstants.CART_INSERTED);
            return CartMapper.toCartDto(cart);
        }catch (Exception e){
            LOGGER.error(CartConstants.CART_NOT_INSERTED + e.getMessage());
            return cartDto;
        }
    }
    public Double calculateTotalPrice(List<CartItem> cartItems){
        Double totalPrice1 = 0.0;
        if (!cartItems.isEmpty())
            for (CartItem cartItem : cartItems) {
                totalPrice1 += cartItem.getTicket().getPrice()* cartItem.getQuantity();
            }
        return totalPrice1;
    }
    public Cart findCartById(String id){
       Optional<Cart> cartOptional= cartRepository.findById(id);
       if(cartOptional.isPresent()){
           return cartOptional.get();
       }
       return null;
    }
    public void update(CartDto cartDtoUpdated){
        Optional<Cart> cartOptional = cartRepository.findById(cartDtoUpdated.getId());
        if(cartOptional.isPresent()){
            Cart cart = cartOptional.get();
            try{
                cartValidator.validateCartDto(cartDtoUpdated);
                cart.setId(cartDtoUpdated.getId());
                cart.setCartItems(CartMapper.toCart(cartDtoUpdated).getCartItems());
                cart.setTotalPrice(CartMapper.toCart(cartDtoUpdated).getTotalPrice());
                cart.setUser(cartDtoUpdated.getUser());
                cartRepository.save(cart);
                LOGGER.error(CartConstants.CART_UPDATED);
            }catch (Exception e){
                LOGGER.error(CartConstants.CART_NOT_UPDATED);
            }
        }
    }
    public void updateTotalPrice(String cartId){
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if(cartOptional.isPresent()){
            Cart cart = cartOptional.get();
            cart.setId(cart.getId());
            cart.setCartItems(cart.getCartItems());
            cart.setTotalPrice(calculateTotalPrice(cart.getCartItems()));
            cart.setUser(cart.getUser());
            cartRepository.save(cart);
        }
    }
}
