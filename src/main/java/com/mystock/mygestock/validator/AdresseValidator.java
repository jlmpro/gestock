package com.mystock.mygestock.validator;

import com.mystock.mygestock.dto.AdresseDto;
import com.mystock.mygestock.dto.CategoryDto;
import com.mystock.mygestock.service.Impl.ClientServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AdresseValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdresseValidator.class);

    public static List<String> validate(AdresseDto adresseDto){
        List<String> errors = new ArrayList<>();
        if (adresseDto == null ) {
            errors.add("Veuiller renseigner l'adresse 1");
            errors.add("Veuiller renseigner l'adresse 2");
            errors.add("Veuiller renseigner le pays");
            errors.add("Veuiller renseigner le code postal");
            LOGGER.error("Address is null or invalid: {}", errors);


            return errors;
        }

        if (!StringUtils.hasLength(adresseDto.getAdresse1())){
            errors.add("Veuiller renseigner l'adresse 1");
            LOGGER.error("Address 1 is null");

        }
        if (!StringUtils.hasLength(adresseDto.getAdresse2())){
            errors.add("Veuiller renseigner l'adresse 2");
            LOGGER.error("Address 2 is null");

        }
        if (!StringUtils.hasLength(adresseDto.getPays())){
            errors.add("Veuiller renseigner le pays");
            LOGGER.error("Pays is null");

        }
        if (!StringUtils.hasLength(adresseDto.getCodePostal())){
            errors.add("Veuiller renseigner le code postal");
            LOGGER.error("Code postal is null");

        }

        return errors;
    }
}
