package com.mystock.mygestock.controller.api;

import com.mystock.mygestock.dto.FournisseurDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Fournisseur", description = "Fournisseur API")
public interface FournisseurApi {
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Enregistrer un fournisseur", description = "Cette méthode permet d'enregistrer ou de modifier un fournisseur", responses = {
            @ApiResponse(responseCode = "200", description = "L'objet fournisseur crée / modifié", content = @Content(schema = @Schema(implementation = FournisseurDto.class))),
            @ApiResponse(responseCode = "404", description = "L'objet fournisseur n'est pas valide")
    })
    FournisseurDto save(@RequestBody FournisseurDto dto);
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Renvoie la liste des fournisseurs", description = "Cette méthode permet de chercher et de reenvoyer la liste des fournisseurs", responses = {
            @ApiResponse(responseCode = "200", description = "Liste des fournisseurs / liste vide", content = @Content(schema = @Schema(implementation = List.class,  subTypes = { FournisseurDto.class }))),
    })
    List<FournisseurDto> findAll();
    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rechercher un fournisseur par ID", description = "Cette méthode permet de chercher un fournisseur par ID", responses = {
            @ApiResponse(responseCode = "200", description = "L'objet fournisseur a été trouvé dans la BDD", content = @Content(schema = @Schema(implementation = FournisseurDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucun objet trouvé dans la BDD avec l'ID fourni")
    })
    FournisseurDto findById(@PathVariable Long id);
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Supprimer un fournisseur", description = "Permet de supprimer un fournisseur par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Le fournisseur a été supprimé")
    })
    void delete(@PathVariable Long id);
}
