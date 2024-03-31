package com.example.untoldpsproject.validators;

import com.example.untoldpsproject.constants.CategoryConstants;
import com.example.untoldpsproject.dtos.CartDto;
import com.example.untoldpsproject.dtos.CartDtoIds;
import com.example.untoldpsproject.dtos.CategoryDto;
import com.example.untoldpsproject.dtos.CategoryDtoIds;

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
        if (startDate == null || finishDate == null || startDate.isBefore(today) || finishDate.isBefore(startDate) ){
            throw new Exception(CategoryConstants.INVALID_DATE);
        }
        return true;
    }

    public boolean categoryDtoValidator(CategoryDto categoryDto) throws Exception {
        if(validateTip(categoryDto.getTip()) || validateDates(categoryDto.getStartDate(),categoryDto.getFinishDate())){
            return false;
        }else{
            return true;
        }
    }
}
