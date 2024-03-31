package com.example.untoldpsproject.services;

import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.UserDto;
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

    /**
     * Inserts a new user into the database.
     * @param userDto The user DTO containing user information.
     * @return The ID of the inserted user.
     */

    public String insert(UserDto userDto){
        User user = UserMapper.toUser(userDto);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db",user.getId());
        return user.getId();
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
            LOGGER.error(" in service Person with id {"+id+"} was not found in db", id);
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
    public void updateUserById(String id, UserDto updatedUserDto){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            LOGGER.error(" in update User with id { "+id+" } was not found in the database", id);
        } else {
            User user = userOptional.get();
            User updatedUser = UserMapper.toUser(updatedUserDto);
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setRole(updatedUser.getRole());
            user.setCart(updatedUser.getCart());
            userRepository.save(user);
            LOGGER.debug("User with id {" + id +"} was successfully updated", id);
        }
    }

    /**
     * Deletes a user by ID from the database.
     * @param id The ID of the user to delete.
     */
    public void deleteUserById(String id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            LOGGER.error("Person with id {} was not found in db", id);
        }else{
            userRepository.delete(userOptional.get());
        }
    }
}
