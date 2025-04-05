package com.mystock.mygestock.service;



import com.mystock.mygestock.dto.ChangerPasswordUtilisateur;
import com.mystock.mygestock.dto.UtilisateurDto;

import java.util.List;

public interface UtilisateurService {
    UtilisateurDto save(UtilisateurDto  dto);

    List<UtilisateurDto> findAll();

    UtilisateurDto findById(Long id);

    UtilisateurDto changePassword(ChangerPasswordUtilisateur dto);

    UtilisateurDto findByEmail(String email);

    void delete (Long id);
}
