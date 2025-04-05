package com.mystock.mygestock.validator;


import com.mystock.mygestock.dto.LigneCommandeClientDto;

import java.util.ArrayList;
import java.util.List;

public class LigneCommandeClientValidator {
    public static List<String> validate(LigneCommandeClientDto dto) {
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Veuiller selectionner un article");
            errors.add("Veuiller selectionner une commande");
        }
        if (dto.getArticle() == null) {
            errors.add("Veuiller selectionner un article");
        }
        if (dto.getCommandeClient() == null) {
            errors.add("Veuiller selectionner une commande");
        }

        return errors;
    }
}
