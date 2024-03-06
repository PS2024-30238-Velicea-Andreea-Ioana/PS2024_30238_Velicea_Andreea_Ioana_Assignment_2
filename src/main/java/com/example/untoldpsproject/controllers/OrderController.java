package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.services.OrderService;
import com.example.untoldpsproject.services.TicketService;
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
@RequestMapping(value = "/order")
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/insert")
    public ResponseEntity<UUID> insertOrder(@RequestBody OrderDto orderDto){
        UUID orderId = orderService.insert(orderDto);
        return new ResponseEntity<>(orderId, HttpStatus.CREATED);
    }
    @GetMapping("/getAllOrders")
    public ResponseEntity<List<OrderDto>> getOrder(){
        List<OrderDto> dtos = orderService.findOrders();
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") UUID orderId){
        OrderDto dto = orderService.findOrderById(orderId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Order> updateOrderById(@PathVariable("id") UUID orderId, @RequestBody OrderDto orderDto){
        Order order = orderService.updateOrderById(orderId,orderDto);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteOrderById(@PathVariable("id") UUID orderId){
        orderService.deleteOrderById(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
