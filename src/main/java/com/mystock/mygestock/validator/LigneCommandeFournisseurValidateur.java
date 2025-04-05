package com.mystock.mygestock.validator;



import com.mystock.mygestock.dto.LigneCommandeFournisseurDto;

import java.util.ArrayList;
import java.util.List;

public class LigneCommandeFournisseurValidateur {
    public static List<String> validate(LigneCommandeFournisseurDto dto) {
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Veuiller selectionner un article");
            errors.add("Veuiller selectionner une commande");
        }
        if (dto.getArticle() == null) {
            errors.add("Veuiller selectionner un article");
        }
        if (dto.getCommandeFournisseur() == null) {
            errors.add("Veuiller selectionner une commande fournisseur");
        }

        return errors;
    }
}
