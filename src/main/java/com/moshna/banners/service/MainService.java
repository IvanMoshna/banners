package com.moshna.banners.service;

import com.moshna.banners.model.Banner;
import com.moshna.banners.model.Category;
import com.moshna.banners.repo.BannerRepository;
import com.moshna.banners.repo.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

public class MainService {

    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;

    public MainService(BannerRepository bannerRepository, CategoryRepository categoryRepository) {
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
    }
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
}
