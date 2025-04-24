package com.mystock.mygestock.service.Impl;

import com.mystock.mygestock.dto.*;
import com.mystock.mygestock.dto.CommandeFournisseurDto;
import com.mystock.mygestock.exception.EntityNotFoundException;
import com.mystock.mygestock.exception.ErrorCodes;
import com.mystock.mygestock.exception.InvalidEntityException;
import com.mystock.mygestock.exception.InvalidOperationException;
import com.mystock.mygestock.entity.*;
import com.mystock.mygestock.repository.ArticleRepository;
import com.mystock.mygestock.repository.CommandeFournisseurRepository;
import com.mystock.mygestock.repository.FournisseurRepository;
import com.mystock.mygestock.repository.LigneCommandeFourniseurRepository;
import com.mystock.mygestock.service.CommandeFournisseurService;
import com.mystock.mygestock.service.MvtStkService;
import com.mystock.mygestock.validator.ArticleValidator;
import com.mystock.mygestock.validator.CommandeFournisseurValidator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandeFournisseurServiceImpl.class);
    private final CommandeFournisseurRepository commandeFournisseurRepository;
    private final FournisseurRepository fournisseurRepository;
    private final LigneCommandeFourniseurRepository ligneCommandeFournisseurRepository;
    private final ArticleRepository articleRepository;
    private final MvtStkService mvtStkService;
    @Autowired
    public CommandeFournisseurServiceImpl(CommandeFournisseurRepository commandeFournisseurRepository, FournisseurRepository fournisseurRepository,
                                          LigneCommandeFourniseurRepository ligneCommandeFournisseurRepository, ArticleRepository articleRepository, MvtStkService mvtStkService) {
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
        this.articleRepository = articleRepository;
        this.mvtStkService = mvtStkService;
    }

    private void checkIdCommande(Long idCommande){
        if(idCommande== null){
            log.error("Commande Fournisseur ID is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec in ID null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE);
        }
    }

    private void checkIdArticle(Long idArticle, String msg){
        if(idArticle== null){
            log.error("L'Id " +msg+ " de l'article est null");
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande avec un " + msg+ " ID article null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE);
        }
    }

    private CommandeFournisseurDto checkCommandeLivree(CommandeFournisseurDto commandeFournisseur){
        if (commandeFournisseur.isCommandeLivree()){
            throw new InvalidOperationException("Impossible de modifier la commade fournisseur lorqu'elle est livrée",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE);
        }
        return commandeFournisseur;
    }

    private void checkIdLigneCommande(Long idLigneCommande){
        if(idLigneCommande == null){
            log.error("LigneCommande ID is null");
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande avec une ligne de commande null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE);
        }
    }

    private Optional<LigneCommandeFournisseur> findLigneCommandeFournisseur(Long idLigneCommande) {
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = ligneCommandeFournisseurRepository.findById(idLigneCommande);

        if (ligneCommandeFournisseurOptional.isEmpty()){
            throw new EntityNotFoundException(
                    "Aucune commande fournisseur avec l'ID = " + idLigneCommande + " n'a été trouvée dans la BD",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID);
        }
        return ligneCommandeFournisseurOptional;

    }

    @Override
    public CommandeFournisseurDto save(CommandeFournisseurDto dto) {
        log.debug("Saving Commande fournisseur: " + dto);

        List<String> errors = CommandeFournisseurValidator.validate(dto);
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("La commande fournisseur n'est pas valide", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID, errors);
        }

        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(dto.getFournisseur().getId());
        if (fournisseur.isEmpty()) {
            log.error("Commande fournisseur is not valid {}", dto);

            throw new EntityNotFoundException("Aucun fournisseur avec l'ID = " + dto.getFournisseur().getId() + " n'a été trouvé dans la BD", ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }

        CommandeFournisseur savedCommandeFournisseur = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(dto));

        dto.getLigneCommandeFournisseurs().forEach(ligneCmd -> {
            Optional<Article> article = articleRepository.findById(ligneCmd.getArticle().getId());
            if (article.isEmpty()) {
                log.error("Article is null");

                throw new EntityNotFoundException("Aucun article avec l'ID = " + ligneCmd.getArticle().getId() + " n'a été trouvé dans la BD", ErrorCodes.ARTICLE_NOT_FOUND);
            }
            LigneCommandeFournisseur ligneCommandeFournisseur = ligneCmd.toEntity(ligneCmd);
            ligneCommandeFournisseur.setCommandeFournisseur(savedCommandeFournisseur);
            ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);

            // mise à jour du stock
       //     Article articleToUpdate = article.get();
       //     articleToUpdate.setQuantite(articleToUpdate.getQuantite() + ligneCmd.getQuantite());
       //     articleRepository.save(articleToUpdate);
        });

        return CommandeFournisseurDto.fromEntity(savedCommandeFournisseur);
    }

    @Override
    public CommandeFournisseurDto updateEtatCommande(Long idCommande, EtatCommande etatCommande) {
        checkIdCommande(idCommande);

        if(!StringUtils.hasLength(String.valueOf(etatCommande))){
            log.error("Commande state is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un ID null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseur = findById(idCommande);
        checkCommandeLivree(commandeFournisseur);
        commandeFournisseur.setEtatCommande(etatCommande);

        CommandeFournisseur savedCommande = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseur));
        if (commandeFournisseur.isCommandeLivree()){
            updateMvtStk(idCommande);
        }
        return CommandeFournisseurDto.fromEntity(savedCommande);
    }

    @Override
    public CommandeFournisseurDto updateQuantiteCommande(Long idCommande, Long idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);

        checkIdLigneCommande(idLigneCommande);

        if(quantite == null || quantite.compareTo(BigDecimal.ZERO) ==0){
            log.error("Quantite ID is null");
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande avec une quantite null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseur = findById(idCommande);

        checkCommandeLivree(commandeFournisseur);

        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional =
                findLigneCommandeFournisseur(idLigneCommande);
        LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurOptional.get();
        ligneCommandeFournisseur.setQuantite(quantite);
        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);

        return commandeFournisseur;
    }

    @Override
    public CommandeFournisseurDto updateFournisseur(Long idCommande, Long idFournisseur) {
        checkIdCommande(idCommande);

        if(idFournisseur== null){
            log.error("Commande ID is null");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un ID fournisseur null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseur = findById(idCommande);

        checkCommandeLivree(commandeFournisseur);

        Optional<Fournisseur> fournisseurOptional = fournisseurRepository.findById(idFournisseur);
        if (fournisseurOptional.isEmpty()){
            throw new EntityNotFoundException("Aucun fournisseur avec l'ID = "+ idFournisseur+ " n'a été trouve dans la base");
        }

        commandeFournisseur.setFournisseur(FournisseurDto.fromEntity(fournisseurOptional.get()));

        return CommandeFournisseurDto.fromEntity(
                commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseur))
        );
    }

    @Override
    public CommandeFournisseurDto updateArticle(Long idCommande, Long idLigneCommande, Long idArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(idArticle,"nouvel");

        CommandeFournisseurDto commandeFournisseur = findById(idCommande);

        checkCommandeLivree(commandeFournisseur);

        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional =
                findLigneCommandeFournisseur(idLigneCommande);

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

        LigneCommandeFournisseur ligneCommandeFournisseurToSaved = ligneCommandeFournisseurOptional.get();

        ligneCommandeFournisseurToSaved.setArticle(articleOptional.get());

        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseurToSaved);

        return commandeFournisseur;
    }

    @Override
    public CommandeFournisseurDto findById(Long id) {
        if (id == null) {
            log.error("Commande fournisseur ID is null");
            return null;
        }

        return commandeFournisseurRepository.findById(id).map(CommandeFournisseurDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException("Aucune commande fournisseur avec l'ID = " + id + " n'a été trouvée dans la BD", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND)
        );
    }

    @Override
    public List<LigneCommandeFournisseurDto> findAllLigneCommandeFournisseurByCommandeFournisseurId(Long idCommande) {
        return ligneCommandeFournisseurRepository.
                findAllByCommandeFournisseurId(idCommande).stream()
                .map(LigneCommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList())

                ;
    }

    @Override
    public CommandeFournisseurDto deleteArticle(Long idCommande, Long idLigneCommande) {

        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CommandeFournisseurDto commandeFournisseur = findById(idCommande);
        checkCommandeLivree(commandeFournisseur);

        // Just to check the ligneCommandeFournisseur and inform the client in case it is absent

        findLigneCommandeFournisseur(idLigneCommande);
        ligneCommandeFournisseurRepository.deleteById(idLigneCommande);


        return null;
    }


    @Override
    public List<CommandeFournisseurDto> findAll() {
        return commandeFournisseurRepository.findAll().stream()
                .map(CommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            log.error(" ID Commande fournisseur id null");
            return;
        }
        List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(id);
        if (!ligneCommandeFournisseurs.isEmpty()){
            throw new InvalidOperationException(
                    "Impossible de supprimer une commande fournisseur déjà utilisé",
                    ErrorCodes.COMMANDE_FOURNISSEUR_ALREADY_IN_USE);
        }

        commandeFournisseurRepository.deleteById(id);
    }

    private void updateMvtStk(Long idCommande){
        List<LigneCommandeFournisseur> ligneCommandeClients = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande);
        ligneCommandeClients.forEach(lig -> {
            MvtStkDto mvtStkDto = MvtStkDto.builder()
                    .article(ArticleDto.fromEntity(lig.getArticle()))
                    .dateMvt(Instant.now())
                    .typeMvtStk(TypeMvtStk.SORTIE)
                    .sourceMvtStk(SourceMvtStk.COMMANDE_FOURNISSEUR)
                    .quantite(lig.getQuantite())
                    .build();
            mvtStkService.entreeStock(mvtStkDto);
        });
    }

    }
