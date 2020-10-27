package com.moshna.banners.controllers;

import com.moshna.banners.models.Banner;
import com.moshna.banners.models.Category;
import com.moshna.banners.repo.BannerRepository;
import com.moshna.banners.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
public class BannerController {

    @Autowired
    private BannerRepository bannerRepository;
    private CategoryRepository categoryRepository;

    public BannerController(BannerRepository bannerRepository, CategoryRepository categoryRepository) {
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/banner")
    public String categoryMain(Model model) {
        //нужно выводить все баннеры
        Iterable<Banner> banners = bannerRepository.findAll();
        Collection<Banner> bannersList = new ArrayList<>();
        for (Banner b: banners) {
            bannersList.add(new Banner(b.getId(), b.getName(), b.getPrice(), b.getCategoryID(), b.getText(), b.isDeleted()));
        }

        Iterable<Category> categories = categoryRepository.findAll();
        Collection<Category> categoryList = new ArrayList<>();
        for (Category item : categories) {
            categoryList.add(new Category(item.getId(), item.getName(), item.getReq_name(), item.isDeleted()));
        }

        model.addAttribute("categories", categoryList);
        model.addAttribute("banners", bannersList);
        return "banner-main";
    }

    @PostMapping("/banner")
    public String bannerPostAdd(@RequestParam String name, @RequestParam double price, @RequestParam Long categoryID,
                                @RequestParam String text, Model model) {

        Banner banner = new Banner(name, price, categoryID , text);
        bannerRepository.save(banner);

        return "redirect:/";
    }

    @GetMapping("/banner/{id}")
    public String bannerDetails(@PathVariable(value = "id") long id, Model model) {
        Optional<Banner> banner = bannerRepository.findById(id);
        ArrayList<Banner> ban = new ArrayList<>();
        banner.ifPresent(ban::add);
        model.addAttribute("banner", ban);
        return "banner-main";
    }

}
