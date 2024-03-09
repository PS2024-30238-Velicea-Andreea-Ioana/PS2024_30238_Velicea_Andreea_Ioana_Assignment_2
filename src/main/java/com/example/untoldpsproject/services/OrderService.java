package com.example.untoldpsproject.services;

import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.dtos.OrderDtoIds;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.mappers.OrderMapper;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.repositories.OrderRepository;
import com.example.untoldpsproject.repositories.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    public UUID insert(OrderDto orderDto){
        Order order = OrderMapper.toOrder(orderDto);
        order = orderRepository.save(order);
        LOGGER.debug("Order with id {} was inserted in db",order.getId());
        return order.getId();
    }

    public List<OrderDtoIds> findOrders(){
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream().map(OrderMapper::toOrderDto).collect(Collectors.toList());
    }

    public OrderDtoIds findOrderById(UUID id){
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(!orderOptional.isPresent()){
            LOGGER.error("Order with id {} was not found in db", id);
        }
        return OrderMapper.toOrderDto(orderOptional.get());
    }

    public Order updateOrderById(UUID id, OrderDto updatedOrderDto){
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(!orderOptional.isPresent()){
            LOGGER.error("Order with id {} was not found in db", id);
        }else{
            Order order = orderOptional.get();
            Order updatedOrder = OrderMapper.toOrder(updatedOrderDto);
            order.setUser(updatedOrder.getUser());
            order.setTotalPrice(updatedOrder.getTotalPrice());
            order.setTickets(updatedOrder.getTickets());
            orderRepository.save(order);
            LOGGER.debug("Order with id {} was successfully updated", id);

        }
        return orderOptional.get();
    }

    public void deleteOrderById(UUID id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(!optionalOrder.isPresent()){
            LOGGER.error("Order with id {} was not found in db", id);
        }else{
            orderRepository.delete(optionalOrder.get());
        }
    }

}
