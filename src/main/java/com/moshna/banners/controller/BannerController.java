package com.moshna.banners.controller;

import com.moshna.banners.model.Banner;
import com.moshna.banners.model.Category;
import com.moshna.banners.repo.BannerRepository;
import com.moshna.banners.repo.CategoryRepository;
import com.moshna.banners.service.MainService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import javax.validation.Valid;

@Controller
public class BannerController {

    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;
    private final MainService mainService;
    private ExceptionController exceptionController;

    public static final String HOME_PAGE = "home";
    public static final String BANNER_MAIN = "banner-main";
    public static final String BANNER_DETAILS = "banner-details";


    public BannerController(BannerRepository bannerRepository,
                            CategoryRepository categoryRepository,
                            MainService mainService,
                            ExceptionController exceptionController) {
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
        this.mainService = mainService;
        this.exceptionController = exceptionController;
    }


    @GetMapping("/home")
    public String goHome()
    {
        return HOME_PAGE;
    }

    @GetMapping("/banner")
    public String categoryMain(Model model) {

        List<Banner> bannersList = mainService.getNotDeletedBanner();
        List<Category> categoryList = mainService.getNotDeletedCategories();

        model.addAttribute("categories", categoryList);
        model.addAttribute("banners", bannersList);
        return BANNER_MAIN;
    }

    @PostMapping("/banner")
    public String bannerPostAdd(@Valid Banner banner,
                                Model model) {
        List<Banner> bannersList = mainService.getNotDeletedBanner();
        List<Category> categoryList = mainService.getNotDeletedCategories();
        String message ="";

        try {
            bannerRepository.save(banner);
            bannersList = mainService.getNotDeletedBanner();
        } catch (Exception e) {
            //message = "validation error";
            exceptionController.handleArgumentNotValidException(e, model);

        }
        model.addAttribute("banners", bannersList);
        model.addAttribute("categories", categoryList);
        //model.addAttribute("validationMessage", message);
        return BANNER_MAIN;
    }

    @GetMapping("/banner/{id}")
    public String bannerDetails(@PathVariable(value = "id") long id, Model model) {
        if(!bannerRepository.existsById(id)) {
            return HOME_PAGE;
        }

        Banner bannerDetail = bannerRepository.findById(id).orElseThrow();
        Category category = categoryRepository.findById(bannerDetail.getCategoryID()).orElseThrow();

        List<Banner> bannersList = mainService.getNotDeletedBanner();
        List<Category> categoryList = mainService.getNotDeletedCategories();

        model.addAttribute("categories", categoryList);
        model.addAttribute("categorySelected", category);
        model.addAttribute("banners", bannersList);
        model.addAttribute("bannerDetails", bannerDetail);
        return BANNER_DETAILS;
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

        List<Banner> bannersList = mainService.getNotDeletedBanner();
        List<Category> categoryList = mainService.getNotDeletedCategories();
        model.addAttribute("categories", categoryList);
        model.addAttribute("banners", bannersList);
        return BANNER_MAIN;
    }

    @PostMapping("/banner/{id}/remove")
    public String bannerPostDelete(@PathVariable(value = "id") long id, Model model) {
        Banner banner = bannerRepository.findById(id).orElseThrow();
        banner.setDeleted(true);
        bannerRepository.save(banner);
        List<Banner> bannersList = mainService.getNotDeletedBanner();
        List<Category> categoryList = mainService.getNotDeletedCategories();
        model.addAttribute("categories", categoryList);
        model.addAttribute("banners", bannersList);
        return BANNER_MAIN;
    }
}
