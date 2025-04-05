package com.mystock.mygestock.service.Impl;

import com.mystock.mygestock.dto.MvtStkDto;
import com.mystock.mygestock.exception.ErrorCodes;
import com.mystock.mygestock.exception.InvalidEntityException;
import com.mystock.mygestock.model.TypeMvtStk;
import com.mystock.mygestock.repository.MvtStkRepository;
import com.mystock.mygestock.service.ArticleService;
import com.mystock.mygestock.service.MvtStkService;
import com.mystock.mygestock.validator.MvtStkValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MvtStkServiceImpl implements MvtStkService {

    private MvtStkRepository mvtStkRepository;
    private ArticleService articleService;
    @Autowired
    public MvtStkServiceImpl(MvtStkRepository mvtStkRepository, ArticleService articleService) {
        this.mvtStkRepository = mvtStkRepository;
        this.articleService = articleService;
    }

    @Override
    public BigDecimal stockReelArticle(Long idArticle) {

        if (idArticle == null){
            log.warn("ID aticle is null");
            return BigDecimal.valueOf(-1);
        }
        articleService.findById(idArticle);
        return mvtStkRepository.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStkDto> mvtStkArticle(Long idArticle) {
        return mvtStkRepository.findAllByArticleId(idArticle)
                .stream()
                .map(MvtStkDto::fromEntity)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public MvtStkDto entreeStock(MvtStkDto dto) {

     return entreePositive(dto,TypeMvtStk.ENTREE);
    }

    @Override
    public MvtStkDto sortieStock(MvtStkDto dto) {
      return sortieNegative(dto,TypeMvtStk.SORTIE);
    }

    @Override
    public MvtStkDto correctionStockPos(MvtStkDto dto) {
      return entreePositive(dto, TypeMvtStk.CORRECTION_POS);
    }

    @Override
    public MvtStkDto correctionStockNeg(MvtStkDto dto) {
     return sortieNegative(dto, TypeMvtStk.CORRECTION_NEG);
    }

    private MvtStkDto entreePositive(MvtStkDto dto, TypeMvtStk typeMvtStk){
        List<String> errors = MvtStkValidator.validate(dto);
        if (!errors.isEmpty()){
            log.error("Article is not valid {}", dto);
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID);
        }

        dto.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(dto.getQuantite().doubleValue())
                )
        );
        dto.setTypeMvtStk(typeMvtStk);
        return MvtStkDto.fromEntity(
                mvtStkRepository.save(MvtStkDto.toEntity(dto))
        );
    }

    private MvtStkDto sortieNegative(MvtStkDto dto, TypeMvtStk typeMvtStk){
        List<String> errors = MvtStkValidator.validate(dto);
        if (!errors.isEmpty()){
            log.error("Article is not valid {}", dto);
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID);
        }

        dto.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(dto.getQuantite().doubleValue()) * -1
                )
        );
        dto.setTypeMvtStk(typeMvtStk);
        return MvtStkDto.fromEntity(
                mvtStkRepository.save(MvtStkDto.toEntity(dto))
        );
    }
}
