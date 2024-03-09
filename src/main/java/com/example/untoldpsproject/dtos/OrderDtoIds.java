package com.example.untoldpsproject.dtos;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDtoIds {
    private UUID id;
    private UUID user;
    private Set<UUID> tickets;
    private Double totalPrice;
}
