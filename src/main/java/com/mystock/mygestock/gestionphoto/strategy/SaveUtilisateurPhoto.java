package com.mystock.mygestock.gestionphoto.strategy;

import com.flickr4java.flickr.FlickrException;
import com.mystock.mygestock.dto.UtilisateurDto;
import com.mystock.mygestock.exception.ErrorCodes;
import com.mystock.mygestock.exception.InvalidOperationException;
import com.mystock.mygestock.gestionphoto.flickr.FlickrService;
import com.mystock.mygestock.service.UtilisateurService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
@Service("utilisateurStrategy")
@Slf4j
public class SaveUtilisateurPhoto implements Strategy<UtilisateurDto>{

    private FlickrService flickrService;
    UtilisateurService utilisateurService;
    @Autowired
    public SaveUtilisateurPhoto(FlickrService flickrService, UtilisateurService utilisateurService) {
        this.flickrService = flickrService;
        this.utilisateurService = utilisateurService;
    }

    @Override
    public UtilisateurDto savePhoto(Long id, InputStream photo, String titre) throws FlickrException {
        UtilisateurDto utilisateur = utilisateurService.findById(id);
        String urlPhoto =flickrService.savePhoto(photo,titre);
        if (!StringUtils.hasLength(urlPhoto)){
            throw new InvalidOperationException(
                    "Erreur lors de l'enregistrement de la photo du utilisateur",
                    ErrorCodes.UPDATE_PHOTO_EXCEPTION);
        }
        utilisateur.setPhoto(urlPhoto);
        return utilisateurService.save(utilisateur);
    }
}
