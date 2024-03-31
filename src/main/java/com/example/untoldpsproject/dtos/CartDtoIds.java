package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.CartItem;
import com.example.untoldpsproject.entities.User;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDtoIds {
    private String id;
    private String user;
    private List<String> cartItems;
    private Double totalPrice;
}
