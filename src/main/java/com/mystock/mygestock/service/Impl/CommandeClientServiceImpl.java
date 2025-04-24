package com.mystock.mygestock.service.Impl;


import com.mystock.mygestock.dto.*;
import com.mystock.mygestock.exception.EntityNotFoundException;
import com.mystock.mygestock.exception.ErrorCodes;
import com.mystock.mygestock.exception.InvalidEntityException;
import com.mystock.mygestock.exception.InvalidOperationException;
import com.mystock.mygestock.entity.*;
import com.mystock.mygestock.repository.ArticleRepository;
import com.mystock.mygestock.repository.ClientRepository;
import com.mystock.mygestock.repository.CommandeClientRepository;
import com.mystock.mygestock.repository.LigneCommandeClientRepository;
import com.mystock.mygestock.service.CommandeClientService;
import com.mystock.mygestock.service.MvtStkService;
import com.mystock.mygestock.validator.ArticleValidator;
import com.mystock.mygestock.validator.CommandeClientValidator;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    private final CommandeClientRepository commandeClientRepository;
    private final ClientRepository clientRepository;
    private final ArticleRepository articleRepository;
    private final LigneCommandeClientRepository ligneCommandeClientRepository;
    private final MvtStkService mvtStkService;
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

    @Override
    @Transactional
    public CommandeClientDto save(CommandeClientDto dto) {
        log.debug("Enregistrement de la commande client : {}", dto);

        if (dto == null) {
            throw new IllegalArgumentException("L'objet CommandeClientDto ne peut pas être null");
        }

        List<String> errors = CommandeClientValidator.validate(dto);
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("Commande client non valide", ErrorCodes.COMMANDE_CLIENT_NOT_VALID, errors);
        }

        if (dto.getId() != null && isCommandeLivree(dto.getId())) {
            throw new InvalidOperationException("Impossible de modifier une commande déjà livrée", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        validateClientExistence(dto.getClient().getId());

        if (dto.getEtatCommande() == null) {
            dto.setEtatCommande(EtatCommande.EN_PREPARATION);
        }

        CommandeClient savedCommande = commandeClientRepository.save(CommandeClientDto.toEntity(dto));

        if (dto.getLigneCommandeClients() != null) {
            dto.getLigneCommandeClients().forEach(ligneDto -> {
                validateArticleExistence(ligneDto.getIdArticle());

                LigneCommandeClient ligne = LigneCommandeClientDto.toEntity(ligneDto);
                ligne.setCommandeClient(savedCommande);
                ligneCommandeClientRepository.save(ligne);
            });
        }

        return CommandeClientDto.fromEntity(savedCommande);
    }



    private void checkIdCommande(Long idCommande){
        if(idCommande== null){
            log.error("Commande Client ID is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec in ID null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private void checkIdArticle(Long idArticle){
        if(idArticle== null){
            log.error("L'Id de l'article est null");
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande avec un nouvel ID article null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private void checkCommandeLivree(CommandeClientDto commandeClient){
        if (isCommandeLivree(commandeClient.getId())){
            throw new InvalidOperationException("Impossible de modifier la commade client lorqu'elle est livrée",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
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
        checkIdArticle(idArticle);

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

    @Override
    @Transactional
    public ResponseEntity<?> validerCommande(Long id) {
        CommandeClient commande = commandeClientRepository.findByIdWithLignes(id)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));
        System.out.print("Commande : " + commande);
        commande.setEtatCommande(EtatCommande.EN_PREPARATION);

        if (commande.getEtatCommande() == EtatCommande.LIVREE) {
            throw new InvalidOperationException("Commande déjà livrée");
        }

        commande.setEtatCommande(EtatCommande.LIVREE);
        try{
        commandeClientRepository.save(commande);
        } catch (Exception e) {
            throw new RuntimeException("Save commande client " +e);
        }

            System.out.println("Lignes : " + commande.getLigneCommandeClients());
        // Mise à jour du stock
        try{
        commande.getLigneCommandeClients().forEach(ligne -> {
            Article article = articleRepository.findById(ligne != null ? ligne.getArticle().getId() : 0)
                    .orElseThrow(() -> new EntityNotFoundException("Article introuvable"));
            article.setQuantiteStock(article.getQuantiteStock().subtract(ligne.getQuantite()));
            articleRepository.save(article);
        });
        } catch (Exception e) {
           System.out.print("Une erreur est survenue : " + e);
        }


        return ResponseEntity.ok(CommandeClientDto.fromEntity(commande)) ;
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

    private void validateClientExistence(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new EntityNotFoundException(
                    "Aucun client avec l'ID = " + clientId + " n'a été trouvé dans la BD",
                    ErrorCodes.CLIENT_NOT_FOUND
            );
        }
    }

    private void validateArticleExistence(Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw new EntityNotFoundException(
                    "Aucun article avec l'ID = " + articleId + " n'a été trouvé dans la BD",
                    ErrorCodes.ARTICLE_NOT_FOUND
            );
        }
    }

    private boolean isCommandeLivree(Long idCommande) {
        return commandeClientRepository.findById(idCommande)
                .map(cmd -> EtatCommande.LIVREE.equals(cmd.getEtatCommande()))
                .orElse(false);
    }

}
