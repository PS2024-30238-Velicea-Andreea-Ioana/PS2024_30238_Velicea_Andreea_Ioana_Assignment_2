package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Category;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDtoIds {
    private UUID id;
    private UUID category;
    private Double price;
    private int quantity;
    private int available;
    private List<UUID> orders;
}
