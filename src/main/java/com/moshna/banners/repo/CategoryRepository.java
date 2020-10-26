package com.moshna.banners.repo;

import com.moshna.banners.models.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
