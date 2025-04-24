package com.mystock.mygestock.service.Impl;

import com.mystock.mygestock.dto.CategoryDto;
import com.mystock.mygestock.exception.EntityNotFoundException;
import com.mystock.mygestock.exception.ErrorCodes;
import com.mystock.mygestock.exception.InvalidEntityException;
import com.mystock.mygestock.exception.InvalidOperationException;
import com.mystock.mygestock.entity.Article;
import com.mystock.mygestock.entity.Category;
import com.mystock.mygestock.repository.ArticleRepository;
import com.mystock.mygestock.repository.CategoryRepository;
import com.mystock.mygestock.service.CategoryService;
import com.mystock.mygestock.validator.CategoryValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ArticleRepository articleRepository) {
        this.categoryRepository = categoryRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public CategoryDto save(CategoryDto dto) {
        List<String> errors = CategoryValidator.validate(dto);
        if (!errors.isEmpty()){
            log.error("Category is not valid !{}", dto);
            throw new InvalidEntityException("Catégorie non valide !!!",ErrorCodes.CATEGORIE_NOT_VALID);
        }
        boolean existCategory = categoryRepository.existsByCode(dto.getCode());
        if (existCategory){
            throw new InvalidEntityException("Ce code catégorie est déjà utilisé !");
        }
        return CategoryDto.fromEntity(
               categoryRepository.save(
                        CategoryDto.toEntity(dto))
        );
    }

    @Override
    public List<CategoryDto> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(Long id) {
        if (id == null){
            log.error("Category ID is null");
            return null;
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun categorie avec l'ID = " + id + " n'a été trouvé dans la base",
                        ErrorCodes.CATEGORIE_NOT_FOUND));
        return CategoryDto.fromEntity(category);
    }

    @Override
    public void delete(Long id) {
        Category categorie = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun categorie avec l'ID = " + id + " n'a été trouvé dans la base",
                        ErrorCodes.CATEGORIE_NOT_FOUND));
        List<Article> articles = articleRepository.findAllByCategoryId(id);
        if (!articles.isEmpty()) {
            throw new InvalidOperationException(
                    "Impossible de supprimer cette catégorie car elle est déjà utilisée dans un article !",ErrorCodes.CATEGORIE_ALREADY_IN_USE);
        }
        categoryRepository.delete(categorie);
    }
}
