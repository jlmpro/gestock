package com.mystock.mygestock.validator;


import com.mystock.mygestock.dto.CommandeClientDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandeClientValidator {
    public  static List<String> validate(CommandeClientDto dto){
        List <String> errors = new ArrayList<>();
        if (dto == null){
            errors.add("Veuiller renseigner le code de la commande");
         //   errors.add("Veuiller renseigner la date de la commande");
            errors.add("Veuiller renseigner l'état de la commande");
            errors.add("Veuiller selectionner le client de la commande");
            return errors;
        }
        if (!StringUtils.hasLength(dto.getCode())){
            errors.add("Veuiller renseigner le code de la commande");
        }
       /* if (dto.getDateCommande() == null){
            errors.add("Veuiller renseigner la date de la commande");
        }*/
        if (!StringUtils.hasLength(dto.getEtatCommande().toString())){
            errors.add("Veuiller renseigner l'état de la commande'");
        }
        if (dto.getClient() == null){
            errors.add("Veuiller selectionner le client de la commande");
        }

        return errors;
    }
}
