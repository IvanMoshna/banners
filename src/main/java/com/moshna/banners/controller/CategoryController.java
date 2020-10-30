package com.moshna.banners.controller;

import com.moshna.banners.model.Banner;
import com.moshna.banners.model.Category;
import com.moshna.banners.model.Request;
import com.moshna.banners.repo.BannerRepository;
import com.moshna.banners.repo.CategoryRepository;
import com.moshna.banners.repo.RequestRepository;
import com.moshna.banners.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class CategoryController {

    private CategoryRepository categoryRepository;
    private BannerRepository bannerRepository;
    private RequestRepository requestRepository;
    private MainService mainService;

    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

    public CategoryController(CategoryRepository categoryRepository,
                              BannerRepository bannerRepository,
                              RequestRepository requestRepository,
                              MainService mainService) {
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.mainService = mainService;
    }

    @GetMapping("/category")
    public String categoryMain(Model model) {
        List<Category> categoryList = mainService.getNotDeletedCategories();
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
    public String categoryDetails(@PathVariable(value = "id") long id, Model model) {
        Category category = categoryRepository.findById(id).orElseThrow();
        List<Category> categoryList = mainService.getNotDeletedCategories();

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

    @GetMapping("/category={req_name}")
    public String getBannerText(@PathVariable(value = "req_name") String req_name,
                                @RequestHeader(value = "User-Agent") String userAgent,
                                HttpServletRequest requestIP,
                                Model model,
                                HttpServletResponse response) {

        List<Category> categories = mainService.getNotDeletedCategories();
        List<Banner> banners = mainService.getNotDeletedBanner();
        List<Request> requests = mainService.getAllRequest();
        List<Banner> bannersWithCatID = new ArrayList<>();
        String soughtBannerText = "";


        for (Category c: categories) {
            String nameReq = c.getReq_name();
            if(c.getReq_name().equals(req_name)) {
                for (Banner b: banners) {
                    if(b.getCategoryID() == c.getId()){
                        bannersWithCatID.add(b);//баннеры с выбранной категорией
                    }
                }
            }
        }

        //ищем с максимальным прайсом
        /*if(!bannersWithCatID.isEmpty()) {

            Banner bannerWithMaxPrice = getBannerWithMaxPrice(bannersWithCatID);
            if(!requests.isEmpty()) {
                for (Request r: requests) {
                    Date dateNow = new Date(System.currentTimeMillis());
                    Long milliseconds = r.getDateTime().getTime() - dateNow.getTime();
                    int days = (int) (milliseconds / (24 * 60 * 60 * 1000));
                    if(r.getBanner_Id() == bannerWithMaxPrice.getId() &&
                    r.getUser_agent().equals(userAgent) &&
                    r.getIp_address() == requestIP.getRemoteAddr() &&
                    days>=1) {
                        bannersWithCatID.remove(bannerWithMaxPrice);

                    }
                }
            }*/
        if(check(bannersWithCatID, requests, userAgent, requestIP) && !bannersWithCatID.isEmpty()) {

            Banner bannerWithMaxPrice = getBannerWithMaxPrice(bannersWithCatID);
            soughtBannerText = bannerWithMaxPrice.getText();

            Date date = new Date(System.currentTimeMillis());
            Request request = new Request(bannerWithMaxPrice.getId(), userAgent,
                                            requestIP.getRemoteAddr(), date);
            requestRepository.save(request);

            model.addAttribute("bannerText", soughtBannerText);
            return "bannerText";
        }
        else {
            response.setStatus(204);
            return "category-main";
        }


    }

    public Banner getBannerWithMaxPrice(List<Banner> bannersWithCatID)
    {
        Banner bannerWithMaxPrice = bannersWithCatID.get(0);
        for (Banner b : bannersWithCatID) {
            if (b.getPrice() >= bannerWithMaxPrice.getPrice()) {
                bannerWithMaxPrice = b;
            }
        }
        return bannerWithMaxPrice;
    }

    public boolean check(List<Banner> bannersWithCatID, List<Request> requests,
                         String userAgent, HttpServletRequest requestIP ) {
        //boolean isOk;
        if (!bannersWithCatID.isEmpty()) {


            if (!requests.isEmpty()) {
                for (Request r : requests) {
                    Banner bannerWithMaxPrice = getBannerWithMaxPrice(bannersWithCatID);
                    Date dateNow = new Date(System.currentTimeMillis());
                    Long milliseconds = dateNow.getTime() - r.getDateTime().getTime();
                    int days = (int) (milliseconds / (24 * 60 * 60 * 1000));
                    if (r.getBanner_Id() == bannerWithMaxPrice.getId() &&
                            r.getUser_agent().equals(userAgent) &&
                            r.getIp_address().equals(requestIP.getRemoteAddr()) &&
                            days < 1) {
                        bannersWithCatID.remove(bannerWithMaxPrice);
                        continue;
                    }
                    else {
                        continue;
                    }
                }
            }
            return true;
        }
       else{
           return  false;
        }

    }
}
