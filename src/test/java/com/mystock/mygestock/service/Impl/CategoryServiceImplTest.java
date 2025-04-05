package com.mystock.mygestock.service.Impl;

import com.mystock.mygestock.dto.CategoryDto;
import com.mystock.mygestock.exception.EntityNotFoundException;
import com.mystock.mygestock.exception.ErrorCodes;
import com.mystock.mygestock.exception.InvalidEntityException;
import com.mystock.mygestock.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {
    @Autowired
    private CategoryService service;
    @Test
    public void shouldSaveCategoryWithSuccess(){
        CategoryDto expectedCategory = CategoryDto.builder()
                .code("cat test")
                .designation("designation test")
                .build();

        CategoryDto savedCategory = service.save(expectedCategory);

        assertNotNull(savedCategory);
        assertNotNull(savedCategory.getId());
        assertEquals(expectedCategory.getCode(), savedCategory.getCode());
        assertEquals(expectedCategory.getDesignation(), savedCategory.getDesignation());

    }

    @Test
    public void shouldUpdateCategoryWithSuccess(){
        CategoryDto expectedCategory = CategoryDto.builder()
                .code("cat test")
                .designation("designation test")
                .build();

        CategoryDto savedCategory = service.save(expectedCategory);

        CategoryDto categoryToUpdate = savedCategory;
        categoryToUpdate.setCode("Update code");

        savedCategory = service.save(categoryToUpdate);

        assertNotNull(categoryToUpdate);
        assertNotNull(categoryToUpdate.getId());
        assertEquals(categoryToUpdate.getCode(), savedCategory.getCode());
        assertEquals(categoryToUpdate.getDesignation(), savedCategory.getDesignation());

    }
    @Test
    public void findByIdCategoryWithSuccess() {

        EntityNotFoundException expectedException = assertThrows(EntityNotFoundException.class ,
                () ->service.findById(0L) );

        assertEquals(ErrorCodes.CATEGORIE_NOT_FOUND, expectedException.getErrorCodes());
        assertEquals("Aucun categorie avec l'ID = 0 n'a été trouvé dans la base", expectedException.getMessage());
       // assertEquals("Veuiller renseigner le code de la catégorie", expectedException.getErrors().get(0));

    }
    @Test
    public void shouldThrowInvalidEntityException() {
        CategoryDto expectedCategory = CategoryDto.builder()
                .build();

        InvalidEntityException expectedException = assertThrows(InvalidEntityException.class ,
                () ->service.save(expectedCategory) );

        assertEquals(ErrorCodes.CATEGORIE_NOT_VALID, expectedException.getErrorCodes());
        //  assertEquals(1, expectedException.getErrors().size());
        // assertEquals("Veuiller renseigner le code de la catégorie", expectedException.getErrors().get(0));

    }
    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowInvalidEntityException2() {
        service.findById(0L);
    }
}