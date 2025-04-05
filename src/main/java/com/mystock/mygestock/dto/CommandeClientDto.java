package com.mystock.mygestock.dto;



import com.mystock.mygestock.model.CommandeClient;
import com.mystock.mygestock.model.EtatCommande;
import com.mystock.mygestock.model.LigneCommandeClient;
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
                .build();
    }

    public static CommandeClient toEntity(CommandeClientDto commandeClientDto){
        CommandeClient commandeClient = new CommandeClient();
        commandeClient.setId(commandeClientDto.getId());
        commandeClient.setCode(commandeClientDto.getCode());
        commandeClient.setDateCommande(commandeClientDto.getDateCommande());
        commandeClient.setEtatCommande(EtatCommande.valueOf(commandeClientDto.getEtatCommande().name()));
        commandeClient.setClient(ClientDto.toEntity(commandeClientDto.getClient())); // appel de la méthode toEntity de ClientDto

        List<LigneCommandeClient> ligneCommandeClients = commandeClientDto.getLigneCommandeClients().stream()
                .map(LigneCommandeClientDto::toEntity) // appel de la méthode toEntity de LigneCommandeClientDto
                .collect(Collectors.toList());

        commandeClient.setLigneCommandeClients(ligneCommandeClients);

        return commandeClient;
    }








  /*  public boolean isCommandeLivree(){
        return EtatCommande.LIVREE.equals(this.etatCommande);
    }*/




}
