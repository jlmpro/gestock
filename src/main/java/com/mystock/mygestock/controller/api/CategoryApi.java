package com.mystock.mygestock.controller.api;

import com.mystock.mygestock.dto.ArticleDto;
import com.mystock.mygestock.dto.CategoryDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface CategoryApi{
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Enregistrer un catégorie", description = "Cette méthode permet d'enregistrer ou de modifier une catégorie", responses = {
            @ApiResponse(responseCode = "200", description = "L'objet catégorie crée / modifié", content = @Content(schema = @Schema(implementation = ArticleDto.class))),
            @ApiResponse(responseCode = "400", description = "L'objet catégorie n'est pas valide")
    })
    CategoryDto save(@RequestBody CategoryDto dto);
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Renvoie la liste des catégories", description = "Cette méthode permet de chercher et de reenvoyer la liste des catégories d'catégories", responses = {
            @ApiResponse(responseCode = "200", description = "Liste des catégories / liste vide", content = @Content(schema = @Schema(implementation = List.class,  subTypes = { ArticleDto.class }))),
    })
    List<CategoryDto> findAll();
    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rechercher une catégorie par ID", description = "Cette méthode permet de chercher une catégorie par ID", responses = {
            @ApiResponse(responseCode = "200", description = "L'objet catégorie a été trouvé dans la BDD", content = @Content(schema = @Schema(implementation = ArticleDto.class))),
            @ApiResponse(responseCode = "400", description = "Aucun objet trouvé dans la BDD avec l'ID fourni")
    })
    CategoryDto findById(@PathVariable Long id);
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Supprimer un catégorie", description = "Permet de supprimer une catégorie par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La catégorie a été supprimé")

    })
    void delete(@PathVariable Long id);
}
