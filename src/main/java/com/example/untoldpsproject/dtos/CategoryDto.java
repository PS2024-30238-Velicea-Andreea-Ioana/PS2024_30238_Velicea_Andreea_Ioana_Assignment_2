package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Ticket;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private UUID id;
    private String tip;
    private List<Ticket> tickets;
}
