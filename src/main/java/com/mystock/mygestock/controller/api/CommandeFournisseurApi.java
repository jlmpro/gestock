package com.mystock.mygestock.controller.api;

import com.mystock.mygestock.dto.CommandeFournisseurDto;
import com.mystock.mygestock.dto.LigneCommandeFournisseurDto;
import com.mystock.mygestock.model.EtatCommande;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Commande_Fournisseur", description = "Commande fournisseur API")
public interface CommandeFournisseurApi {

    @PostMapping(value = "/save")
    CommandeFournisseurDto save(@RequestBody CommandeFournisseurDto dto);

    @PatchMapping(value = "/update/etat/{idCommande}/{etatCommande}")
    CommandeFournisseurDto updateEtatCommande(
            @PathVariable ("idCommande") Long idCommande, @PathVariable ("etatCommande")EtatCommande etatCommande);

    @PatchMapping(value = "/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
    CommandeFournisseurDto updateQuantiteCommande(
            @PathVariable("idCommande") Long idCommande,
            @PathVariable("idLigneCommande") Long idLigneCommande,
            @PathVariable("quantite") BigDecimal quantite);
    @PatchMapping(value = "/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
    CommandeFournisseurDto updateArticle(
            @PathVariable("idCommande") Long idCommande,
            @PathVariable("idLigneCommande") Long idLigneCommande,
            @PathVariable("idArticle") Long idArticle);
    @DeleteMapping(value = "/delete/article/{idCommande}/{idLigneCommande}")
    CommandeFournisseurDto deleteArticle(
            @PathVariable("idCommande") Long idCommande,
            @PathVariable("idLigneCommande") Long idLigneCommande);

    @PatchMapping(value = "/update/client/{idCommande}/{idFournisseur}")
    CommandeFournisseurDto updateFournisseur(@PathVariable("idCommande") Long idCommande,
                                                   @PathVariable("idFournisseur") Long idFournisseur);


    @GetMapping(value = "/all")
    List<CommandeFournisseurDto> findAll();

    @GetMapping(value = "/find/lignescommande/{idCommande}")
    List<LigneCommandeFournisseurDto> findAllLigneCommandeFournisseurByCommandeFournisseurId(@PathVariable Long idCommande);

    @GetMapping(value = "/find/{id}")
    CommandeFournisseurDto findById(@PathVariable Long id);

    @DeleteMapping(value = "/find/{id}")
    void delete(@PathVariable Long id);
}
