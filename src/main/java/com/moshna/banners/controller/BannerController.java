package com.moshna.banners.controller;

import com.moshna.banners.model.Banner;
import com.moshna.banners.model.Category;
import com.moshna.banners.repo.BannerRepository;
import com.moshna.banners.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Controller
public class BannerController {

    @Autowired
    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;

    public BannerController(BannerRepository bannerRepository, CategoryRepository categoryRepository) {
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
    }

    //TODO: сделать методы вытаскивания всех категорий и баннеров

    @GetMapping("/banner")
    public String categoryMain(Model model) {

        Iterable<Banner> banners = bannerRepository.findAll();
        Collection<Banner> bannersList = new ArrayList<>();
        for (Banner b: banners) {
            if(!b.isDeleted()) {
                bannersList.add(new Banner(b.getId(), b.getName(), b.getPrice(), b.getCategoryID(), b.getText(), b.isDeleted()));
            }
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
        if(!bannerRepository.existsById(id)) {
            return "redirect:/";
        }

        Optional<Banner> banner = bannerRepository.findById(id);
        ArrayList<Banner> ban = new ArrayList<>();
        banner.ifPresent(ban::add);


        Iterable<Banner> banners = bannerRepository.findAll();
        Collection<Banner> bannersList = new ArrayList<>();
        for (Banner b: banners) {
                bannersList.add(new Banner(b.getId(), b.getName(), b.getPrice(),
                        b.getCategoryID(), b.getText(), b.isDeleted()));
        }

        //TODO: убрать этот костыль
        Banner bannerDetail = new Banner(ban.get(0).getId(), ban.get(0).getName(), ban.get(0).getPrice(),
                ban.get(0).getCategoryID(), ban.get(0).getText(), ban.get(0).isDeleted());

        Optional<Category> categoryOptList = categoryRepository.findById(bannerDetail.getCategoryID());
        ArrayList<Category> catList = new ArrayList<>();
        categoryOptList.ifPresent(catList::add);
        Category category = new Category(catList.get(0).getId(), catList.get(0).getName(),
                catList.get(0).getReq_name(), catList.get(0).isDeleted());

        Iterable<Category> categories = categoryRepository.findAll();
        Collection<Category> categoryList = new ArrayList<>();
        for (Category item : categories) {
            categoryList.add(new Category(item.getId(), item.getName(), item.getReq_name(), item.isDeleted()));
        }

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
