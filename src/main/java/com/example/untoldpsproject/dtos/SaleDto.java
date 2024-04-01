package com.example.untoldpsproject.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDto {
    private String id;
    private double discountPercentage;
    private double discountedPrice;
}
