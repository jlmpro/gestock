package com.mystock.mygestock.model;



public enum EtatCommande {
    EN_PREPARATION,
    EN_COURS,
    LIVREE;

    public static EtatCommande fromValue(String value) {
        try {
            return EtatCommande.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid EtatCommande value: " + value);
        }
    }


}
