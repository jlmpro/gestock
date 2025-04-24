package com.mystock.mygestock.dto;


import com.mystock.mygestock.entity.Article;
import com.mystock.mygestock.entity.Category;
import com.mystock.mygestock.entity.LigneCommandeClient;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class LigneCommandeClientDto {
    private Long id;
    private Long idArticle;
    private BigDecimal quantite;
    private  CommandeClientDto commandeClient;

    public static LigneCommandeClientDto fromEntity(LigneCommandeClient ligneCommandeClient) {
        if (ligneCommandeClient == null) {
            return null;
        }

        Long articleDto = null;
        if (ligneCommandeClient.getArticle() != null) {
            articleDto = ligneCommandeClient.getArticle().getId();
        }

        return LigneCommandeClientDto.builder()
                .id(ligneCommandeClient.getId())
                .quantite(ligneCommandeClient.getQuantite())
                .idArticle(articleDto)
                .build();

    }
    public static LigneCommandeClient toEntity(LigneCommandeClientDto dto) {
        if (dto == null) {
            return null;
        }

        LigneCommandeClient entity = new LigneCommandeClient();
        entity.setId(dto.getId());
        entity.setQuantite(dto.getQuantite());

        if (dto.getIdArticle() != null) {
            Article article = new Article();
            article.setId(dto.getIdArticle());
            entity.setArticle(article);
        }

        // On ne set PAS commandeClient ici pour éviter une boucle infinie (StackOverflowError)
        // ligneCommandeClient.setCommandeClient(CommandeClientDto.toEntity(dto.getCommandeClient()));

        return entity;
    }


}
