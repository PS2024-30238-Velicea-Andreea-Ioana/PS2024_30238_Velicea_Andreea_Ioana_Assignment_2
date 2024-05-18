
package com.example.untoldpsproject.services;

import com.example.untoldpsproject.constants.CartConstants;
import com.example.untoldpsproject.constants.UserConstants;
import com.example.untoldpsproject.dtos.CartDto;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.*;
import com.example.untoldpsproject.factory.Artist;
import com.example.untoldpsproject.mappers.CartMapper;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.repositories.*;
import com.example.untoldpsproject.validators.CartValidator;
import com.example.untoldpsproject.validators.UserValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Setter
@Getter
@AllArgsConstructor
public class HomePageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomePageService.class);
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final ArtistRepository artistRepository;
    private final UserValidator userValidator = new UserValidator();
    private final CartValidator cartValidator = new CartValidator();

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public String insertUser(UserDto userDto){
        try{
            userValidator.userDtoValidator(userDto);
            User user = UserMapper.toUser(userDto);
            user = userRepository.save(user);
            LOGGER.debug(UserConstants.USER_INSERTED);
            return UserConstants.USER_INSERTED;
        }catch (Exception e){
            LOGGER.error(UserConstants.USER_NOT_INSERTED + ": "+ e.getMessage());
            return UserConstants.USER_NOT_INSERTED + ": " +e.getMessage();
        }
    }
    public UserDto findUserById(String id){
        Optional<User> userOptional =  userRepository.findById(id);
        if(userOptional.isEmpty()){
            LOGGER.error(UserConstants.USER_NOT_FOUND);
            return null;
        }else{
            return UserMapper.toUserDto(userOptional.get());
        }
    }
    public CartDto insertCart(CartDto cartDto) {
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
    public List<TicketDto> findTickets(){
        List<Ticket> ticketList = ticketRepository.findAll();
        return ticketList.stream().map(TicketMapper::toTicketDto).collect(Collectors.toList());
    }
    public String updateUserById(String id, UserDto updatedUserDto) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            LOGGER.error(UserConstants.USER_NOT_FOUND);
            return UserConstants.USER_NOT_FOUND;
        } else {
            User user = userOptional.get();
            try {
                userValidator.userDtoValidator(updatedUserDto);
                User updatedUser = UserMapper.toUser(updatedUserDto);
                user.setFirstName(updatedUser.getFirstName());
                user.setLastName(updatedUser.getLastName());
                user.setEmail(updatedUser.getEmail());
                user.setPassword(updatedUser.getPassword());
                user.setRole(updatedUser.getRole());
                user.setCart(updatedUser.getCart());
                userRepository.save(user);
                LOGGER.debug(UserConstants.USER_UPDATED);
                return UserConstants.USER_UPDATED;
            } catch (Exception e) {
                LOGGER.error(UserConstants.USER_NOT_UPDATED);
                return  UserConstants.USER_NOT_UPDATED + ": " + e.getMessage();
            }
        }
    }
    public List<Category> findCategories(){
        return categoryRepository.findAll();
    }
    public List<Artist> findArtists(){
        return artistRepository.findAll();
    }
}
