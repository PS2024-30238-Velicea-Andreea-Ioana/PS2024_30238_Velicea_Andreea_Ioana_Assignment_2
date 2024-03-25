package com.example.untoldpsproject.services;

import com.example.untoldpsproject.dtos.CategoryDto;
import com.example.untoldpsproject.entities.Category;
import com.example.untoldpsproject.mappers.CategoryMapper;
import com.example.untoldpsproject.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
@AllArgsConstructor
@Service
public class CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final CategoryRepository categoryRepository;

    /**
     * Inserts a new category into the database.
     * @param categoryDto The user DTO containing user information.
     * @return The ID of the inserted category.
     */

    public String insert(CategoryDto categoryDto){
        Category category = CategoryMapper.toCategory(categoryDto);
        category = categoryRepository.save(category);
        LOGGER.debug("Category with id {} was inserted in db",category.getId());
        return category.getId();
    }

    /**
     * Retrieves all categories from the database.
     *
     * @return A list of category DTOs containing category information.
     */
    public List<Category> findCategories(){
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList;
    }
    /**
     * Retrieves a category by ID from the database.
     * @param id The ID of the category to retrieve.
     * @return The category DTO containing category information, or null if not found.
     */
    public Category findCategoryById(String id){
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(!categoryOptional.isPresent()){
            LOGGER.error("Category with id {} was not found in db", id);
        }
        return categoryOptional.get();
    }
    /**
     * Updates a category by ID in the database.
     * @param id The ID of the category to update.
     * @param updatedCategoryDto The updated category DTO containing new user information.
     * @return The updated category entity.
     */
    public Category updateCategoryById(String id, CategoryDto updatedCategoryDto){
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(!categoryOptional.isPresent()){
            LOGGER.error("Category with id {} was not found in db", id);
        }else{
            Category category = categoryOptional.get();
            Category updatedCategory = CategoryMapper.toCategory(updatedCategoryDto);
            category.setTip(updatedCategory.getTip());
            category.setTickets(updatedCategory.getTickets());
            categoryRepository.save(category);
            LOGGER.debug("Category with id {} was successfully updated", id);
        }
        return categoryOptional.get();
    }

    /**
     * Deletes a category by ID from the database.
     * @param id The ID of the category to delete.
     */
    public void deleteCategoryById(String id){
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(!categoryOptional.isPresent()){
            LOGGER.error("Category with id {} was not found in db", id);
        }else{
            categoryRepository.delete(categoryOptional.get());
        }
    }
}
