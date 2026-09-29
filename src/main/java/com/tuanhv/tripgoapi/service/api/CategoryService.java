package com.tuanhv.tripgoapi.service.api;

import com.tuanhv.tripgoapi.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getCategories();
}
