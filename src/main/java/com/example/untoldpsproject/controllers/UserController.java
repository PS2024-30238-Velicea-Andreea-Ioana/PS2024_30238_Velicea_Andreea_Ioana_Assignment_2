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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
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

//    /**
//     * Inserts a new user.
//     *
//     * @param userDto The user DTO containing information about the user.
//     * @return The String of the inserted user.
//     */
//    @PostMapping("/insert")
//    public ResponseEntity<String> insertUser(@RequestBody UserDto userDto){
//        String userId = userService.insert(userDto);
//        return new ResponseEntity<>(userId, HttpStatus.CREATED);
//    }
//
//    /**
//     * Retrieves all users.
//     *
//     * @return A list of user DTOs.
//     */
//    @GetMapping("/getAllUsers")
//    public ResponseEntity<List<UserDtoIds>> getUsers(){
//        List<UserDtoIds> dtos = userService.findUsers();
//        return new ResponseEntity<>(dtos,HttpStatus.OK);
//    }
//
//    /**
//     * Retrieves a user by its ID.
//     *
//     * @param userId The ID of the user to retrieve.
//     * @return The user DTO.
//     */
//    @GetMapping(value = "/{id}")
//    public ResponseEntity<UserDtoIds> getUserById(@PathVariable("id") String userId){
//        UserDtoIds dto = userService.findUserById(userId);
//        return new ResponseEntity<>(dto, HttpStatus.OK);
//    }
//
//    /**
//     * Updates a user by its ID.
//     *
//     * @param userId The ID of the user to update.
//     * @param userDto The updated user DTO.
//     * @return The updated user entity.
//     */
//    @PutMapping(value = "/{id}")
//    public ResponseEntity<User> updateUserById(@PathVariable("id") String userId, @RequestBody UserDto userDto){
//        User user = userService.updateUserById(userId,userDto);
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }
//
//    /**
//     * Deletes a user by its ID.
//     *
//     * @param userId The ID of the user to delete.
//     * @return HttpStatus indicating the success of the operation.
//     */
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<String> deleteUserById(@PathVariable("id") String userId){
//        userService.deleteUserById(userId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
    @GetMapping("/list")
    public ModelAndView userList() {
        ModelAndView mav = new ModelAndView("user-list");
        List<UserDtoIds> users = userService.findUsers();
        mav.addObject("users", users);
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editUserForm(@PathVariable("id") String userId) {
        ModelAndView mav = new ModelAndView("user-edit");
        UserDtoIds userDto = userService.findUserById(userId);
        mav.addObject("userDto", userDto);
        return mav;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView updateUser(@PathVariable("id") String id, @ModelAttribute UserDto userDto) {
        userService.updateUserById(id, userDto);
        return new ModelAndView("redirect:/user/list");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") String id) {
        userService.deleteUserById(id);
        return new ModelAndView("redirect:/user/list");
    }

    @GetMapping("/add")
    public ModelAndView addUserForm() {
        ModelAndView mav = new ModelAndView("user-add");
        mav.addObject("userDto", new UserDto());
        return mav;
    }

    @PostMapping("/add")
    public ModelAndView addUser(@ModelAttribute UserDto userDto) {
        userService.insert(userDto);
        return new ModelAndView("redirect:/user/list");
    }
}
