package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.CartItem;
import com.example.untoldpsproject.entities.Category;
import com.example.untoldpsproject.entities.Order;
import lombok.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {
    private String id;
    private Category category;
    private Double price;
    private int available;
    private List<Order> orders;
    private List<CartItem> cartItem;
}
