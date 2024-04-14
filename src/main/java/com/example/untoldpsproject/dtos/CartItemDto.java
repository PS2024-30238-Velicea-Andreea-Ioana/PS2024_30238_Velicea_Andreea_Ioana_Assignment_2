package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Cart;
import com.example.untoldpsproject.entities.Ticket;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private String id;

    private Ticket ticket;

    private Double quantity;

    private Cart cart;
}
