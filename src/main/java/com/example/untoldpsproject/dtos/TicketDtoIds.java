package com.example.untoldpsproject.dtos;

import lombok.*;

import java.util.List;

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
