package com.mystock.mygestock.validator;


import com.mystock.mygestock.dto.FournisseurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FournisseurValidator {
    public  static List<String> validate(FournisseurDto dto){
        List<String> errors = new ArrayList<>();
        if (dto == null){
            errors.add("Veuiller renseigner le nom fournisseur");
            errors.add("Veuiller renseigner le prenom fournisseur");
            errors.add("Veuiller renseigner le mail fournisseur");
            errors.add("Veuiller renseigner le téléphone fournisseur");
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }
        if (!StringUtils.hasLength(dto.getNom())){
            errors.add("Veuiller renseigner le nom fournisseur");
        }
        if (!StringUtils.hasLength(dto.getPrenom())){
            errors.add("Veuiller renseigner le prenom fournisseur");
        }
        if (!StringUtils.hasLength(dto.getMail())){
            errors.add("Veuiller renseigner le mail fournisseur");
        }
        if (!StringUtils.hasLength(dto.getNumTel())){
            errors.add("Veuiller renseigner le téléphone fournisseur");
        }
        errors.addAll(AdresseValidator.validate(dto.getAdresse()));
        return  errors;
    }
}
