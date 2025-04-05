package com.mystock.mygestock.service.Impl;

import com.mystock.mygestock.dto.ArticleDto;
import com.mystock.mygestock.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
 public class ArticleServiceImplTest {
    @Autowired
    ArticleService articleService;

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdWithNullId() {
        ArticleDto articleDto = articleService.findById(null);
    }


}