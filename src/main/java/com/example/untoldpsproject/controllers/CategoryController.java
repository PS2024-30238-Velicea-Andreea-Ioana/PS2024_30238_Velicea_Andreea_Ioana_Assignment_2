package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.constants.CategoryConstants;
import com.example.untoldpsproject.dtos.CategoryDto;
import com.example.untoldpsproject.services.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @GetMapping("/list/{userId}")
    public ModelAndView categoryList(@PathVariable("userId")String userId) {
        ModelAndView mav = new ModelAndView("category-list");
        List<CategoryDto> categories = categoryService.findCategories();
        mav.addObject("categories", categories);
        mav.addObject("userId", userId);
        return mav;
    }

    /**
     * Displays the form for adding a new category.
     *
     * @return A ModelAndView object containing the view name and an empty CategoryDto object.
     */
    @GetMapping("/add/{userId}")
    public ModelAndView addCategoryForm(@PathVariable("userId") String userId) {
        ModelAndView mav = new ModelAndView("category-add");
        mav.addObject("categoryDto", new CategoryDto());
        mav.addObject("userId", userId);
        return mav;
    }

    /**
     * Adds a new category.
     *
     * @param categoryDto The CategoryDto object representing the category to be added.
     * @return A redirection to the category list view.
     */
    @PostMapping("/add/{userId}")
    public ModelAndView addCategory(@PathVariable("userId") String userId, @ModelAttribute CategoryDto categoryDto, RedirectAttributes redirectAttributes) {
        String result = categoryService.insert(categoryDto);
        if(result.equals(CategoryConstants.CATEGORY_INSERTED)){
            return new ModelAndView("redirect:/category/list/"+userId);
        }else{
            redirectAttributes.addFlashAttribute("error" + result);
            return new ModelAndView("redirect:/category/add/"+userId);
        }

    }

    /**
     * Displays the form for editing an existing category.
     *
     * @param categoryId The ID of the category to be edited.
     * @return A ModelAndView object containing the view name and the CategoryDto object to be edited.
     */
    @GetMapping("/edit/{id}/{userId}")
    public ModelAndView editCategoryForm(@PathVariable("userId") String userId, @PathVariable("id") String categoryId) {
        ModelAndView mav = new ModelAndView("category-edit");
        CategoryDto categoryDto = categoryService.findCategoryById(categoryId);
        mav.addObject("categoryDto", categoryDto);
        mav.addObject("userId", userId);
        return mav;
    }

    /**
     * Updates an existing category.
     *
     * @param categoryDto The CategoryDto object representing the updated category information.
     * @return A redirection to the category list view.
     */
    @PostMapping("/edit/{id}/{userId}")
    public ModelAndView updateCategory(@PathVariable("userId") String userId, @ModelAttribute("categoryDto") CategoryDto categoryDto, RedirectAttributes redirectAttributes) {
        String result = categoryService.updateCategoryById(categoryDto.getId(), categoryDto);
        if(result.equals(CategoryConstants.CATEGORY_UPDATED)){
            return new ModelAndView("redirect:/category/list/"+userId);
        }else{
            redirectAttributes.addFlashAttribute("error"+result);
            return new ModelAndView("redirect:/category/edit/"+categoryDto.getId()+"/"+userId);
        }

    }

    /**
     * Deletes a category.
     *
     * @param id The ID of the category to be deleted.
     * @return A redirection to the category list view.
     */
    @GetMapping("/delete/{id}/{userId}")
    public ModelAndView deleteCategory(@PathVariable("userId") String userId,@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        String result = categoryService.deleteCategoryById(id);
        redirectAttributes.addFlashAttribute("error",result);
        return new ModelAndView("redirect:/category/list/"+userId);
    }
}