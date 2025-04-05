package com.mystock.mygestock.gestionphoto.strategy;

import com.flickr4java.flickr.FlickrException;
import com.mystock.mygestock.dto.ArticleDto;
import com.mystock.mygestock.exception.ErrorCodes;
import com.mystock.mygestock.exception.InvalidOperationException;
import com.mystock.mygestock.gestionphoto.flickr.FlickrService;
import com.mystock.mygestock.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
@Service("articleStrategy")
@Slf4j
public class SaveArticlePhoto implements Strategy<ArticleDto>{

    private FlickrService flickrService;
    private ArticleService articleService;
    @Autowired
    public SaveArticlePhoto(FlickrService flickrService, ArticleService articleService) {
        this.flickrService = flickrService;
        this.articleService = articleService;
    }

    @Override
    public ArticleDto savePhoto(Long id ,InputStream photo, String titre) throws FlickrException {

        ArticleDto article = articleService.findById(id);
        String urlPhoto = flickrService.savePhoto(photo, titre);
        if (!StringUtils.hasLength(urlPhoto)){
            throw new InvalidOperationException(
                    "Erreur lors de l'enregistrement de la photo de l'article",
                    ErrorCodes.UPDATE_PHOTO_EXCEPTION);
        }
        article.setPhoto(urlPhoto);

        return articleService.save(article);
    }
}
