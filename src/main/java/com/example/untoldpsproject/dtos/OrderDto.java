package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private UUID id;
    private User user;
    private List<Ticket> tickets;
    private Double totalPrice;
}
