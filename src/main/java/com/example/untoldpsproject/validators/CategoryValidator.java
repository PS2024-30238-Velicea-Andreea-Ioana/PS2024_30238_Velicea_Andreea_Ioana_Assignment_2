package com.example.untoldpsproject.validators;

import com.example.untoldpsproject.constants.CategoryConstants;
import com.example.untoldpsproject.dtos.CategoryDto;

import java.time.LocalDate;

public class CategoryValidator {
    public boolean validateTip(String tip) throws Exception {
        if (tip == null || tip.isEmpty()) {
            throw new Exception(CategoryConstants.INVALID_TYPE);
        }
        return true;
    }

    public boolean validateDates(LocalDate startDate, LocalDate finishDate) throws Exception {
        LocalDate today = LocalDate.now();
        System.out.println("STARTdate " + startDate);
        System.out.println("finishDate " + finishDate);
        System.out.println("today" + today);
        System.out.println(startDate.isAfter(today));
        if (startDate == null || finishDate == null || startDate.isAfter(finishDate) ){
            throw new Exception(CategoryConstants.INVALID_DATE);
        }
        return true;
    }

    public boolean categoryDtoValidator(CategoryDto categoryDto) throws Exception {
       if( (!validateTip(categoryDto.getTip()) && !validateDates(categoryDto.getStartDate(), categoryDto.getFinishDate()))){
           return false;
        }
       return true;
    }
}
