package com.mystock.mygestock.service.Impl;


import com.mystock.mygestock.dto.ArticleDto;
import com.mystock.mygestock.dto.MvtStkDto;
import com.mystock.mygestock.dto.VenteDto;
import com.mystock.mygestock.exception.EntityNotFoundException;
import com.mystock.mygestock.exception.ErrorCodes;
import com.mystock.mygestock.exception.InvalidEntityException;
import com.mystock.mygestock.exception.InvalidOperationException;
import com.mystock.mygestock.model.*;
import com.mystock.mygestock.repository.ArticleRepository;
import com.mystock.mygestock.repository.LigneVenteRepository;
import com.mystock.mygestock.repository.VenteRepository;
import com.mystock.mygestock.service.MvtStkService;
import com.mystock.mygestock.service.VenteService;
import com.mystock.mygestock.validator.VenteValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VenteServiceImpl implements VenteService {
    private VenteRepository venteRepository;
    private ArticleRepository articleRepository;
    private LigneVenteRepository ligneVenteRepository;
    private final MvtStkService mvtStkService;

    public VenteServiceImpl(VenteRepository venteRepository,
                            ArticleRepository articleRepository,
                            LigneVenteRepository ligneVenteRepository, MvtStkService mvtStkService) {
        this.venteRepository = venteRepository;
        this.articleRepository = articleRepository;
        this.ligneVenteRepository = ligneVenteRepository;
        this.mvtStkService = mvtStkService;
    }

    @Override
    public VenteDto save(VenteDto dto) {
        List<String> errors = VenteValidator.validate(dto);
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("La vente n'est pas valide", ErrorCodes.VENTE_NOT_VALID, errors);
        }

        Vente savedVente = venteRepository.save(VenteDto.toEntity(dto));



        dto.getLigneVentes().forEach(ligneVenteDto -> {
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
            if (article.isEmpty()) {
                throw new EntityNotFoundException("Aucun article avec l'ID = " + ligneVenteDto.getArticle().getId() + " n'a été trouvé dans la BD", ErrorCodes.ARTICLE_NOT_FOUND);
            }
            LigneVente ligneVenteToSave = ligneVenteDto.toEntity(ligneVenteDto);
            ligneVenteToSave.setVente(savedVente);
            ligneVenteRepository.save(ligneVenteToSave);
            updateMvtStk(ligneVenteToSave);

            // mise à jour du stock
        //    Article articleToUpdate = article.get();
        //    articleToUpdate.setQuantite(articleToUpdate.getQuantite() - ligneVente.getQuantite());
       //     articleRepository.save(articleToUpdate);
        });

        return VenteDto.fromEntity(savedVente);
    }
    @Override
    public List<VenteDto> findAll() {
        return venteRepository.findAll().stream()
                .map(VenteDto::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    public VenteDto findById(Long id) {
        if (id == null) {
            log.error("Vente ID is null");
            return null;
        }

        return venteRepository.findById(id).map(VenteDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException("Aucune vente avec l'ID = " + id + " n'a été trouvée dans la BD", ErrorCodes.VENTE_NOT_FOUND)
        );
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            log.error("Vente ID is null");
            return;
        }
        List<LigneVente> ventes = ligneVenteRepository.findAllByVenteId(id);
        if (!ventes.isEmpty()){
            throw new InvalidOperationException(
                    "Impossible de supprimer une vente ",
                    ErrorCodes.VENTE_ALREADY_IN_USE);
        }
        venteRepository.deleteById(id);

    }

    private void updateMvtStk(LigneVente lig){

            MvtStkDto mvtStkDto = MvtStkDto.builder()
                    .article(ArticleDto.fromEntity(lig.getArticle()))
                    .dateMvt(Instant.now())
                    .typeMvtStk(TypeMvtStk.SORTIE)
                    .sourceMvtStk(SourceMvtStk.VENTE)
                    .quantite(lig.getQuantite())
                    .build();
            mvtStkService.sortieStock(mvtStkDto);

    }

}
