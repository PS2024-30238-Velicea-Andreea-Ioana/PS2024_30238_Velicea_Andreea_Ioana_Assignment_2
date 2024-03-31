package com.example.untoldpsproject.dtos;


import com.example.untoldpsproject.entities.CartItem;
import com.example.untoldpsproject.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private String id;
    private User user;
    private List<CartItem> cartItems;
    private Double totalPrice;
}
