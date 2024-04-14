package com.example.untoldpsproject.validators;

import com.example.untoldpsproject.constants.PaymentConstants;
import com.example.untoldpsproject.constants.UserConstants;
import com.example.untoldpsproject.dtos.PaymentDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentValidator {
    public boolean titularValidator(String cardTitular) throws Exception {
        if(cardTitular == null){
            throw new Exception(PaymentConstants.INVALID_TITULAR);
        }
        Pattern pattern = Pattern.compile("^[A-Z]+ [A-Z]+[- ]?[A-Z]*$");
        Matcher matcher = pattern.matcher(cardTitular);
        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            matcher.reset();
            throw new Exception(PaymentConstants.INVALID_TITULAR);
        }
    }
    public boolean cardNumberValidator(String cardNumber) throws Exception {
        if(cardNumber == null){
            throw new Exception(PaymentConstants.INVALID_CARD_NUMBER);
        }
        Pattern pattern = Pattern.compile("^[0-9]{16}$");
        Matcher matcher = pattern.matcher(cardNumber);
        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            matcher.reset();
            throw new Exception(PaymentConstants.INVALID_CARD_NUMBER);
        }
    }

    public boolean ccvValidator(String ccv) throws Exception{
        if(ccv == null){
            throw new Exception(PaymentConstants.INVALID_CCV);
        }
        Pattern pattern = Pattern.compile("^[0-9]{3}$");
        Matcher matcher = pattern.matcher(ccv);
        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            matcher.reset();
            throw new Exception(PaymentConstants.INVALID_CCV);
        }
    }

    public boolean paymentDtoValidator(PaymentDto paymentDto) throws Exception{
        return titularValidator(paymentDto.getTitularCard()) && cardNumberValidator(paymentDto.getNrCard())
                && ccvValidator(paymentDto.getCcv());
    }
}
