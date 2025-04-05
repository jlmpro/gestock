package com.mystock.mygestock.service.Impl;


import com.mystock.mygestock.dto.*;
import com.mystock.mygestock.exception.EntityNotFoundException;
import com.mystock.mygestock.exception.ErrorCodes;
import com.mystock.mygestock.exception.InvalidEntityException;
import com.mystock.mygestock.exception.InvalidOperationException;
import com.mystock.mygestock.model.*;
import com.mystock.mygestock.repository.ArticleRepository;
import com.mystock.mygestock.repository.ClientRepository;
import com.mystock.mygestock.repository.CommandeClientRepository;
import com.mystock.mygestock.repository.LigneCommandeClientRepository;
import com.mystock.mygestock.service.CommandeClientService;
import com.mystock.mygestock.service.MvtStkService;
import com.mystock.mygestock.validator.ArticleValidator;
import com.mystock.mygestock.validator.CommandeClientValidator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommandeClientServiceImpl implements CommandeClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandeClientServiceImpl.class);

    private CommandeClientRepository commandeClientRepository;
    private ClientRepository clientRepository;
    private ArticleRepository articleRepository;
    private LigneCommandeClientRepository ligneCommandeClientRepository;
    private MvtStkService mvtStkService;
    @Autowired
    public CommandeClientServiceImpl(CommandeClientRepository commandeClientRepository,
                                     ClientRepository clientRepository,
                                     ArticleRepository articleRepository,
                                     LigneCommandeClientRepository ligneCommandeClientRepository, MvtStkService mvtStkService) {


        this.commandeClientRepository = commandeClientRepository;
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepository;
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
        this.mvtStkService = mvtStkService;
    }

    public boolean isCommandeLivree(Long commandeId) {
        CommandeClient commande = commandeClientRepository.findById(commandeId)
                .orElseThrow(() -> new NotFoundException("Commande not found with id " + commandeId));

        return EtatCommande.LIVREE.equals(commande.getEtatCommande());
    }



    private void checkIdCommande(Long idCommande){
        if(idCommande== null){
            log.error("Commande Client ID is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec in ID null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private void checkIdArticle(Long idArticle, String msg){
        if(idArticle== null){
            log.error("L'Id " +msg+ " de l'article est null");
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande avec un " + msg+ " ID article null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private CommandeClientDto checkCommandeLivree(CommandeClientDto commandeClient){
        if (isCommandeLivree(commandeClient.getId())){
            throw new InvalidOperationException("Impossible de modifier la commade client lorqu'elle est livrée",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        return commandeClient;
    }

    private void checkIdLigneCommande(Long idLigneCommande){
        if(idLigneCommande == null){
            log.error("LigneCommande ID is null");
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande avec une ligne de commande null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private Optional<LigneCommandeClient> findLigneCommandeClient(Long idLigneCommande) {
        Optional<LigneCommandeClient> ligneCommandeClientOptional = ligneCommandeClientRepository.findById(idLigneCommande);

        if (ligneCommandeClientOptional.isEmpty()){
            throw new EntityNotFoundException(
                    "Aucune commande client avec l'ID = " + idLigneCommande + " n'a été trouvée dans la BD",
                    ErrorCodes.COMMANDE_CLIENT_NOT_FOUND);
        }
        return ligneCommandeClientOptional;

    }

    @Override
    public CommandeClientDto save(CommandeClientDto dto) {
        log.debug("Saving Commande client: " + dto);

        if (dto == null) {
            throw new IllegalArgumentException("L'objet CommandeClientDto ne peut pas être null");
        }

        List<LigneCommandeClientDto> ligneCommandeClients = dto.getLigneCommandeClients();
        if (ligneCommandeClients == null) {
            ligneCommandeClients = new ArrayList<>();
        }

        List<String> errors = CommandeClientValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Commande client is not valid {}", dto);
            throw new InvalidEntityException("La commande client n'est pas valide", ErrorCodes.COMMANDE_CLIENT_NOT_VALID, errors);
        }

        if (dto.getId() != null && isCommandeLivree(dto.getId())){
            throw new InvalidOperationException("Impossible de modifier la commade client lorqu'elle est livrée",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        Optional<Client> client = clientRepository.findById(dto.getClient().getId());
        if (client.isEmpty()) {
            log.error("Client is not selected {}", dto);

            throw new EntityNotFoundException("Aucun client avec l'ID = " + dto.getClient().getId() + " n'a été trouvé dans la BD", ErrorCodes.CLIENT_NOT_FOUND);
        }
        CommandeClient savedCommandeClient = commandeClientRepository.save(CommandeClientDto.toEntity(dto));
        log.error("Commande saved {}", dto);


        ligneCommandeClients.forEach(ligneCmd -> {
            Optional<Article> article = articleRepository.findById(ligneCmd.getArticle().getId());
            if (article.isEmpty()) {
                log.error("Article is empty");

                throw new EntityNotFoundException("Aucun article avec l'ID = " + ligneCmd.getArticle().getId() + " n'a été trouvé dans la BD", ErrorCodes.ARTICLE_NOT_FOUND);
            }
            LigneCommandeClient ligneCommandeClient = ligneCmd.toEntity(ligneCmd);
            ligneCommandeClient.setCommandeClient(savedCommandeClient);
            ligneCommandeClientRepository.save(ligneCommandeClient);

            log.error("Success", dto);


            // mise à jour du stock
            // Article articleToUpdate = article.get();
            // articleToUpdate.setQuantite(articleToUpdate.getQuantite() - ligneCmd.getQuantite());
            // articleRepository.save(articleToUpdate);
        });

        return CommandeClientDto.fromEntity(savedCommandeClient);
    }


    @Override
    public CommandeClientDto updateEtatCommande(Long idCommande, EtatCommande etatCommande) {

      checkIdCommande(idCommande);

      if(!StringUtils.hasLength(String.valueOf(etatCommande))){
            log.error("Commande state is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un ID null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClientDto commandeClient = findById(idCommande);
       checkCommandeLivree(commandeClient);
        commandeClient.setEtatCommande(etatCommande);

       CommandeClient savedCmdClt =  commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient));
       if (isCommandeLivree(commandeClient.getId())){
       updateMvtStk(idCommande);
       }
        return CommandeClientDto.fromEntity(savedCmdClt);
    }

    @Override
    public CommandeClientDto updateQuantiteCommande(Long idCommande, Long idLigneCommande, BigDecimal quantite) {

         checkIdCommande(idCommande);

         checkIdLigneCommande(idLigneCommande);

        if(quantite == null || quantite.compareTo(BigDecimal.ZERO) ==0){
            log.error("Quantite ID is null");
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande avec une quantite null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClientDto commandeClient = findById(idCommande);

        checkCommandeLivree(commandeClient);

        Optional<LigneCommandeClient> ligneCommandeClientOptional =
         findLigneCommandeClient(idLigneCommande);
        LigneCommandeClient ligneCommandeClient = ligneCommandeClientOptional.get();
        ligneCommandeClient.setQuantite(quantite);
        ligneCommandeClientRepository.save(ligneCommandeClient);

        return commandeClient;
    }



    @Override
    public CommandeClientDto updateClient(Long idCommande, Long idClient) {

       checkIdCommande(idCommande);

        if(idClient== null){
            log.error("Commande ID is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un ID client null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClientDto commandeClient = findById(idCommande);

        checkCommandeLivree(commandeClient);

        Optional<Client> clientOptional = clientRepository.findById(idClient);
        if (clientOptional.isEmpty()){
            throw new EntityNotFoundException("Aucun client avec l'ID = "+ idClient+ " n'a été trouve dans la base");
        }

        commandeClient.setClient(ClientDto.fromEntity(clientOptional.get()));

        return CommandeClientDto.fromEntity(
                commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient))
        );
    }

    @Override
    public CommandeClientDto updateArticle(Long idCommande, Long idLigneCommande,Long idArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(idArticle,"nouvel");

        CommandeClientDto commandeClient = findById(idCommande);

        checkCommandeLivree(commandeClient);

        Optional<LigneCommandeClient> ligneCommandeClientOptional =
               findLigneCommandeClient(idLigneCommande);

        Optional<Article> articleOptional = articleRepository.findById(idArticle);
        if (articleOptional.isEmpty()){
            throw new EntityNotFoundException(
                    "Aucun article avec l'ID = " + idArticle + " n'a été trouvée dans la BD",
                    ErrorCodes.ARTICLE_NOT_FOUND);
        }

       List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));
        if (!errors.isEmpty()){
            throw new InvalidEntityException("Article invalide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        LigneCommandeClient ligneCommandeClientToSaved = ligneCommandeClientOptional.get();

        ligneCommandeClientToSaved.setArticle(articleOptional.get());

        ligneCommandeClientRepository.save(ligneCommandeClientToSaved);

        return commandeClient;
    }

    @Override
    public CommandeClientDto findById(Long id) {
        if (id == null) {
            log.error("Commande client ID is null");
            return null;
        }

        return commandeClientRepository.findById(id).map(CommandeClientDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException("Aucune commande client avec l'ID = " + id + " n'a été trouvée dans la BD", ErrorCodes.COMMANDE_CLIENT_NOT_FOUND)
        );
    }



    @Override
    public List<CommandeClientDto> findAll() {
        return commandeClientRepository.findAll().stream()
                .map(CommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeClientDto> findAllLigneCommandeClientByCommandeClientId(Long idCommande) {

        return ligneCommandeClientRepository.
                findAllByCommandeClientId(idCommande).stream()
                .map(LigneCommandeClientDto::fromEntity)
                .collect(Collectors.toList())

                ;
    }

    @Override
    public CommandeClientDto deleteArticle(Long idCommande, Long idLigneCommande) {

        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CommandeClientDto commandeClient = findById(idCommande);
        checkCommandeLivree(commandeClient);

        // Just to check the ligneCommandeClient and inform the client in case it is absent

        findLigneCommandeClient(idLigneCommande);
        ligneCommandeClientRepository.deleteById(idLigneCommande);

        return null;
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            log.error("Commande client ID is null");
            return;
        }

        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(id);
        if (!ligneCommandeClients.isEmpty()){
            throw new InvalidOperationException(
                    "Impossible de supprimer une commande client déjà utilisé",
                    ErrorCodes.COMMANDE_CLIENT_ALREADY_IN_USE);
        }

        commandeClientRepository.deleteById(id);

    }

    private void updateMvtStk(Long idCommande){
        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(idCommande);
        ligneCommandeClients.forEach(lig -> {
            MvtStkDto mvtStkDto = MvtStkDto.builder()
                    .article(ArticleDto.fromEntity(lig.getArticle()))
                    .dateMvt(Instant.now())
                    .typeMvtStk(TypeMvtStk.SORTIE)
                    .sourceMvtStk(SourceMvtStk.COMMANDE_CLIENT)
                    .quantite(lig.getQuantite())
                    .build();
            mvtStkService.sortieStock(mvtStkDto);
        });
    }
}
