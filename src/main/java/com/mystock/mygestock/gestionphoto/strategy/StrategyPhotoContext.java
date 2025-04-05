package com.mystock.mygestock.gestionphoto.strategy;

import com.flickr4java.flickr.FlickrException;
import com.mystock.mygestock.exception.ErrorCodes;
import com.mystock.mygestock.exception.InvalidOperationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
@Service
public class StrategyPhotoContext {
    private BeanFactory beanFactory;
    private Strategy strategy;
    private String context;
    @Autowired
    public StrategyPhotoContext(BeanFactory beanFactory) {
        this.beanFactory=beanFactory;
    }

    public Object savePhoto(String context, Long id, InputStream photo,String titre) throws FlickrException{
        determinContext(context);
        return strategy.savePhoto(id,photo,titre);
    }

    private void determinContext(String context){
        final String beanName = context + "Strategy";
        switch (context){
            case "article" :
                strategy = beanFactory.getBean(beanName, SaveArticlePhoto.class);
                break;
            case "client" :
                strategy = beanFactory.getBean(beanName, SaveClientPhoto.class);
                break;
            case "fournisseur" :
                strategy = beanFactory.getBean(beanName, SaveFournisseurPhoto.class);
                break;
            case "utilisateur" :
                strategy = beanFactory.getBean(beanName, SaveUtilisateurPhoto.class);
                break;
            default: throw new InvalidOperationException("Contexte inconnu pour l'enregistrement de la photo", ErrorCodes.UNKNOW_CONTEXT);
        }
    }
}
