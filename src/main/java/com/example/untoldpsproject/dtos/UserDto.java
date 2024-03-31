package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Cart;
import com.example.untoldpsproject.entities.CartItem;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Role;
import lombok.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Order> orders;
    private Role role;
    private Cart cart;
}