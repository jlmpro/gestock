package com.mystock.mygestock.controller;

import com.mystock.mygestock.controller.api.CommandeClientApi;
import com.mystock.mygestock.dto.CommandeClientDto;
import com.mystock.mygestock.dto.LigneCommandeClientDto;
import com.mystock.mygestock.model.EtatCommande;
import com.mystock.mygestock.service.CommandeClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequestMapping("/commandeclient")
public class CommandeClientController implements CommandeClientApi {

    private final CommandeClientService commandeClientService;
    @Autowired
    public CommandeClientController(CommandeClientService commandeClientService) {
        this.commandeClientService = commandeClientService;
    }

    @Override
    public ResponseEntity<CommandeClientDto> save(CommandeClientDto dto) {
        return ResponseEntity.ok(commandeClientService.save(dto));
    }

    @Override
    public ResponseEntity<CommandeClientDto> updateEtatCommande(Long idCommande, EtatCommande etatCommande) {
        return ResponseEntity.ok(commandeClientService.updateEtatCommande(idCommande, etatCommande));
    }

    @Override
    public ResponseEntity<CommandeClientDto> updateQuantiteCommande(Long idCommande, Long idLigneCommande, BigDecimal quantite) {
        return ResponseEntity.ok(commandeClientService.updateQuantiteCommande(idCommande,idLigneCommande,quantite));
    }

    @Override
    public ResponseEntity<CommandeClientDto> updateArticle(Long idCommande, Long idLigneCommande, Long idArticle) {
        return ResponseEntity.ok(commandeClientService.updateArticle(idCommande,idLigneCommande,idArticle));
    }

    @Override
    public ResponseEntity<CommandeClientDto> deleteArticle(Long idCommande, Long idLigneCommande) {
        return ResponseEntity.ok(commandeClientService.deleteArticle(idCommande,idLigneCommande));
    }

    @Override
    public ResponseEntity<List<LigneCommandeClientDto>> findAllLigneCommandeClientByCommandeClientId(Long idCommande) {
        return ResponseEntity.ok(commandeClientService.findAllLigneCommandeClientByCommandeClientId(idCommande));
    }

    @Override
    public ResponseEntity<CommandeClientDto> updateClient(Long idCommande, Long idClient) {
        return ResponseEntity.ok(commandeClientService.updateClient(idCommande,idClient));
    }

    @Override
    public ResponseEntity<CommandeClientDto> findById(Long id) {
        return ResponseEntity.ok(commandeClientService.findById(id));
    }

    @Override
    public ResponseEntity<List<CommandeClientDto>> findAll() {
        return ResponseEntity.ok(commandeClientService.findAll()) ;
    }

    @Override
    public ResponseEntity delete(Long id) {
         commandeClientService.delete(id);
         return ResponseEntity.ok().build();
    }
}
