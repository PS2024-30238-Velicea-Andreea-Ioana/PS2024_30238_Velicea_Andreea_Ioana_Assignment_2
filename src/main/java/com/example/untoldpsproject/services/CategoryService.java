package com.example.untoldpsproject.services;

import com.example.untoldpsproject.constants.CategoryConstants;
import com.example.untoldpsproject.dtos.CategoryDto;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.entities.Category;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.mappers.CategoryMapper;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.repositories.CategoryRepository;
import com.example.untoldpsproject.repositories.TicketRepository;
import com.example.untoldpsproject.validators.CategoryValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@Service
public class CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final TicketRepository ticketRepository;
    private final CategoryValidator categoryValidator = new CategoryValidator();
    /**
     * Inserts a new category into the database.
     * @param categoryDto The user DTO containing user information.
     * @return The ID of the inserted category.
     */

    public String insert(CategoryDto categoryDto){
        try{
            categoryValidator.categoryDtoValidator(categoryDto);
            Category category = CategoryMapper.toCategory(categoryDto);
            categoryRepository.save(category);
            LOGGER.debug(CategoryConstants.CATEGORY_INSERTED);
            return CategoryConstants.CATEGORY_INSERTED;
        }catch (Exception e){
            LOGGER.error(CategoryConstants.CATEGORY_NOT_INSERTED + e.getMessage());
            return CategoryConstants.CATEGORY_NOT_INSERTED;
        }
    }

    /**
     * Retrieves all categories from the database.
     *
     * @return A list of category DTOs containing category information.
     */
    public List<CategoryDto> findCategories(){
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }
    /**
     * Retrieves a category by ID from the database.
     * @param id The ID of the category to retrieve.
     * @return The category DTO containing category information, or null if not found.
     */
    public CategoryDto findCategoryById(String id){
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isEmpty()){
            LOGGER.error("Category with id {} was not found in db", id);
            return null;
        }else{
            return CategoryMapper.toCategoryDto(categoryOptional.get());
        }

    }
    public List<TicketDto> findAllWithCategory(String categoryId){
        Category category = categoryRepository.findById(categoryId).get();
        return ticketRepository.findAllByCategory(category).stream()
                .map(TicketMapper::toTicketDto)
                .collect(Collectors.toList());
    }
    /**
     * Updates a category by ID in the database.
     *
     * @param id                 The ID of the category to update.
     * @param updatedCategoryDto The updated category DTO containing new user information.
     */
    public void updateCategoryById(String id, CategoryDto updatedCategoryDto){
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isEmpty()){
            LOGGER.error("Category with id {} was not found in db", id);
        }else{
            Category category = categoryOptional.get();
            try{
                categoryValidator.categoryDtoValidator(updatedCategoryDto);
                Category updatedCategory = CategoryMapper.toCategory(updatedCategoryDto);
                category.setTip(updatedCategory.getTip());
                category.setTickets(updatedCategory.getTickets());
                category.setStartDate(updatedCategory.getStartDate());
                category.setFinishDate(updatedCategory.getFinishDate());
                categoryRepository.save(category);
                LOGGER.debug(CategoryConstants.CATEGORY_UPDATED);
            }catch (Exception e){
                LOGGER.error(CategoryConstants.CATEGORY_NOT_UPDATED);
            }
        }

    }

    /**
     * Deletes a category by ID from the database.
     * @param id The ID of the category to delete.
     */
    public void deleteCategoryById(String id){
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isEmpty()){
            LOGGER.error("Category with id {} was not found in db", id);
        }else{
            categoryRepository.delete(categoryOptional.get());
        }
    }
}
