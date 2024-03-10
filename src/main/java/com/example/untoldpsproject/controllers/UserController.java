package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.dtos.UserDtoIds;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
/**
 * Controller class for managing users.
 */
@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;

    /**
     * Inserts a new user.
     *
     * @param userDto The user DTO containing information about the user.
     * @return The UUID of the inserted user.
     */
    @PostMapping("/insert")
    public ResponseEntity<UUID> insertUser(@RequestBody UserDto userDto){
        UUID userId = userService.insert(userDto);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    /**
     * Retrieves all users.
     *
     * @return A list of user DTOs.
     */
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDtoIds>> getUsers(){
        List<UserDtoIds> dtos = userService.findUsers();
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user DTO.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDtoIds> getUserById(@PathVariable("id") UUID userId){
        UserDtoIds dto = userService.findUserById(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Updates a user by its ID.
     *
     * @param userId The ID of the user to update.
     * @param userDto The updated user DTO.
     * @return The updated user entity.
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable("id") UUID userId, @RequestBody UserDto userDto){
        User user = userService.updateUserById(userId,userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Deletes a user by its ID.
     *
     * @param userId The ID of the user to delete.
     * @return HttpStatus indicating the success of the operation.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteUserById(@PathVariable("id") UUID userId){
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
