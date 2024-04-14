package com.example.untoldpsproject.mappers;

import com.example.untoldpsproject.dtos.PaymentDto;
import com.example.untoldpsproject.entities.Payment;

public class PaymentMapper {
    public static Payment toPayment(PaymentDto paymentDto){
        return Payment.builder()
                .id(paymentDto.getId())
                .titularCard(paymentDto.getTitularCard())
                .nrCard(paymentDto.getNrCard())
                .ccv(paymentDto.getCcv())
                .build();
    }
    public static PaymentDto toPaymentDto(Payment payment){
        return PaymentDto.builder()
                .id(payment.getId())
                .titularCard(payment.getTitularCard())
                .nrCard(payment.getNrCard())
                .ccv(payment.getCcv())
                .build();
    }
}
