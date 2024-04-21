package com.example.untoldpsproject.services;

import com.example.untoldpsproject.constants.UserConstants;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.repositories.TicketRepository;
import com.example.untoldpsproject.repositories.UserRepository;
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
 * Service class for managing users.
 */
@Setter
@Getter
@AllArgsConstructor
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final UserValidator userValidator = new UserValidator();

    /**
     * Inserts a new user into the database.
     * @param userDto The user DTO containing user information.
     * @return The ID of the inserted user.
     */

    public String insert(UserDto userDto){
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
     * Retrieves all users from the database.
     * @return A list of user DTOs containing user information.
     */
    public List<UserDto> findUsers(){
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
    /**
     * Retrieves a user by ID from the database.
     * @param id The ID of the user to retrieve.
     * @return The user DTO containing user information, or null if not found.
     */
    public UserDto findUserById(String id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            LOGGER.error(UserConstants.USER_NOT_FOUND);
            return null;
        }else{
            return UserMapper.toUserDto(userOptional.get());
        }

    }
    public List<TicketDto> findTickets(){
        List<Ticket> ticketList = ticketRepository.findAll();
        return ticketList.stream().map(TicketMapper::toTicketDto).collect(Collectors.toList());
    }
    /**
     * Updates a user by ID in the database.
     *
     * @param id             The ID of the user to update.
     * @param updatedUserDto The updated user DTO containing new user information.
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
     * Deletes a user by ID from the database.
     * @param id The ID of the user to delete.
     */
    public String deleteUserById(String id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            LOGGER.error(UserConstants.USER_NOT_FOUND);
            return UserConstants.USER_NOT_FOUND;
        }else{
            if(!userOptional.get().getOrders().isEmpty()) {
                for (Order order : userOptional.get().getOrders()) {
                    if(!order.getTickets().isEmpty()){
                        for(Ticket ticket: order.getTickets()){
                            ticket.setAvailable(ticket.getAvailable()+1);
                            ticketRepository.save(ticket);
                        }
                    }
                }
            }
            userRepository.delete(userOptional.get());
            return "User with id " + id + UserConstants.USER_SUCCESS_DELETE;
        }
    }
    public User findUserByEmail(String email) {
        // Find user entity by email
        User user = userRepository.findByEmail(email);
        return user;
    }
}
