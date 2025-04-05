package com.mystock.mygestock.validator;



import com.mystock.mygestock.dto.VenteDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VenteValidator {
    public  static List<String> validate(VenteDto dto){
        List <String> errors = new ArrayList<>();
        if (dto == null){
            errors.add("Veuiller renseigner le code de la commande");
            errors.add("Veuiller renseigner la date de la vente");
            return errors;
        }
        if (!StringUtils.hasLength(dto.getCode())){
            errors.add("Veuiller renseigner le code de la commande");
        }
        if (dto.getDateVente() == null){
            errors.add("Veuiller renseigner la date de la vente");
        }

        return errors;
    }
}
