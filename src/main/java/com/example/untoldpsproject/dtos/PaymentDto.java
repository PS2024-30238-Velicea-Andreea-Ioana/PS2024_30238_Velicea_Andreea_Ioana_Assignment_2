package com.example.untoldpsproject.dtos;

import jakarta.persistence.Column;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {
    private String id;
    private String titularCard;
    private String nrCard;
    private String ccv;
}
