package com.moshna.banners.controller;

import com.moshna.banners.model.Banner;
import com.moshna.banners.model.Category;
import com.moshna.banners.model.Request;
import com.moshna.banners.repo.BannerRepository;
import com.moshna.banners.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @GetMapping("/category/{req_name}")
    public String getBannerText(@PathVariable(value = "req_name") String req_name, Model model,
                                HttpServletResponse response) {
        //TODO: реализация пункта получения текста баннера по URL определенного вида
        List<Category> categories = GetNotDeletedCategories();
        List<Banner> banners = GetNotDeletedBanner();
        List<Banner> bannersWithCatID = new ArrayList<>();
        for (Category c: categories) {
            if(c.getReq_name() == req_name) {
                for (Banner b: banners) {
                    if(b.getCategoryID() == c.getId()){
                        bannersWithCatID.add(b);
                    }
                }
            }
        }

        //ищем с максимальным прайсом
        if(!bannersWithCatID.isEmpty()) {
            Banner bannerWithMaxPrice = bannersWithCatID.get(0);
            for (Banner b : bannersWithCatID) {
                if (b.getPrice() >= bannerWithMaxPrice.getPrice()) {
                    bannerWithMaxPrice = b;
                }
            }

            //TODO: вывод текста на экран




            //TODO:проверка ip и даты
            String ip_address = "";
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());


            Request request = new Request(bannerWithMaxPrice.getId(), bannerWithMaxPrice.getText(),
                                            ip_address, date);


        }
        else {
            //TODO: вернуть HTTP status 204
            //return response.setStatus(HttpStatus.NO_CONTENT);
        }


        //TODO: тут же реализация последнего пункта и создание записи в базу REQUEST
        return "redirect:/";
    }



}
