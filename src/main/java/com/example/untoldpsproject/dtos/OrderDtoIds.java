package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Status;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDtoIds {
    private String id;
    private String user;
    private List<String> tickets;
    private Double totalPrice;
    private Status status;
}