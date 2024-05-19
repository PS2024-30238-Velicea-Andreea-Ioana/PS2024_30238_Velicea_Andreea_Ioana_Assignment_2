package com.example.untoldpsproject.services;

import com.example.untoldpsproject.constants.CartConstants;
import com.example.untoldpsproject.constants.TicketConstants;
import com.example.untoldpsproject.constants.UserConstants;
import com.example.untoldpsproject.dtos.*;
import com.example.untoldpsproject.entities.*;
import com.example.untoldpsproject.mappers.CartItemMapper;
import com.example.untoldpsproject.mappers.CartMapper;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.repositories.CartItemRepository;
import com.example.untoldpsproject.repositories.CartRepository;
import com.example.untoldpsproject.repositories.TicketRepository;
import com.example.untoldpsproject.repositories.UserRepository;
import com.example.untoldpsproject.validators.CartValidator;
import com.example.untoldpsproject.validators.TicketValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing shopping carts in the system.
 */
@Setter
@Getter
@AllArgsConstructor
@Service
public class CartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final TicketRepository ticketRepository;
    private final CartValidator cartValidator = new CartValidator();

    /**
     * Inserts a new shopping cart into the database.
     *
     * @param cartDto The DTO representing the shopping cart to be inserted.
     * @return The DTO representing the inserted shopping cart, or the original DTO if insertion fails.
     */
    @Transactional
    public CartDto insert(CartDto cartDto) {
        try {
            cartValidator.validateCartDto(cartDto);
            Cart cart = CartMapper.toCart(cartDto);
            cart.setTotalPrice(calculateTotalPrice(cart.getCartItems()));
            cart = cartRepository.save(cart);
            LOGGER.debug(CartConstants.CART_INSERTED);
            return CartMapper.toCartDto(cart);
        } catch (Exception e) {
            LOGGER.error(CartConstants.CART_NOT_INSERTED + e.getMessage());
            return cartDto;
        }
    }

    /**
     * Calculates the total price of the items in the shopping cart.
     *
     * @param cartItems The list of cart items in the shopping cart.
     * @return The total price of the items in the shopping cart.
     */
    public Double calculateTotalPrice(List<CartItem> cartItems){
        Double totalPrice1 = 0.0;
        if (!cartItems.isEmpty())
            for (CartItem cartItem : cartItems) {
                if(cartItem.getTicket().getDiscountedPrice() == null)
                    totalPrice1 += cartItem.getTicket().getPrice()* cartItem.getQuantity();
                else{
                    totalPrice1 += cartItem.getTicket().getDiscountedPrice()* cartItem.getQuantity();
                }
            }
        return totalPrice1;
    }

    /**
     * Finds a shopping cart by its ID.
     *
     * @param id The ID of the shopping cart to find.
     * @return The found shopping cart, or null if not found.
     */
    public Cart findCartById(String id){
        Optional<Cart> cartOptional= cartRepository.findById(id);
        if(cartOptional.isPresent()){
            return cartOptional.get();
        }
        return null;
    }

    /**
     * Updates an existing shopping cart.
     *
     * @param cartDtoUpdated The updated DTO representing the shopping cart.
     */
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

    /**
     * Updates the total price of a shopping cart.
     *
     * @param cartId The ID of the shopping cart to update.
     */
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

    /**
     * Retrieves a cart item by its ID.
     *
     * @param id The ID of the cart item.
     * @return The cart item if found, otherwise null.
     */
    public CartItem getCartItemById(String id){
        Optional<CartItem> cartItem =  cartItemRepository.findById(id);
        if(cartItem.isPresent()){
            return cartItem.get();
        }
        return null;
    }

    /**
     * Decreases the quantity of a cart item.
     *
     * @param cartItemId The ID of the cart item to decrease.
     * @return The ID of the cart after the operation, or null if the operation fails.
     */
    public String decreaseCartItem(String cartItemId){
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent()) {
            String cartId = cartItem.get().getCart().getId();
            if(cartItem.get().getQuantity() > 1){
                cartItem.get().setQuantity(cartItem.get().getQuantity()-1);
                cartItemRepository.save(cartItem.get());
            }
            updateTotalPrice(cartId);
            return cartId;
        }
        return null;
    }

    /**
     * Increases the quantity of a cart item.
     *
     * @param cartItemId The ID of the cart item to increase.
     * @return The ID of the cart after the operation, or null if the operation fails.
     */
    public String increaseCartItem(String cartItemId){
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent()) {
            String cartId = cartItem.get().getCart().getId();
            if(cartItem.get().getTicket().getAvailable() >= cartItem.get().getQuantity() + 1){
                cartItem.get().setQuantity(cartItem.get().getQuantity()+1);
                cartItemRepository.save(cartItem.get());
            }
            updateTotalPrice(cartId);
            return cartId;
        }
        return null;
    }

    /**
     * Finds a ticket by its ID.
     *
     * @param ticketId The ID of the ticket.
     * @return The ticket if found, otherwise null.
     */
    public Ticket findTicket(String ticketId){
        Optional<Ticket> ticket =  ticketRepository.findById(ticketId);
        if(!ticket.isPresent()){
            LOGGER.error(TicketConstants.TICKET_NOT_FOUND);
        }
        return ticket.get();
    }

    /**
     * Finds a user by its ID.
     *
     * @param userId The ID of the user.
     * @return The user if found, otherwise null.
     */
    public User findUser(String userId){
        Optional<User> user =  userRepository.findById(userId);
        if(!user.isPresent()){
            LOGGER.error(UserConstants.USER_NOT_FOUND);
        }
        return user.get();
    }

    /**
     * Finds a cart item by ticket ID and cart ID.
     *
     * @param ticketId The ID of the ticket.
     * @param cartId   The ID of the cart.
     * @return The cart item if found, otherwise null.
     */
    public CartItem findCartItemByTicketIdAndCartId(String ticketId, String cartId){
        return cartItemRepository.findCartItemByTicketIdAndAndCartId(ticketId,cartId);
    }

    /**
     * Inserts a cart item into the database.
     *
     * @param cartItemDto The DTO representing the cart item to be inserted.
     * @return The ID of the inserted cart item.
     */
    public String insertCartItem(CartItemDto cartItemDto){
        cartItemRepository.save(CartItemMapper.toCartItem(cartItemDto));
        return cartItemDto.getId();
    }


    /**
     * Updates a cart item in the database.
     *
     * @param cartItemDto The DTO representing the updated cart item.
     */
    public void updateCartItem(CartItemDto cartItemDto){
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

    /**
     * Removes a cart item from the database.
     *
     * @param cartItemId The ID of the cart item to be removed.
     * @return The ID of the cart after the operation, or null if the operation fails.
     */
    public String removeCartItem(String cartItemId){
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent()) {
            String cartId = cartItem.get().getCart().getId();
            cartItemRepository.delete(cartItem.get());
            updateTotalPrice(cartId);
            return cartId;
        }
        return null;
    }

}