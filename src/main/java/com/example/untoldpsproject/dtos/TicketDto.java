package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {
    private UUID id;
    private String type;
    private Double price;
    private int quantity;
    private int available;
    private List<Order> orders;
}
