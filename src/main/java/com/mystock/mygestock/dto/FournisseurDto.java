package com.mystock.mygestock.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mystock.mygestock.model.CommandeFournisseur;
import com.mystock.mygestock.model.Fournisseur;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class FournisseurDto {
    private Long id ;
    private  String nom ;
    private String prenom ;
    private AdresseDto adresse;
    private String photo;
    private  String mail ;
    private String numTel ;
    @JsonIgnore
    private List<CommandeFournisseurDto> commandeFournisseurs;

    public static FournisseurDto fromEntity(Fournisseur fournisseur){
        if (fournisseur == null){
            // return exception
            return null;
        }

   //   List<CommandeFournisseurDto> commandeFournisseurDtos = new ArrayList<>();
     //   for (CommandeFournisseur commandeFournisseur : fournisseur.getCommandeFournisseur()) {
  //          commandeFournisseurDtos.add(CommandeFournisseurDto.fromEntity(commandeFournisseur));
  //      }

        return FournisseurDto.builder()
                .id(fournisseur.getId())
                .nom(fournisseur.getNom())
                .prenom(fournisseur.getPrenom())
                .adresse(AdresseDto.fromEntity(fournisseur.getAdresse()))
                .photo(fournisseur.getPhoto())
                .mail(fournisseur.getMail())
                .numTel(fournisseur.getNumTel())
      //         .commandeFournisseurs(commandeFournisseurDtos)
                .build();
    }
    public static Fournisseur toEntity(FournisseurDto fournisseurDto){
        if (fournisseurDto == null){
            return null;
        }

    //   List<CommandeFournisseur> commandeFournisseurs = new ArrayList<>();
     //   for (CommandeFournisseurDto commandeFournisseurDto : fournisseurDto.getCommandeFournisseurs()) {
    //        commandeFournisseurs.add(CommandeFournisseurDto.toEntity(commandeFournisseurDto));
   //    }

        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(fournisseurDto.getId());
        fournisseur.setNom(fournisseurDto.getNom());
        fournisseur.setPrenom(fournisseurDto.getPrenom());
        fournisseur.setPhoto(fournisseurDto.getPhoto());
        fournisseur.setMail(fournisseurDto.getMail());
        fournisseur.setNumTel(fournisseurDto.getNumTel());
        fournisseur.setAdresse(AdresseDto.toEntity(fournisseurDto.getAdresse()));
    //    fournisseur.setCommandeFournisseur(commandeFournisseurs);
        return fournisseur;

    }
}
