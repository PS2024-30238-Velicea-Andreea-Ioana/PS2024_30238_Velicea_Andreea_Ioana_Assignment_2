package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.CartItem;
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
    private int available;
    private List<String> orders;
    private List<String> cartItem;
    private String sale;
    private String imageUrl;
}
