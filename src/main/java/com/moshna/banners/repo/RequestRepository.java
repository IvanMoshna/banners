package com.moshna.banners.repo;

import com.moshna.banners.model.Request;
import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Long> {
}
