package com.mystock.mygestock.dto;



import com.mystock.mygestock.entity.CommandeClient;
import com.mystock.mygestock.entity.EtatCommande;
import com.mystock.mygestock.entity.LigneCommandeClient;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


@Builder
@Data
public class CommandeClientDto {
    private Long id ;
    private String code;
    private Instant dateCommande;
    private EtatCommande etatCommande;
    private ClientDto client;

    private List<LigneCommandeClientDto> ligneCommandeClients;

    public static CommandeClientDto fromEntity(CommandeClient commandeClient){
        if (commandeClient == null){
            return null ;
        }
        return CommandeClientDto.builder()
                .id(commandeClient.getId())
                .code(commandeClient.getCode())
                .dateCommande(commandeClient.getDateCommande())
                .etatCommande(commandeClient.getEtatCommande())
                .client(ClientDto.fromEntity(commandeClient.getClient()))
                .ligneCommandeClients(
                        commandeClient.getLigneCommandeClients() != null
                                ? commandeClient.getLigneCommandeClients().stream()
                                .map(LigneCommandeClientDto::fromEntity)
                                .collect(Collectors.toList())
                                : null
                )

                .build();
    }

    public static CommandeClient toEntity(CommandeClientDto dto) {
        if (dto == null) {
            return null;
        }

        CommandeClient entity = new CommandeClient();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setDateCommande(dto.getDateCommande());
        entity.setEtatCommande(dto.getEtatCommande());
        entity.setClient(ClientDto.toEntity(dto.getClient()));

        if (dto.getLigneCommandeClients() != null) {
            List<LigneCommandeClient> lignes = dto.getLigneCommandeClients().stream()
                    .map(LigneCommandeClientDto::toEntity)
                    .peek(ligne -> ligne.setCommandeClient(entity)) // éviter la boucle infinie
                    .collect(Collectors.toList());
            entity.setLigneCommandeClients(lignes);
        }

        return entity;
    }









  /*  public boolean isCommandeLivree(){
        return EtatCommande.LIVREE.equals(this.etatCommande);
    }*/




}
