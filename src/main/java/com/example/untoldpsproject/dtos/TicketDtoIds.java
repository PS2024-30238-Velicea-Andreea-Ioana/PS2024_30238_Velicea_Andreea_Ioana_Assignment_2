package com.example.untoldpsproject.dtos;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDtoIds {
    private UUID id;
    private String type;
    private Double price;
    private int quantity;
    private Set<UUID> orders;
}
