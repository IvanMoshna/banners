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

    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;
    private final MainService mainService;

    public BannerController(BannerRepository bannerRepository,
                            CategoryRepository categoryRepository,
                            MainService mainService) {
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
        this.mainService = mainService;
    }


    @GetMapping("/home")
    public String goHome()
    {
        return "home";
    }

    @GetMapping("/banner")
    public String categoryMain(Model model) {

        List<Banner> bannersList = mainService.getNotDeletedBanner();
        List<Category> categoryList = mainService.getNotDeletedCategories();
        String abc = "";
        model.addAttribute("categories", categoryList);
        model.addAttribute("banners", bannersList);
        return "banner-main";
    }

    @PostMapping("/banner")
    public String bannerPostAdd(@Valid Banner banner,
                                BindingResult bindingResult,
                                Model model) {
        List<Banner> bannersList = mainService.getNotDeletedBanner();
        List<Category> categoryList = mainService.getNotDeletedCategories();
        String message ="";

        try {
            bannerRepository.save(banner);
        } catch (Exception e) {
            //TODO:вывести ошибку
            message = "validation error";

        }
        model.addAttribute("banners", bannersList);
        model.addAttribute("categories", categoryList);
        model.addAttribute("validationMessage", message);
        return "banner-main";
    }

    @GetMapping("/banner/{id}")
    public String bannerDetails(@PathVariable(value = "id") long id, Model model) {
        if(!bannerRepository.existsById(id)) {
            return "redirect:/";
        }

        Banner bannerDetail = bannerRepository.findById(id).orElseThrow();
        Category category = categoryRepository.findById(bannerDetail.getCategoryID()).orElseThrow();

        List<Banner> bannersList = mainService.getNotDeletedBanner();
        List<Category> categoryList = mainService.getNotDeletedCategories();

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
