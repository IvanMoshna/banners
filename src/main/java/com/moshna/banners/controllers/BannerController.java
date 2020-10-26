package com.moshna.banners.controllers;

import com.moshna.banners.models.Banner;
import com.moshna.banners.models.Category;
import com.moshna.banners.repo.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BannerController {

    @Autowired
    private BannerRepository bannerRepository;

    @GetMapping("/banner")
    public String categoryMain(Model model) {
        model.addAttribute("banner", "Banners");
        return "banner-main";
    }

    @PostMapping("/banner")
    public String bannerPostAdd(@RequestParam String name, @RequestParam double price, @RequestParam Long categoryID,
                                @RequestParam String text, Model model) {
        Banner banner = new Banner(name, price, categoryID, text);
        bannerRepository.save(banner);
        return "redirect:/";
    }

}
