package com.moshna.banners.controller;

import com.moshna.banners.model.Banner;
import com.moshna.banners.model.Category;
import com.moshna.banners.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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

    @GetMapping("/category/{id}")
    public String bannerDetails(@PathVariable(value = "id") long id, Model model) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        categoryOptional.ifPresent(categoryArrayList::add);

        //TODO: пофиксить тот же костыль
        Category category = new Category(categoryArrayList.get(0).getId(), categoryArrayList.get(0).getName(),
                categoryArrayList.get(0).getReq_name(), categoryArrayList.get(0).isDeleted());


        Iterable<Category> categories = categoryRepository.findAll();
        Collection<Category> categoryList = new ArrayList<>();
        for (Category item : categories) {
            categoryList.add(new Category(item.getId(), item.getName(), item.getReq_name(), item.isDeleted()));
        }

        model.addAttribute("categories", categoryList);
        model.addAttribute("categoryDetails", category);
        return "category-details";
    }

    @PostMapping("/category/{id}")
    public String categoryPostUpdate(@PathVariable(value = "id") long id, @RequestParam String name,
                                     @RequestParam String req_name, Model model) {
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setName(name);
        category.setReq_name(req_name);

        categoryRepository.save(category);

        return "redirect:/category";
    }

}
