package com.mystock.mygestock.controller;

import com.mystock.mygestock.controller.api.UtilisateurApi;
import com.mystock.mygestock.dto.UtilisateurDto;
import com.mystock.mygestock.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/utilisateur")
public class UtilisateurController implements UtilisateurApi {
    private final UtilisateurService utilisateurService;
    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Override
    public UtilisateurDto save(UtilisateurDto dto) {
        return utilisateurService.save(dto);
    }

    @Override
    public List<UtilisateurDto> findAll() {
        return utilisateurService.findAll();
    }

    @Override
    public UtilisateurDto findById(Long id) {
        return utilisateurService.findById(id);
    }

    @Override
    public UtilisateurDto findByEmail(String email) {
        return utilisateurService.findByEmail(email);
    }

    @Override
    public void delete(Long id) {
        utilisateurService.delete(id);
    }

}
