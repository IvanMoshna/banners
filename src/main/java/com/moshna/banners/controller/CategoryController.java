package com.moshna.banners.controller;

import com.moshna.banners.model.Banner;
import com.moshna.banners.model.Category;
import com.moshna.banners.repo.BannerRepository;
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
import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    private BannerRepository bannerRepository;

    public CategoryController(CategoryRepository categoryRepository, BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
    }

    //TODO: возможно их лучше куда нибудь переместить?
    public List<Banner> GetAllBanners() {
        Iterable<Banner> banners = bannerRepository.findAll();
        List<Banner> bannersList = new ArrayList<>();
        for (Banner b : banners) {
            bannersList.add(new Banner(b.getId(), b.getName(), b.getPrice(),
                    b.getCategoryID(), b.getText(), b.isDeleted()));
        }
        return bannersList;
    }
    public List<Banner> GetNotDeletedBanner() {
        Iterable<Banner> banners = bannerRepository.findAll();
        List<Banner> bannersList = new ArrayList<>();
        for (Banner b : banners) {
            if(!b.isDeleted()) {
                bannersList.add(new Banner(b.getId(), b.getName(), b.getPrice(),
                        b.getCategoryID(), b.getText(), b.isDeleted()));
            }
        }
        return bannersList;
    }
    public List<Category> GetAllCategories() {
        Iterable<Category> categories = categoryRepository.findAll();
        List<Category> categoriesList = new ArrayList<>();
        for (Category b : categories) {
            categoriesList.add(new Category(b.getId(), b.getName(), b.getReq_name(), b.isDeleted()));
        }
        return categoriesList;
    }
    public List<Category> GetNotDeletedCategories() {
        Iterable<Category> categories = categoryRepository.findAll();
        List<Category> categoriesList = new ArrayList<>();
        for (Category b : categories) {
            if(!b.isDeleted()) {
                categoriesList.add(new Category(b.getId(), b.getName(), b.getReq_name(), b.isDeleted()));
            }
        }
        return categoriesList;
    }

    @GetMapping("/category")
    public String categoryMain(Model model) {
        List<Category> categoryList = GetNotDeletedCategories();
        model.addAttribute("categories", categoryList);
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
        Category category = categoryRepository.findById(id).orElseThrow();
        List<Category> categoryList = GetNotDeletedCategories();

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

    @PostMapping("/category/{id}/remove")
    public String categoryPostRemove(@PathVariable(value = "id") long id, Model model) {
        List<Long> notDeletedBannersID = new ArrayList<>();
        Category category = categoryRepository.findById(id).orElseThrow();


        Iterable<Banner> banners = bannerRepository.findAll();
        Collection<Banner> bannersList = new ArrayList<>();
        for (Banner b: banners) {
            if(b.getCategoryID() == id && !b.isDeleted()) {
                notDeletedBannersID.add(b.getId());
            }
        }

        if(!notDeletedBannersID.isEmpty()) {
            //TODO:ошибка с айдишниками баннеров
        }
        else {
            category.setDeleted(true);
            categoryRepository.save(category);
        }


        model.addAttribute("notDeleted", notDeletedBannersID);
        return "redirect:/category";
    }



}
