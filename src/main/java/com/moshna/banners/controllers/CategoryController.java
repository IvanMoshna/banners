package com.moshna.banners.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {

    @GetMapping("/category")
    public String categoryMain(Model model) {
        model.addAttribute("category", "Categories");
        return "category-main";
    }
}
