package com.mystock.mygestock.controller;

import com.mystock.mygestock.controller.api.CommandeFournisseurApi;
import com.mystock.mygestock.dto.CommandeFournisseurDto;
import com.mystock.mygestock.dto.LigneCommandeFournisseurDto;
import com.mystock.mygestock.entity.EtatCommande;
import com.mystock.mygestock.service.CommandeFournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequestMapping("/commandefournisseur")
public class CommandeFournisseurController implements CommandeFournisseurApi {
    private final CommandeFournisseurService commandeFournisseurService;
    @Autowired
    public CommandeFournisseurController(CommandeFournisseurService commandeFournisseurService) {
        this.commandeFournisseurService = commandeFournisseurService;
    }

    @Override
    public CommandeFournisseurDto save(CommandeFournisseurDto dto) {
        return commandeFournisseurService.save(dto);
    }

    @Override
    public CommandeFournisseurDto updateEtatCommande(Long idCommande, EtatCommande etatCommande) {
        return commandeFournisseurService.updateEtatCommande(idCommande,etatCommande);
    }

    @Override
    public CommandeFournisseurDto updateQuantiteCommande(Long idCommande, Long idLigneCommande, BigDecimal quantite) {
        return commandeFournisseurService.updateQuantiteCommande(idCommande,idLigneCommande,quantite);
    }

    @Override
    public CommandeFournisseurDto updateArticle(Long idCommande, Long idLigneCommande, Long idArticle) {
        return commandeFournisseurService.updateArticle(idCommande,idLigneCommande,idArticle);
    }

    @Override
    public CommandeFournisseurDto deleteArticle(Long idCommande, Long idLigneCommande) {
        return commandeFournisseurService.deleteArticle(idCommande,idLigneCommande);
    }

    @Override
    public CommandeFournisseurDto updateFournisseur(Long idCommande, Long idFournisseur) {
        return commandeFournisseurService.updateFournisseur(idCommande,idFournisseur);
    }

    @Override
    public List<CommandeFournisseurDto> findAll() {
        return commandeFournisseurService.findAll();
    }

    @Override
    public List<LigneCommandeFournisseurDto> findAllLigneCommandeFournisseurByCommandeFournisseurId(Long idCommande) {
        return commandeFournisseurService.findAllLigneCommandeFournisseurByCommandeFournisseurId(idCommande);
    }

    @Override
    public CommandeFournisseurDto findById(Long id) {
        return commandeFournisseurService.findById(id);
    }

    @Override
    public void delete(Long id) {
        commandeFournisseurService.delete(id);
    }
}
