package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDtoIds {
    private UUID id;
    private UUID user;
    private List<UUID> tickets;
    private Double totalPrice;
}