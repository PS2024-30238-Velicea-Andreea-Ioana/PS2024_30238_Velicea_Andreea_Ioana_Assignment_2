package com.example.untoldpsproject.services;

import com.example.untoldpsproject.constants.CartConstants;
import com.example.untoldpsproject.constants.CategoryConstants;
import com.example.untoldpsproject.constants.TicketConstants;
import com.example.untoldpsproject.dtos.CategoryDto;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.entities.Category;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.mappers.CategoryMapper;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.repositories.CategoryRepository;
import com.example.untoldpsproject.repositories.OrderRepository;
import com.example.untoldpsproject.repositories.TicketRepository;
import com.example.untoldpsproject.validators.TicketValidator;
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
 * Service class for managing tickets.
 */
@Setter
@Getter
@AllArgsConstructor
@Service
public class TicketService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketService.class);
    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final TicketValidator ticketValidator = new TicketValidator();

    /**
     * Inserts a new ticket into the database.
     *
     * @param ticketDto The ticket DTO containing information about the ticket.
     * @return The String of the inserted ticket.
     */
    public String insert(TicketDto ticketDto){
        try{
            ticketValidator.ticketDtoValidator(ticketDto);
            Ticket ticket = TicketMapper.toTicket(ticketDto);
            ticket.setDiscountedPrice(ticketDto.getPrice());
            ticket = ticketRepository.save(ticket);
            LOGGER.debug(TicketConstants.TICKET_INSERTED);
            return TicketConstants.TICKET_INSERTED;
        }catch (Exception e){
            LOGGER.error(TicketConstants.TICKET_NOT_INSERTED + ": " +e.getMessage());
            return TicketConstants.TICKET_NOT_INSERTED+ ": "+e.getMessage();
        }

    }

    /**
     * Retrieves all tickets from the database.
     *
     * @return A list of ticket DTOs.
     */
    public List<TicketDto> findTickets(){
        List<Ticket> ticketList = ticketRepository.findAll();
        return ticketList.stream().map(TicketMapper::toTicketDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a ticket by its ID from the database.
     *
     * @param id The ID of the ticket to retrieve.
     * @return The ticket DTO.
     */
    public TicketDto findTicketById(String id){
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(ticketOptional.isEmpty()){
            LOGGER.error(TicketConstants.TICKET_NOT_FOUND);
            return null;
        }else{
            return TicketMapper.toTicketDto(ticketOptional.get());
        }

    }

    public CategoryDto findCategoryById(String id){
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isEmpty()){
            LOGGER.error(CategoryConstants.CATEGORY_NOT_FOUND);
            return null;
        }else{
            return CategoryMapper.toCategoryDto(categoryOptional.get());
        }
    }
    public List<CategoryDto> findCategories(){
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    /**
     * Updates a ticket in the database.
     *
     * @param id The ID of the ticket to update.
     * @param updatedTicketDto The updated ticket DTO.
     */
    public String updateTicketById(String id, TicketDto updatedTicketDto){
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(ticketOptional.isEmpty()){
            LOGGER.error(TicketConstants.TICKET_NOT_FOUND);
            return TicketConstants.TICKET_NOT_FOUND;
        }else{
            Ticket ticket = ticketOptional.get();
            try {
                ticketValidator.ticketDtoValidator(updatedTicketDto);
                Ticket updatedTicket = TicketMapper.toTicket(updatedTicketDto);
                ticket.setCategory(updatedTicket.getCategory());
                ticket.setPrice(updatedTicket.getPrice());
                ticket.setAvailable(updatedTicket.getAvailable());
                ticket.setCartItems(updatedTicketDto.getCartItem());
                ticketRepository.save(ticket);
                if(!ticket.getOrders().isEmpty()) {
                    List<Order> orders = ticket.getOrders();
                    for (Order order : orders) {
                        order.setTotalPrice(calculateTotalPrice(order.getTickets()));
                        orderRepository.save(order);
                    }
                }
                LOGGER.debug(TicketConstants.TICKET_UPDATED);
                return TicketConstants.TICKET_UPDATED;
            }catch (Exception e){
                LOGGER.error(TicketConstants.TICKET_NOT_UPDATED + " " + e.getMessage());
                return TicketConstants.TICKET_NOT_UPDATED+": "+e.getMessage();
            }

        }
    }
    /**
     * Deletes a ticket from the database.
     *
     * @param id The ID of the ticket to delete.
     */
    public String deleteTicketById(String id){
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(ticketOptional.isEmpty()){
            LOGGER.error(TicketConstants.TICKET_NOT_FOUND);
            return TicketConstants.TICKET_NOT_FOUND;
        }else{
            ticketRepository.delete(ticketOptional.get());
            return "Ticket with id "+ id + TicketConstants.TICKET_SUCCESS_DELETE;
        }
    }
    public Double calculateTotalPrice(List<Ticket> tickets){
        Double totalPrice1 = 0.0;
        if (!tickets.isEmpty())
            for (Ticket ticket : tickets) {
                totalPrice1 += ticket.getPrice();
            }
        return totalPrice1;
    }
}
