package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Order;
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
}