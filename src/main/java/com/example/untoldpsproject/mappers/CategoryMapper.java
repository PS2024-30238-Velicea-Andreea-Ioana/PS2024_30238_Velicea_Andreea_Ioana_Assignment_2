package com.example.untoldpsproject.mappers;

import com.example.untoldpsproject.dtos.CategoryDto;
import com.example.untoldpsproject.entities.Category;

import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category){
        return CategoryDto.builder().id(category.getId())
                .tip(category.getTip())
                .tickets(category.getTickets())
                .startDate(category.getStartDate())
                .finishDate(category.getFinishDate())
                .build();
    }

    public static Category toCategory(CategoryDto categoryDto){
        return Category.builder().id(categoryDto.getId())
                .tip(categoryDto.getTip())
                .tickets(categoryDto.getTickets())
                .startDate(categoryDto.getStartDate())
                .finishDate(categoryDto.getFinishDate())
                .build();
    }
}
