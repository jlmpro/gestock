package com.mystock.mygestock.controller.api;

import com.mystock.mygestock.dto.ArticleDto;
import com.mystock.mygestock.dto.LigneCommandeClientDto;
import com.mystock.mygestock.dto.LigneCommandeFournisseurDto;
import com.mystock.mygestock.dto.LigneVenteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Article", description = "Article API")
public interface ArticleApi {
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Enregistrer un article", description = "Cette méthode permet d'enregistrer ou de modifier un article", responses = {
            @ApiResponse(responseCode = "200", description = "L'objet article crée / modifié", content = @Content(schema = @Schema(implementation = ArticleDto.class))),
            @ApiResponse(responseCode = "400", description = "L'objet article n'est pas valide")
    })
    ArticleDto save(@RequestBody ArticleDto dto);

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Renvoie la liste des articles", description = "Cette méthode permet de chercher et de reenvoyer la liste des articles", responses = {
            @ApiResponse(responseCode = "200", description = "Liste des articles / liste vide", content = @Content(schema = @Schema(implementation = List.class,  subTypes = { ArticleDto.class }))),
    })
    List<ArticleDto> findAll();

    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rechercher un article par ID", description = "Cette méthode permet de chercher un article par ID", responses = {
            @ApiResponse(responseCode = "200", description = "L'objet article a été trouvé dans la BDD", content = @Content(schema = @Schema(implementation = ArticleDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucun objet trouvé dans la BDD avec l'ID fourni")
    })
    ArticleDto findById(@PathVariable Long id);
    @GetMapping(value = "/historique/vente/{idArticle}")
    List<LigneVenteDto> findHistoriqueVente(@PathVariable("idArticle")Long idArticle);
    @GetMapping(value = "/historique/commandeclient/{idArticle}")
    List<LigneCommandeClientDto> findHistoriqueCommandeClient(@PathVariable("idArticle") Long idArticle);
    @GetMapping(value = "/historique/commandefournisseur/{idArticle}")
    List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(@PathVariable("idArticle")Long idArticle);
    @GetMapping(value = "/filter/category/{idArticle}")
    List<ArticleDto> findAllArticleByIdCategory(@PathVariable("idArticle")Long idArticle);
    @GetMapping(value = "/find/article/{codeArticle}")
    ArticleDto findArticleByCodeArticle(@PathVariable("codeArticle") String codeArticle);

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Supprimer un article", description = "Permet de supprimer un article par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "L'article a été supprimé")
    })
    void delete(@PathVariable Long id);

}
