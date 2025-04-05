package com.mystock.mygestock.validator;


import com.mystock.mygestock.dto.UtilisateurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UtilisateurValidator {
    public static List<String> validate(UtilisateurDto utilisateurDto){
        List<String> errors = new ArrayList<>();
        if (utilisateurDto == null) {
            errors.add("Veuiller renseigner le nom de l'utilisateur");
            errors.add("Veuiller renseigner le prénom de l'utilisateur");
            errors.add("Veuiller renseigner le mot de passe de l'utilisateur");
            errors.add("Veuiller renseigner l'adresse de l'utilisateur");
            errors.addAll(AdresseValidator.validate(null));

            return errors;
        }
        if (!StringUtils.hasLength(utilisateurDto.getLastname())){
            errors.add("Veuiller renseigner le nom de l'utilisateur");
        }
        if (!StringUtils.hasLength(utilisateurDto.getFirstname())){
            errors.add("Veuiller renseigner le prénom de l'utilisateur");
        }
        if (!StringUtils.hasLength(utilisateurDto.getEmail())){
            errors.add("Veuiller renseigner l'email de l'utilisateur");
        }
        if (!StringUtils.hasLength(utilisateurDto.getPassword())){
            errors.add("Veuiller renseigner le mot de passe de l'utilisateur");
        }
        if (utilisateurDto.getDateDeNaissance() == null) {
            errors.add("Veuiller renseigner la date de naissance de l'utilisateur");
        }
        if (utilisateurDto.getAdresse() == null) {
            errors.add("Veuiller renseigner l'adresse de l'utilisateur");
        }
        errors.addAll(AdresseValidator.validate(utilisateurDto.getAdresse()));

        return errors;
        }

    }






