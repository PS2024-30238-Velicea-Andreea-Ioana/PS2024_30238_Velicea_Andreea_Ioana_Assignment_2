
package com.example.untoldpsproject.services;

import com.example.untoldpsproject.constants.CartConstants;
import com.example.untoldpsproject.constants.UserConstants;
import com.example.untoldpsproject.dtos.CartDto;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.*;
import com.example.untoldpsproject.entities.Artist;
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


/**
 * Service class providing various functionalities for the home page.
 */
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

    /**
     * Finds a user by email.
     *
     * @param email The email of the user to find.
     * @return The found user, or null if not found.
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Inserts a new user.
     *
     * @param userDto The DTO representing the user to insert.
     * @return A success or error message indicating the result of the operation.
     */
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

    /**
     * Finds a user by ID.
     *
     * @param id The ID of the user to find.
     * @return The found user as a DTO, or null if not found.
     */
    public UserDto findUserById(String id){
        Optional<User> userOptional =  userRepository.findById(id);
        if(userOptional.isEmpty()){
            LOGGER.error(UserConstants.USER_NOT_FOUND);
            return null;
        }else{
            return UserMapper.toUserDto(userOptional.get());
        }
    }

    /**
     * Inserts a new cart.
     *
     * @param cartDto The DTO representing the cart to insert.
     * @return The DTO representing the inserted cart, or the original DTO if insertion fails.
     */
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

    /**
     * Calculates the total price of items in the cart.
     *
     * @param cartItems The list of cart items.
     * @return The total price of items in the cart.
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
     * Finds all tickets.
     *
     * @return A list of all tickets.
     */
    public List<TicketDto> findTickets(){
        List<Ticket> ticketList = ticketRepository.findAll();
        return ticketList.stream().map(TicketMapper::toTicketDto).collect(Collectors.toList());
    }

    /**
     * Updates a user by ID.
     *
     * @param id              The ID of the user to update.
     * @param updatedUserDto  The DTO representing the updated user data.
     * @return A success or error message indicating the result of the operation.
     */
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
    /**
     * Finds all categories.
     *
     * @return A list of all categories.
     */
    public List<Category> findCategories(){
        return categoryRepository.findAll();
    }
    /**
     * Finds all artists.
     *
     * @return A list of all artists.
     */
    public List<Artist> findArtists(){
        return artistRepository.findAll();
    }
}
