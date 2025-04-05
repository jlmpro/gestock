package com.mystock.mygestock.dto;



import com.mystock.mygestock.model.CommandeFournisseur;
import com.mystock.mygestock.model.EtatCommande;
import com.mystock.mygestock.model.LigneCommandeFournisseur;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class CommandeFournisseurDto {
    private Long id;
    private String code;
    private Instant dateCommande;
    private EtatCommande etatCommande;
    private FournisseurDto fournisseur;

    private List<LigneCommandeFournisseurDto> ligneCommandeFournisseurs;

    public static CommandeFournisseurDto fromEntity(CommandeFournisseur commandeFournisseur) {
        if (commandeFournisseur == null) {
            return null;
        }
        return CommandeFournisseurDto.builder()
                .id(commandeFournisseur.getId())
                .code(commandeFournisseur.getCode())
                .dateCommande(commandeFournisseur.getDateCommande())
                .etatCommande(commandeFournisseur.getEtatCommande())
                .fournisseur(FournisseurDto.fromEntity(commandeFournisseur.getFournisseur()))
                .build();
    }

    public static CommandeFournisseur toEntity(CommandeFournisseurDto commandeFournisseurDto) {
        CommandeFournisseur commandeFournisseur = new CommandeFournisseur();
        commandeFournisseur.setId(commandeFournisseurDto.getId());
        commandeFournisseur.setCode(commandeFournisseurDto.getCode());
        commandeFournisseur.setDateCommande(commandeFournisseurDto.getDateCommande());
        commandeFournisseur.setEtatCommande(EtatCommande.valueOf(commandeFournisseurDto.getEtatCommande().name()));
        commandeFournisseur.setFournisseur(FournisseurDto.toEntity(commandeFournisseurDto.getFournisseur())); // appel de la méthode toEntity de FournisseurDto

        List<LigneCommandeFournisseur> ligneCommandeFournisseurs = commandeFournisseurDto.getLigneCommandeFournisseurs().stream()
                .map(LigneCommandeFournisseurDto::toEntity) // appel de la méthode toEntity de LigneCommandeFournisseurDto
                .collect(Collectors.toList());

        commandeFournisseur.setLigneCommandeFournisseurs(ligneCommandeFournisseurs);

        return commandeFournisseur;
    }


    public boolean isCommandeLivree(){
        return EtatCommande.LIVREE.equals(this.etatCommande);
    }
}
