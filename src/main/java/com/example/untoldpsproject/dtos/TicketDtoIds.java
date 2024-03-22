package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Category;
import lombok.*;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDtoIds {
    private String id;
    private String category;
    private Double price;
    private int quantity;
    private int available;
    private List<String> orders;
}
