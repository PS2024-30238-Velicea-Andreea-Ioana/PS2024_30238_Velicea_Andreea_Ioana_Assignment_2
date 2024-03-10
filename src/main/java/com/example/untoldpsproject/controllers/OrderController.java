package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.OrderDto;
import com.example.untoldpsproject.dtos.OrderDtoIds;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.services.OrderService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
/**
 * Controller class for managing order operations.
 */
@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/order")
public class OrderController {
    private final OrderService orderService;

    /**
     * Inserts a new order into the system.
     *
     * @param orderDto The DTO representing the order to be inserted.
     * @return ResponseEntity containing the ID of the inserted order.
     */
    @PostMapping("/insert")
    public ResponseEntity<UUID> insertOrder(@RequestBody OrderDto orderDto){
        UUID orderId = orderService.insert(orderDto);
        return new ResponseEntity<>(orderId, HttpStatus.CREATED);
    }

    /**
     * Retrieves all orders from the system.
     *
     * @return ResponseEntity containing a list of order DTOs.
     */
    @GetMapping("/getAllOrders")
    public ResponseEntity<List<OrderDtoIds>> getOrder(){
        List<OrderDtoIds> dtos = orderService.findOrders();
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }

    /**
     * Retrieves an order by its ID from the system.
     *
     * @param orderId The ID of the order to retrieve.
     * @return ResponseEntity containing the order DTO.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDtoIds> getOrderById(@PathVariable("id") UUID orderId){
        OrderDtoIds dto = orderService.findOrderById(orderId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Updates an order in the system by its id.
     *
     * @param orderId   The ID of the order to update.
     * @param orderDto  The DTO representing the updated order.
     * @return ResponseEntity containing the updated order entity.
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Order> updateOrderById(@PathVariable("id") UUID orderId, @RequestBody OrderDto orderDto){
        Order order = orderService.updateOrderById(orderId,orderDto);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * Deletes an order from the system by its ID.
     *
     * @param orderId The ID of the order to delete.
     * @return ResponseEntity indicating the success of the operation.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteOrderById(@PathVariable("id") UUID orderId){
        orderService.deleteOrderById(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
