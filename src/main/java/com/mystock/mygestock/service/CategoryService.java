package com.mystock.mygestock.service;

import com.mystock.mygestock.dto.CategoryDto;
import com.mystock.mygestock.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto save(CategoryDto dto);
    List<CategoryDto> findAll();
    CategoryDto findById(Long id);
    void delete(Long id);
}
