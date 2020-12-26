package com.moshna.banners.service;

import com.moshna.banners.model.Banner;
import com.moshna.banners.model.Category;
import com.moshna.banners.model.Request;
import com.moshna.banners.repo.BannerRepository;
import com.moshna.banners.repo.CategoryRepository;
import com.moshna.banners.repo.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MainService {

    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    public MainService(BannerRepository bannerRepository,
                       CategoryRepository categoryRepository,
                       RequestRepository requestRepository) {
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
    }

    public List<Banner> getNotDeletedBanner() {
        try {
            Iterable<Banner> banners = bannerRepository.findAll();
            List<Banner> bannersList = new ArrayList<>();
            banners.forEach(banner -> {
                if(!banner.isDeleted()) {
                    bannersList.add(banner);
                }
            });
            return bannersList;
        }
        catch (Exception e)
        {
            return Collections.emptyList();
        }
    }

    public List<Category> getNotDeletedCategories() {
        try {
            Iterable<Category> categories = categoryRepository.findAll();
            List<Category> categoriesList = new ArrayList<>();
            categories.forEach(category -> {
                if(!category.isDeleted()) {
                    categoriesList.add(category);
                }
            });
            return categoriesList;
        }
        catch (Exception e)
        {
            return Collections.emptyList();
        }
    }

    public List<Request> getAllRequest() {
        Iterable<Request> requests = requestRepository.findAll();
        List<Request> requestsList = new ArrayList<>();
        for (Request r : requests) {
            requestsList.add(new Request(r.getBanner_Id(), r.getUser_agent(),
                    r.getIp_address(), r.getDateTime()));
        }
        return requestsList;
    }
}
