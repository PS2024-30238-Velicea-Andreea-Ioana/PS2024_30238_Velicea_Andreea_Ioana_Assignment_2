package com.example.untoldpsproject.dtos;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDtoIds {
    private UUID id;
    private String tip;
    private List<UUID> tickets;
}
