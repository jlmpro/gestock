package com.mystock.mygestock.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mystock.mygestock.model.Utilisateur;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UtilisateurDto {
    private Long id;
    private String lastname;

    private String firstname;

    private String email;

    private String dateDeNaissance;

    private String password;

    private AdresseDto adresse;

    private String photo;
    @JsonIgnore
    private List<RolesDto> role;

    public static UtilisateurDto fromEntity(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        return UtilisateurDto.builder()
                .id(utilisateur.getId())
                .lastname(utilisateur.getLastname())
                .firstname(utilisateur.getFirstname())
                .email(utilisateur.getEmail())
                .dateDeNaissance(utilisateur.getDateDeNaissance())
                .password(utilisateur.getPassword())
                .photo(utilisateur.getPhoto()).build();
    }

    public static Utilisateur toEntity(UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(utilisateurDto.getId());
        utilisateur.setFirstname(utilisateurDto.getLastname());
        utilisateur.setLastname(utilisateurDto.getFirstname());
        utilisateur.setEmail(utilisateurDto.getEmail());
        utilisateur.setDateDeNaissance(utilisateurDto.getDateDeNaissance());
        utilisateur.setPassword(utilisateurDto.getPassword());
        utilisateur.setPhoto(utilisateurDto.getPhoto());
        return utilisateur;
    }

}
