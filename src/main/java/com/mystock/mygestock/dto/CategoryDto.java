package com.mystock.mygestock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mystock.mygestock.entity.Category;
import lombok.Builder;
import lombok.Data;


import java.util.List;
@Builder
@Data
public class CategoryDto {
    private Long id ;
    private String code;
    private String designation ;

    @JsonIgnore
    private List<ArticleDto> articles ;

    public static CategoryDto fromEntity(Category category){
        if (category == null){
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .code(category.getCode())
                .designation(category.getDesignation())
                .build();
    }
    public static Category toEntity(CategoryDto categoryDto){
        if (categoryDto == null) {
            return  null;
        }
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setCode(categoryDto.getCode());
        category.setDesignation(categoryDto.getDesignation());
        return category;
    }
}
