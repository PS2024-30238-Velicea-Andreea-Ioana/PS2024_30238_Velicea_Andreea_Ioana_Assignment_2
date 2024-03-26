package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Ticket;
import lombok.*;
import java.time.LocalDate;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private String id;
    private String tip;
    private List<Ticket> tickets;
    private LocalDate startDate;
    private LocalDate finishDate;

}
