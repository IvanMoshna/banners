package com.moshna.banners.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BannerController {

    @GetMapping("/banner")
    public String categoryMain(Model model) {
        model.addAttribute("banner", "Banners");
        return "banner-main";
    }
}
