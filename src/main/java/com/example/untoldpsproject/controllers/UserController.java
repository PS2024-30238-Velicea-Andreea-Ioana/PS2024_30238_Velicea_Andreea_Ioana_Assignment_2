package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.services.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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

    @GetMapping("/list")
    public ModelAndView userList() {
        ModelAndView mav = new ModelAndView("user-list");
        List<UserDto> users = userService.findUsers();
        mav.addObject("users", users);
        return mav;
    }
    @GetMapping("/add")
    public ModelAndView addUserForm() {
        ModelAndView mav = new ModelAndView("user-add");
        mav.addObject("userDto", new UserDto());
        return mav;
    }

    @PostMapping("/add")
    public ModelAndView addUser(@ModelAttribute("userDto") UserDto userDto) {
        userService.insert(userDto);
        return new ModelAndView("redirect:/user/list");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editUserForm(@PathVariable("id") String userId) {
        ModelAndView mav = new ModelAndView("user-edit");
        UserDto userDto = userService.findUserById(userId);
        mav.addObject("userDto", userDto);
        return mav;
    }

    @PostMapping ("/edit/{id}")
    public ModelAndView updateUser(@ModelAttribute("userDto") UserDto userDto) {
        userService.updateUserById(userDto.getId(), userDto);
        return new ModelAndView("redirect:/user/list");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") String id) {
        userService.deleteUserById(id);
        return new ModelAndView("redirect:/user/list");
    }


}
