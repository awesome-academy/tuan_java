package com.tuanhv.tripgoapi.repository;

import com.tuanhv.tripgoapi.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    List<Category> findAllByOrderByNameAsc();
}
