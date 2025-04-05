package com.mystock.mygestock.controller.api;

import com.mystock.mygestock.dto.ClientDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Client", description = "Client API")
public interface ClientApi {
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Enregistrer un client", description = "Cette méthode permet d'enregistrer ou de modifier un client", responses = {
            @ApiResponse(responseCode = "200", description = "L'objet client crée / modifié", content = @Content(schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "404", description = "L'objet client n'est pas valide")
    })
    ClientDto save(@RequestBody ClientDto dto);
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Renvoie la liste des clients", description = "Cette méthode permet de chercher et de reenvoyer la liste des clients", responses = {
            @ApiResponse(responseCode = "200", description = "Liste des clients / liste vide", content = @Content(schema = @Schema(implementation = List.class,  subTypes = { ClientDto.class }))),
    })
    List<ClientDto> findAll();
    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rechercher un client par ID", description = "Cette méthode permet de chercher un client par ID", responses = {
            @ApiResponse(responseCode = "200", description = "L'objet client a été trouvé dans la BDD", content = @Content(schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucun objet trouvé dans la BDD avec l'ID fourni")
    })
    ClientDto findById(@PathVariable Long id);
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Supprimer un client", description = "Permet de supprimer un client par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Le client a été supprimé")
    })
    void delete(@PathVariable Long id);
}
