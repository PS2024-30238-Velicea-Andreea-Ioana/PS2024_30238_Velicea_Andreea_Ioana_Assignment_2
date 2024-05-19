package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.constants.TicketConstants;
import com.example.untoldpsproject.dtos.*;
import com.example.untoldpsproject.mappers.CategoryMapper;
import com.example.untoldpsproject.services.TicketService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class for managing ticket operations.
 */
@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/ticket")
public class TicketController {

    private final TicketService ticketService;
    private static final String directoryPath = "src/main/resources/static/images";

    /**
     * Retrieves a list of tickets and displays them.
     *
     * @return A ModelAndView object containing the view name and the list of tickets.
     */
    @GetMapping("/list/{userId}")
    public ModelAndView ticketsList(@PathVariable("userId")String userId) {
        ModelAndView mav = new ModelAndView("ticket-list");
        List<TicketDto> tickets = ticketService.findTickets();
        mav.addObject("tickets", tickets);
        mav.addObject("userId", userId);
        return mav;
    }

    /**
     * Displays the form for adding a new ticket.
     *
     * @return A ModelAndView object containing the view name and an empty TicketDto object.
     */
    @GetMapping("/add/{userId}")
    public ModelAndView addTicketForm(@PathVariable("userId") String userId) {
        ModelAndView mav = new ModelAndView("ticket-add");
        mav.addObject("ticketDto", new TicketDto());
        List<CategoryDto> categories = ticketService.findCategories();
        mav.addObject("categories", categories);
        mav.addObject("userId", userId);
        List<String> imageNames = getImageFiles();
        mav.addObject("imageNames", imageNames);
        return mav;
    }
    private List<String> getImageFiles() {
        List<String> imageNames = new ArrayList<>();
        File folder = new File(directoryPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    imageNames.add(file.getName());
                }
            }
        }
        return imageNames;
    }
    /**
     * Adds a new ticket.
     *
     * @param ticketDtoIds The TicketDtoIds object representing the ticket to be added.
     * @return A redirection to the ticket list view.
     */
    @PostMapping("/add/{userId}")
    public ModelAndView addTicket(@PathVariable("userId") String userId,@ModelAttribute("ticketDto") TicketDtoIds ticketDtoIds, RedirectAttributes redirectAttributes) {
        CategoryDto category = ticketService.findCategoryById(ticketDtoIds.getCategory());
        TicketDto ticket = new TicketDto();
        ticket.setCategory(CategoryMapper.toCategory(category));
        ticket.setAvailable(ticketDtoIds.getAvailable());
        ticket.setPrice(ticketDtoIds.getPrice());
        String imageUrl = "/images/" + ticketDtoIds.getImageUrl();
        ticket.setImageUrl(imageUrl);
        String result = ticketService.insert(ticket);
        if(result.equals(TicketConstants.TICKET_INSERTED)){
            return new ModelAndView("redirect:/ticket/list/"+userId);
        }else{
            redirectAttributes.addFlashAttribute("error", result);
            return new ModelAndView("redirect:/ticket/add/"+userId);
        }

    }

    /**
     * Displays the form for editing an existing ticket.
     *
     * @param ticketId The ID of the ticket to be edited.
     * @return A ModelAndView object containing the view name and the TicketDto object to be edited.
     */
    @GetMapping("/edit/{id}/{userId}")
    public ModelAndView editTicketForm(@PathVariable("userId") String userId, @PathVariable("id") String ticketId) {
        ModelAndView mav = new ModelAndView("ticket-edit");
        TicketDto ticketDto = ticketService.findTicketById(ticketId);
        mav.addObject("ticketDto", ticketDto);
        List<CategoryDto> categories = ticketService.findCategories();
        mav.addObject("categories", categories);
        mav.addObject("userId", userId);
        List<String> imageNames = getImageFiles();
        mav.addObject("imageNames", imageNames);
        return mav;
    }

    /**
     * Updates an existing ticket.
     *
     * @param ticketDto The TicketDto object representing the updated ticket information.
     * @return A redirection to the ticket list view.
     */
    @PostMapping("/edit/{id}/{userId}")
    public ModelAndView updateTicket(@PathVariable("userId") String userId, @ModelAttribute("ticketDto") TicketDto ticketDto,RedirectAttributes redirectAttributes) {
        String result = ticketService.updateTicketById(ticketDto.getId(), ticketDto);
        if(result.equals(TicketConstants.TICKET_UPDATED)){
            return new ModelAndView("redirect:/ticket/list/"+userId);
        }else{
            redirectAttributes.addFlashAttribute("error",result);
            return new ModelAndView("redirect:/ticket/edit/"+ticketDto.getId()+"/"+userId);
        }

    }

    /**
     * Deletes a ticket.
     *
     * @param id The ID of the ticket to be deleted.
     * @return A redirection to the ticket list view.
     */
    @GetMapping("/delete/{id}/{userId}")
    public ModelAndView deleteTicket(@PathVariable("userId") String userId, @PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        String result = ticketService.deleteTicketById(id);
        redirectAttributes.addFlashAttribute("error", result);
        return new ModelAndView("redirect:/ticket/list/"+userId);
    }
}