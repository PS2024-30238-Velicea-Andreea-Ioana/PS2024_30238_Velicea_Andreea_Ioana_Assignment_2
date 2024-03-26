package com.example.untoldpsproject.mappers;

import com.example.untoldpsproject.dtos.CategoryDto;
import com.example.untoldpsproject.dtos.CategoryDtoIds;
import com.example.untoldpsproject.entities.Category;
import com.example.untoldpsproject.entities.Ticket;

import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryDtoIds toCategoryDtoIds(Category category){
        return CategoryDtoIds.builder().id(category.getId())
                .tip(category.getTip())
                .tickets(category.getTickets().stream().map(Ticket::getId).collect(Collectors.toList()))
                .startDate(category.getStartDate())
                .finishDate(category.getFinishDate())
                .build();
    }
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
