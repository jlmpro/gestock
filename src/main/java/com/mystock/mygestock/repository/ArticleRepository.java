package com.mystock.mygestock.repository;

import com.mystock.mygestock.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article , Long> {

    List<Article> findAllByCategoryId(Long idCategory);

    boolean existsByCodeArticle(String code);

    Optional <Article> findArticleByCodeArticle(String codeArticle);


}
