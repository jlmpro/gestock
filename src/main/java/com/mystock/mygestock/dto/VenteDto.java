package com.mystock.mygestock.dto;

import com.mystock.mygestock.model.Vente;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class VenteDto {
    private Long id ;
    private String code;
    private String dateVente;
    private String commentaire;

    private List<LigneVenteDto> ligneVentes;



    public static VenteDto fromEntity(Vente vente){
        if (vente == null){
            // return exception
            return null;
        }
        // Mapping de VenteDto -> Vente
        return VenteDto.builder()
                .id(vente.getId())
                .code(vente.getCode())
                .dateVente(vente.getDateVente())
                .commentaire(vente.getCommentaire())
               // .ligneVentes(LigneVenteDto.fromEntity(vente.getLigneVentes()))
                .build();
    }
    public static Vente toEntity(VenteDto venteDto){
        if (venteDto == null) {
            // throw ecxception
        }
        Vente vente = new Vente();
        vente.setId(venteDto.getId());
        vente.setCode(venteDto.getCode());
        vente.setDateVente(venteDto.getDateVente());
        vente.setCommentaire(venteDto.getCommentaire());
        return vente;
    }
}
