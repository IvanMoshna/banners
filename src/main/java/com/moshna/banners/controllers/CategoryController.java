package com.moshna.banners.controllers;

import com.moshna.banners.models.Category;
import com.moshna.banners.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping("/category")
    public String categoryMain(Model model) {
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "category-main";
    }

    @PostMapping("/category")
    public String categoryPostAdd(@RequestParam String name, @RequestParam String req_name, Model model) {
        Category category = new Category(name, req_name);
        categoryRepository.save(category);
        return "redirect:/";
    }
}
