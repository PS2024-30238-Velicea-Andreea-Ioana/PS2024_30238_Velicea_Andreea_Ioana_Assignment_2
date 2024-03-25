package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.dtos.CategoryDto;
import com.example.untoldpsproject.dtos.CategoryDtoIds;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.dtos.UserDtoIds;
import com.example.untoldpsproject.entities.Category;
import com.example.untoldpsproject.services.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/category")
public class CategoryController {
    private final CategoryService categoryService;

//    /**
//     * Inserts a new category.
//     *
//     * @param categoryDto The category DTO containing information about the category.
//     * @return The UUID of the inserted category.
//     */
//    @PostMapping("/insert")
//    public ResponseEntity<String> insertCategory(@RequestBody CategoryDto categoryDto){
//        String categoryId = categoryService.insert(categoryDto);
//        return new ResponseEntity<>(categoryId, HttpStatus.CREATED);
//    }
//
//    /**
//     * Retrieves all categories.
//     *
//     * @return A list of category DTOs.
//     */
//    @GetMapping("/getAllCategories")
//    public ResponseEntity<List<CategoryDtoIds>> getCategories(){
//        List<CategoryDtoIds> dtos = categoryService.findCategories();
//        return new ResponseEntity<>(dtos,HttpStatus.OK);
//    }
//
//    /**
//     * Retrieves a category by its ID.
//     *
//     * @param categoryId The ID of the category to retrieve.
//     * @return The category DTO.
//     */
//    @GetMapping(value = "/{id}")
//    public ResponseEntity<CategoryDtoIds> getUserById(@PathVariable("id") String categoryId){
//        CategoryDtoIds dto = categoryService.findCategoryById(categoryId);
//        return new ResponseEntity<>(dto, HttpStatus.OK);
//    }
//
//    /**
//     * Updates a category by its ID.
//     *
//     * @param categoryId The ID of the category to update.
//     * @param categoryDto The updated category DTO.
//     * @return The updated category entity.
//     */
//    @PutMapping(value = "/{id}")
//    public ResponseEntity<Category> updateUserById(@PathVariable("id") String categoryId, @RequestBody CategoryDto categoryDto){
//        Category category = categoryService.updateCategoryById(categoryId,categoryDto);
//        return new ResponseEntity<>(category, HttpStatus.OK);
//    }
//
//    /**
//     * Deletes a category by its ID.
//     *
//     * @param categoryId The ID of the category to delete.
//     * @return HttpStatus indicating the success of the operation.
//     */
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<UUID> deleteCategoryById(@PathVariable("id") String categoryId){
//        categoryService.deleteCategoryById(categoryId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
    @GetMapping("/list")
    public ModelAndView categoryList() {
        ModelAndView mav = new ModelAndView("category-list");
        List<CategoryDtoIds> categories = categoryService.findCategories();
        mav.addObject("categories", categories);
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editCategoryForm(@PathVariable("id") String categoryId) {
        ModelAndView mav = new ModelAndView("category-edit");
        Category category = categoryService.findCategoryById(categoryId);
        mav.addObject("categoryDto", category);
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
}
