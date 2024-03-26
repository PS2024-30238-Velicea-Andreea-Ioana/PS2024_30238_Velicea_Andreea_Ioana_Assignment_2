package com.example.untoldpsproject.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDtoIds {
    private String id;
    private String tip;
    private List<String> tickets;
    private LocalDate startDate;
    private LocalDate finishDate;
}
