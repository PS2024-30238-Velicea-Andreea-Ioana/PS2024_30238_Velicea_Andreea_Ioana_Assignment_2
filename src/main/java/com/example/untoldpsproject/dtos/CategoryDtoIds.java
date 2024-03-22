package com.example.untoldpsproject.dtos;

import lombok.*;

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
}
