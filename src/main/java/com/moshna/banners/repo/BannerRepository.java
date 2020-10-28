package com.moshna.banners.repo;

import com.moshna.banners.model.Banner;
import org.springframework.data.repository.CrudRepository;

public interface BannerRepository extends CrudRepository<Banner, Long> {
}
