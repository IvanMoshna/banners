package com.moshna.banners.controller;

import com.moshna.banners.model.Banner;
import com.moshna.banners.model.Category;
import com.moshna.banners.repo.BannerRepository;
import com.moshna.banners.repo.CategoryRepository;
import com.moshna.banners.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import javax.validation.Valid;

@Controller
public class BannerController {

    @Autowired
    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;

    public BannerController(BannerRepository bannerRepository, CategoryRepository categoryRepository) {
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

    @GetMapping("/home")
    public String goHome()
    {
        return "home";
    }

    @GetMapping("/banner")
    public String categoryMain(Model model) {

        List<Banner> bannersList = GetNotDeletedBanner();
        List<Category> categoryList = GetNotDeletedCategories();
        String abc = "";
        model.addAttribute("categories", categoryList);
        model.addAttribute("banners", bannersList);
        model.addAttribute("test" , abc);
        return "banner-main";
    }

    @PostMapping("/banner")
    public String bannerPostAdd(@Valid Banner banner,
                                BindingResult bindingResult,
                                Model model) {
        String message ="";

        try {
            bannerRepository.save(banner);
        } catch (Exception e) {
            //TODO:вывести ошибку
            message = "validation error";

        }

        model.addAttribute("validationMessage", message);
        return "redirect:/banner";
    }

    @GetMapping("/banner/{id}")
    public String bannerDetails(@PathVariable(value = "id") long id, Model model) {
        if(!bannerRepository.existsById(id)) {
            return "redirect:/";
        }

        Banner bannerDetail = bannerRepository.findById(id).orElseThrow();
        Category category = categoryRepository.findById(bannerDetail.getCategoryID()).orElseThrow();

        List<Banner> bannersList = GetNotDeletedBanner();
        List<Category> categoryList = GetNotDeletedCategories();

        model.addAttribute("categories", categoryList);
        model.addAttribute("categorySelected", category);
        model.addAttribute("banners", bannersList);
        model.addAttribute("bannerDetails", bannerDetail);
        return "banner-details";
    }

    @PostMapping("/banner/{id}")
    public String bannerPostUpdate(@PathVariable(value = "id") long id, @RequestParam String name,
                                   @RequestParam double price, @RequestParam Long categoryID,
                                   @RequestParam String text, Model model) {
        Banner banner = bannerRepository.findById(id).orElseThrow();
        banner.setName(name);
        banner.setPrice(price);
        banner.setCategoryID(categoryID);
        banner.setText(text);

        bannerRepository.save(banner);

        return "redirect:/banner";
    }

    @PostMapping("/banner/{id}/remove")
    public String bannerPostDelete(@PathVariable(value = "id") long id, Model model) {
        Banner banner = bannerRepository.findById(id).orElseThrow();
        banner.setDeleted(true);
        bannerRepository.save(banner);
        return "redirect:/banner";
    }
}
