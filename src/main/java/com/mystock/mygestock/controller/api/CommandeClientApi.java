package com.mystock.mygestock.controller.api;

import com.mystock.mygestock.dto.CommandeClientDto;
import com.mystock.mygestock.dto.LigneCommandeClientDto;
import com.mystock.mygestock.model.EtatCommande;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@Tag(name = "Commande_Client", description = "Commande_Client API")
public interface CommandeClientApi {


    @PostMapping(value = "/save")
    ResponseEntity<CommandeClientDto> save(@RequestBody CommandeClientDto dto);

    @PatchMapping(value = "/update/etat/{idCommande}/{etatCommande}")
    ResponseEntity<CommandeClientDto> updateEtatCommande(
            @PathVariable ("idCommande") Long idCommande, @PathVariable ("etatCommande")EtatCommande etatCommande);

    @PatchMapping(value = "/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
   ResponseEntity<CommandeClientDto> updateQuantiteCommande(
           @PathVariable("idCommande") Long idCommande,
           @PathVariable("idLigneCommande") Long idLigneCommande,
           @PathVariable("quantite") BigDecimal quantite);
    @PatchMapping(value = "/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
    ResponseEntity<CommandeClientDto> updateArticle(
            @PathVariable("idCommande") Long idCommande,
            @PathVariable("idLigneCommande") Long idLigneCommande,
            @PathVariable("idArticle") Long idArticle);
    @DeleteMapping(value = "/delete/article/{idCommande}/{idLigneCommande}")
    ResponseEntity<CommandeClientDto> deleteArticle(
            @PathVariable("idCommande") Long idCommande,
            @PathVariable("idLigneCommande") Long idLigneCommande);
    @GetMapping(value = "/find/lignescommande/{idCommande}")
    ResponseEntity<List<LigneCommandeClientDto>> findAllLigneCommandeClientByCommandeClientId(@PathVariable("idCommande") Long idCommande);

    @PatchMapping(value = "/update/client/{idCommande}/{idClient}")
    ResponseEntity<CommandeClientDto> updateClient(@PathVariable("idCommande") Long idCommande,
                                                   @PathVariable("idClient") Long idClient);

    @GetMapping(value = "/find/{id}")
    ResponseEntity<CommandeClientDto> findById(@PathVariable Long id);
    @GetMapping(value = "/all")
    ResponseEntity<List<CommandeClientDto>> findAll();
    @DeleteMapping(value = "/delete/{id}")
    ResponseEntity delete(@PathVariable Long id);
}
