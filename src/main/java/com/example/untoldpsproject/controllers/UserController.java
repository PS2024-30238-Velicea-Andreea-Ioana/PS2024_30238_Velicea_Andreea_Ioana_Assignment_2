package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.UserDto;
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

@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;
    @PostMapping("/insert")
    public ResponseEntity<UUID> insertUser(@RequestBody UserDto userDto){
        UUID userID = userService.insert(userDto);
        return new ResponseEntity<>(userID, HttpStatus.CREATED);
    }
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getUsers(){
        List<UserDto> dtos = userService.findUsers();
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") UUID userId){
        UserDto dto = userService.findUserById(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable("id") UUID userId, @RequestBody UserDto userDto){
        User user = userService.updateUserById(userId,userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteUserById(@PathVariable("id") UUID userId){
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
