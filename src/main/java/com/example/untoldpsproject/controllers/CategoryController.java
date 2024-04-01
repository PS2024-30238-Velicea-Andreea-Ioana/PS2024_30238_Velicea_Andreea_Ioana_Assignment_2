package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.CategoryDto;
import com.example.untoldpsproject.services.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

/**
 * Controller class for managing category operations.
 */
@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/category")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Retrieves a list of categories and displays them.
     *
     * @return A ModelAndView object containing the view name and the list of categories.
     */
    @GetMapping("/list")
    public ModelAndView categoryList() {
        ModelAndView mav = new ModelAndView("category-list");
        List<CategoryDto> categories = categoryService.findCategories();
        mav.addObject("categories", categories);
        return mav;
    }

    /**
     * Displays the form for adding a new category.
     *
     * @return A ModelAndView object containing the view name and an empty CategoryDto object.
     */
    @GetMapping("/add")
    public ModelAndView addCategoryForm() {
        ModelAndView mav = new ModelAndView("category-add");
        mav.addObject("categoryDto", new CategoryDto());
        return mav;
    }

    /**
     * Adds a new category.
     *
     * @param categoryDto The CategoryDto object representing the category to be added.
     * @return A redirection to the category list view.
     */
    @PostMapping("/add")
    public ModelAndView addCategory(@ModelAttribute CategoryDto categoryDto) {
        categoryService.insert(categoryDto);
        return new ModelAndView("redirect:/category/list");
    }

    /**
     * Displays the form for editing an existing category.
     *
     * @param categoryId The ID of the category to be edited.
     * @return A ModelAndView object containing the view name and the CategoryDto object to be edited.
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editCategoryForm(@PathVariable("id") String categoryId) {
        ModelAndView mav = new ModelAndView("category-edit");
        CategoryDto categoryDto = categoryService.findCategoryById(categoryId);
        mav.addObject("categoryDto", categoryDto);
        return mav;
    }

    /**
     * Updates an existing category.
     *
     * @param categoryDto The CategoryDto object representing the updated category information.
     * @return A redirection to the category list view.
     */
    @PostMapping("/edit/{id}")
    public ModelAndView updateCategory( @ModelAttribute("categoryDto") CategoryDto categoryDto) {
        categoryService.updateCategoryById(categoryDto.getId(), categoryDto);
        return new ModelAndView("redirect:/category/list");
    }

    /**
     * Deletes a category.
     *
     * @param id The ID of the category to be deleted.
     * @return A redirection to the category list view.
     */
    @GetMapping("/delete/{id}")
    public ModelAndView deleteCategory(@PathVariable("id") String id) {
        categoryService.deleteCategoryById(id);
        return new ModelAndView("redirect:/category/list");
    }
}