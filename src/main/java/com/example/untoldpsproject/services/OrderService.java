package com.example.untoldpsproject.services;

import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.dtos.OrderDtoIds;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.mappers.OrderMapper;
import com.example.untoldpsproject.repositories.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    @PersistenceContext
    private EntityManager entityManager;
    /**
     * Inserts a new order into the database.
     *
     * @param orderDto The order DTO containing information about the order.
     * @return The UUID of the inserted order.
     */
    @Transactional
    public UUID insert(OrderDto orderDto){
        Order order = OrderMapper.toOrder(orderDto);
        order.setTotalPrice();
        if (order.getTickets() == null) {
            order.setTickets(new ArrayList<>());
        }
        for (int i = 0; i < order.getTickets().size(); i++) {
            Ticket ticket = order.getTickets().get(i);
            ticket.setAvailable(ticket.getAvailable()-1);
            ticket.setQuantity(ticket.getQuantity()+1);
            for (int j = i + 1; j < order.getTickets().size(); j++) {
                Ticket nextTicket = order.getTickets().get(j);
                ticket.setAvailable(ticket.getAvailable()-1);
                ticket.setQuantity(ticket.getQuantity()+1);
                if (ticket.getId().equals(nextTicket.getId())) {
                    order.getTickets().remove(j);
                    j--;
                }
            }
        }
        order = orderRepository.save(order);
        LOGGER.debug("Order with id {} was inserted in db",order.getId());
        for (Ticket ticket : order.getTickets()) {
            String updateQuery = "UPDATE Ticket t SET t.available = t.available - :quantity WHERE t.id = :ticketId";
            entityManager.createQuery(updateQuery)
                    .setParameter("quantity", ticket.getQuantity())
                    .setParameter("ticketId", ticket.getId())
                    .executeUpdate();
        }
        return order.getId();
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return A list of order DTOs.
     */
    public List<OrderDtoIds> findOrders(){
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream().map(OrderMapper::toOrderDto).collect(Collectors.toList());
    }

    /**
     * Retrieves an order by its ID from the database.
     *
     * @param id The ID of the order to retrieve.
     * @return The order DTO.
     */
    public OrderDtoIds findOrderById(UUID id){
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(!orderOptional.isPresent()){
            LOGGER.error("Order with id {} was not found in db", id);
        }
        return OrderMapper.toOrderDto(orderOptional.get());
    }

    /**
     * Updates an order in the database.
     *
     * @param id The ID of the order to update.
     * @param updatedOrderDto The updated order DTO.
     * @return The updated order entity.
     */
    public Order updateOrderById(UUID id, OrderDto updatedOrderDto){
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(!orderOptional.isPresent()){
            LOGGER.error("Order with id {} was not found in db", id);
        }else{
            Order order = orderOptional.get();
            Order updatedOrder = OrderMapper.toOrder(updatedOrderDto);
            order.setUser(updatedOrder.getUser());
            order.setTickets(updatedOrder.getTickets());
            order.setTotalPrice();
            orderRepository.save(order);
            LOGGER.debug("Order with id {} was successfully updated", id);

        }
        return orderOptional.get();
    }

    /**
     * Deletes an order from the database.
     *
     * @param id The ID of the order to delete.
     */
    public void deleteOrderById(UUID id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(!optionalOrder.isPresent()){
            LOGGER.error("Order with id {} was not found in db", id);
        }else{
            orderRepository.delete(optionalOrder.get());
        }
    }

}
