package com.example.untoldpsproject.services;

import com.example.untoldpsproject.constants.OrderConstants;
import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.dtos.OrderDtoIds;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.mappers.OrderMapper;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.repositories.OrderRepository;
import com.example.untoldpsproject.repositories.TicketRepository;
import com.example.untoldpsproject.repositories.UserRepository;
import com.example.untoldpsproject.validators.OrderValidator;
import jakarta.transaction.Transactional;
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
 * Service class for managing orders.
 */
@Setter
@Getter
@AllArgsConstructor
@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final OrderValidator orderValidator = new OrderValidator();


    /**
     * Inserts a new order into the database.
     *
     * @param orderDto The order DTO containing information about the order.
     * @return The String of the inserted order.
     */
    @Transactional
    public String insert(OrderDto orderDto){
        try{
            orderValidator.OrderDtoValidator(orderDto);
            Order order = OrderMapper.toOrder(orderDto);
            order.setTotalPrice(calculateTotalPrice(order.getTickets()));
            order = orderRepository.save(order);
            LOGGER.debug(OrderConstants.ORDER_INSERTED);
            for (Ticket ticket : order.getTickets()) {
                if(ticketRepository.findById(ticket.getId()).isPresent())
                    ticketRepository.findById(ticket.getId()).get().setAvailable(ticket.getAvailable()-1);
            }
            return order.getId();
        }catch (Exception e){
            LOGGER.error(OrderConstants.ORDER_NOT_INSERTED + " " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return A list of order DTOs.
     */
    public List<OrderDto> findOrders(){
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream().map(OrderMapper::toOrderDto).collect(Collectors.toList());
    }

    /**
     * Retrieves an order by its ID from the database.
     *
     * @param id The ID of the order to retrieve.
     * @return The order DTO.
     */
    public OrderDto findOrderById(String id){
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isEmpty()){
            LOGGER.error("Order with id {} was not found in db", id);
            return null;
        }else{
            return OrderMapper.toOrderDto(orderOptional.get());
        }

    }
    public List<UserDto> findUsers(){
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
    public UserDto findUserById(String id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            LOGGER.error(" in service Person with id {"+id+"} was not found in db", id);
            return null;
        }else{
            return UserMapper.toUserDto(userOptional.get());
        }

    }
    public TicketDto findTicketById(String id){
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if(ticketOptional.isEmpty()){
            LOGGER.error("Ticket with id {} was not found in db", id);
            return null;
        }else{
            return TicketMapper.toTicketDto(ticketOptional.get());
        }

    }
    public List<TicketDto> findTickets(){
        List<Ticket> ticketList = ticketRepository.findAll();
        return ticketList.stream().map(TicketMapper::toTicketDto).collect(Collectors.toList());
    }

    /**
     * Updates an order in the database.
     *
     * @param id The ID of the order to update.
     * @param updatedOrderDto The updated order DTO.
     */
    public void updateOrderById(String id, OrderDto updatedOrderDto){
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isEmpty()){
            LOGGER.error("Order with id {} was not found in db", id);
        }else{
            Order order = orderOptional.get();
            try {
                orderValidator.OrderDtoValidator(updatedOrderDto);
                Order updatedOrder = OrderMapper.toOrder(updatedOrderDto);
                order.setUser(updatedOrder.getUser());
                order.setTickets(updatedOrder.getTickets());
                order.setTotalPrice(calculateTotalPrice(order.getTickets()));
                orderRepository.save(order);
                LOGGER.debug(OrderConstants.ORDER_UPDATED);
            } catch (Exception e) {
                LOGGER.error(OrderConstants.ORDER_NOT_UPDATED);
            }
        }
    }

    /**
     * Deletes an order from the database.
     *
     * @param id The ID of the order to delete.
     */
    @Transactional
    public void deleteOrderById(String id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(optionalOrder.isEmpty()){
            LOGGER.error("Order with id {} was not found in db", id);
        }else{
            for (Ticket ticket :  optionalOrder.get().getTickets()) {
                if(ticketRepository.findById(ticket.getId()).isPresent())
                    ticketRepository.findById(ticket.getId()).get().setAvailable(ticket.getAvailable() + 1);
            }
            orderRepository.delete(optionalOrder.get());
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
