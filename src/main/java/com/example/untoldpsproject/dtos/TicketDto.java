package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.CartItem;
import com.example.untoldpsproject.entities.Category;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Sale;
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
    private Double discountedPrice;
    private int available;
    private List<Order> orders;
    private List<CartItem> cartItem;
    private Sale sale;
    private String imageUrl;
}
