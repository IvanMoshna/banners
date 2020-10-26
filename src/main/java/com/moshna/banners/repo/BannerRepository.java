package com.moshna.banners.repo;

import com.moshna.banners.models.Banner;
import org.springframework.data.repository.CrudRepository;

public interface BannerRepository extends CrudRepository<Banner, Long> {
}
