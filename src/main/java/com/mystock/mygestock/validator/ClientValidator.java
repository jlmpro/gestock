package com.mystock.mygestock.validator;


import com.mystock.mygestock.dto.ClientDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ClientValidator {
    public  static List<String> validate(ClientDto dto){
        List<String> errors = new ArrayList<>();
        if (dto == null){
            errors.add("Veuiller renseigner le nom client");
            errors.add("Veuiller renseigner le prenom client");
            errors.add("Veuiller renseigner le mail client");
            errors.add("Veuiller renseigner le téléphone client");
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }
        if (!StringUtils.hasLength(dto.getNom())){
            errors.add("Veuiller renseigner le nom client");
        }
        if (!StringUtils.hasLength(dto.getPrenom())){
            errors.add("Veuiller renseigner le prenom client");
        }
        if (!StringUtils.hasLength(dto.getMail())){
            errors.add("Veuiller renseigner le mail client");
        }
        if (!StringUtils.hasLength(dto.getNumTel())){
            errors.add("Veuiller renseigner le téléphone client");
        }
        errors.addAll(AdresseValidator.validate(dto.getAdresse()));
        return  errors;
    }
}
