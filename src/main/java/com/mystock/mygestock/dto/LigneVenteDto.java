package com.mystock.mygestock.dto;

import com.mystock.mygestock.model.LigneVente;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class LigneVenteDto {
    private Long id ;
    private VenteDto vente ;
    private BigDecimal quantite ;
    private BigDecimal prixUnitaire;
    private ArticleDto article;


    public static LigneVenteDto fromEntity(LigneVente ligneVente) {
        if (ligneVente == null) {
            return null;
        }
        return LigneVenteDto.builder()
                .id(ligneVente.getId())
                .quantite(ligneVente.getQuantite())
                .prixUnitaire(ligneVente.getPrixUnitaire())
                .article(ArticleDto.fromEntity(ligneVente.getArticle()))
                .build();
    }
    public static LigneVente toEntity(LigneVenteDto ligneVenteDto){
        LigneVente ligneVente = new LigneVente();
        ligneVente.setId(ligneVenteDto.getId());
        ligneVente.setQuantite(ligneVenteDto.getQuantite());
        ligneVente.setPrixUnitaire(ligneVenteDto.getPrixUnitaire());
        return ligneVente;
    }
}
