package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.CategoryDto;
import com.example.untoldpsproject.services.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/category")
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/list")
    public ModelAndView categoryList() {
        ModelAndView mav = new ModelAndView("category-list");
        List<CategoryDto> categories = categoryService.findCategories();
        mav.addObject("categories", categories);
        return mav;
    }
    @GetMapping("/add")
    public ModelAndView addCategoryForm() {
        ModelAndView mav = new ModelAndView("category-add");
        mav.addObject("categoryDto", new CategoryDto());
        return mav;
    }
    @PostMapping("/add")
    public ModelAndView addCategory(@ModelAttribute CategoryDto categoryDto) {
        categoryService.insert(categoryDto);
        return new ModelAndView("redirect:/category/list");
    }
    @GetMapping("/edit/{id}")
    public ModelAndView editCategoryForm(@PathVariable("id") String categoryId) {
        ModelAndView mav = new ModelAndView("category-edit");
        CategoryDto categoryDto = categoryService.findCategoryById(categoryId);
        mav.addObject("categoryDto", categoryDto);
        return mav;
    }
    @PostMapping("/edit/{id}")
    public ModelAndView updateCategory( @ModelAttribute("categoryDto") CategoryDto categoryDto) {
        categoryService.updateCategoryById(categoryDto.getId(), categoryDto);
        return new ModelAndView("redirect:/category/list");
    }
    @GetMapping("/delete/{id}")
    public ModelAndView deleteCategory(@PathVariable("id") String id) {
        categoryService.deleteCategoryById(id);
        return new ModelAndView("redirect:/category/list");
    }


}
