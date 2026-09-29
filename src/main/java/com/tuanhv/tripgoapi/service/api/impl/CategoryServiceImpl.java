package com.tuanhv.tripgoapi.service.api.impl;

import com.tuanhv.tripgoapi.dto.response.CategoryResponse;
import com.tuanhv.tripgoapi.repository.CategoryRepository;
import com.tuanhv.tripgoapi.service.api.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAllByOrderByNameAsc()
                .stream()
                .map(category -> new CategoryResponse(
                        category.getSlug(),
                        category.getName()
                ))
                .toList();
    }
}
