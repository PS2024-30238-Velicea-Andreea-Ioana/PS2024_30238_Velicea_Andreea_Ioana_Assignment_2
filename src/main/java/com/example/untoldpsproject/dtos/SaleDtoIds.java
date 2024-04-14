package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Ticket;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDtoIds {
    private String id;
    private Double discountPercentage;
    private List<String> tickets;
}
