package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Status;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private String id;
    private User user;
    private String userId;
    private List<Ticket> tickets;
    private Double totalPrice;
    private Status status;
}
