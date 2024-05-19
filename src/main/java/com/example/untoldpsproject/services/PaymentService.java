package com.example.untoldpsproject.services;

import com.example.untoldpsproject.constants.CategoryConstants;
import com.example.untoldpsproject.constants.OrderConstants;
import com.example.untoldpsproject.constants.PaymentConstants;
import com.example.untoldpsproject.dtos.CategoryDto;
import com.example.untoldpsproject.dtos.PaymentDto;
import com.example.untoldpsproject.entities.Category;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Payment;
import com.example.untoldpsproject.entities.Status;
import com.example.untoldpsproject.mappers.CategoryMapper;
import com.example.untoldpsproject.mappers.PaymentMapper;
import com.example.untoldpsproject.repositories.OrderRepository;
import com.example.untoldpsproject.repositories.PaymentRepository;
import com.example.untoldpsproject.validators.PaymentValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing payments.
 */
@Setter
@Getter
@AllArgsConstructor
@Service
public class PaymentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentValidator paymentValidator = new PaymentValidator();

    /**
     * Inserts a new payment into the database.
     *
     * @param paymentDto The payment DTO containing information about the payment.
     * @return A message indicating the success or failure of the insertion.
     */
    public String insert(PaymentDto paymentDto){
        try{
            paymentValidator.paymentDtoValidator(paymentDto);
            Payment payment = PaymentMapper.toPayment(paymentDto);
            paymentRepository.save(payment);
            LOGGER.debug(PaymentConstants.PAYMENT_INSERTED);
            return PaymentConstants.PAYMENT_INSERTED;
        }catch (Exception e){
            LOGGER.error(PaymentConstants.PAYMENT_NOT_INSERTED + e.getMessage());
            return PaymentConstants.PAYMENT_NOT_INSERTED + " "+e.getMessage();
        }
    }

    /**
     * Updates the status of an order to "PAID" after successful payment.
     *
     * @param orderId The ID of the order to update.
     * @return A message indicating the success or failure of the update.
     */
    public String actualizeOrderStatus(String orderId){
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if(orderOptional.isEmpty()){
            LOGGER.error(OrderConstants.ORDER_NOT_FOUND);
            return OrderConstants.ORDER_NOT_FOUND;
        }else{
            Order order = orderOptional.get();
            order.setStatus(Status.PAID);
            orderRepository.save(order);
            LOGGER.debug(OrderConstants.ORDER_UPDATED);
            return OrderConstants.ORDER_UPDATED;
        }
    }
}