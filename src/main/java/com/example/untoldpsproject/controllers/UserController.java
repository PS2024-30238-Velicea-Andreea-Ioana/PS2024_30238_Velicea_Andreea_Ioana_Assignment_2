package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.constants.PaymentConstants;
import com.example.untoldpsproject.constants.UserConstants;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.services.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /**
     * Retrieves a list of users and displays them.
     *
     * @return A ModelAndView object containing the view name and the list of users.
     */
    @GetMapping("/list")
    public ModelAndView userList() {
        ModelAndView mav = new ModelAndView("user-list");
        List<UserDto> users = userService.findUsers();
        mav.addObject("users", users);
        return mav;
    }

    /**
     * Displays the form for adding a new user.
     *
     * @return A ModelAndView object containing the view name and an empty UserDto object.
     */
    @GetMapping("/add")
    public ModelAndView addUserForm() {
        ModelAndView mav = new ModelAndView("user-add");
        mav.addObject("userDto", new UserDto());
        return mav;
    }

    /**
     * Adds a new user.
     *
     * @param userDto The UserDto object representing the user to be added.
     * @return A redirection to the user list view.
     */
    @PostMapping("/add")
    public ModelAndView addUser(@ModelAttribute("userDto") UserDto userDto, RedirectAttributes redirectAttributes) {
        String result = userService.insert(userDto);
        if(result.equals(UserConstants.USER_INSERTED)){
            return new ModelAndView("redirect:/user/list");
        }else{
            redirectAttributes.addFlashAttribute("error", result);
            return new ModelAndView("redirect:/user/add");
        }
    }

    /**
     * Displays the form for editing an existing user.
     *
     * @param userId The ID of the user to be edited.
     * @return A ModelAndView object containing the view name and the UserDto object to be edited.
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editUserForm(@PathVariable("id") String userId) {
        ModelAndView mav = new ModelAndView("user-edit");
        UserDto userDto = userService.findUserById(userId);
        mav.addObject("userDto", userDto);
        return mav;
    }

    /**
     * Updates an existing user.
     *
     * @param userDto The UserDto object representing the updated user information.
     * @return A redirection to the user list view.
     */
    @PostMapping ("/edit/{id}")
    public ModelAndView updateUser(@ModelAttribute("userDto") UserDto userDto, RedirectAttributes redirectAttributes) {
        String result = userService.updateUserById(userDto.getId(), userDto);
        if(result.equals(UserConstants.USER_UPDATED)){
            return new ModelAndView("redirect:/user/list");
        }else{
            redirectAttributes.addFlashAttribute("error", result);
            return new ModelAndView("redirect:/user/edit/"+userDto.getId());
        }
    }

    /**
     * Deletes a user.
     *
     * @param id The ID of the user to be deleted.
     * @return A redirection to the user list view.
     */
    @GetMapping("/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        String result = userService.deleteUserById(id);
        redirectAttributes.addFlashAttribute("error", result);
        return new ModelAndView("redirect:/user/list");
    }


}
