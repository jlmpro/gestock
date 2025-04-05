package com.mystock.mygestock.dto;


import com.mystock.mygestock.model.MvtStk;
import com.mystock.mygestock.model.SourceMvtStk;
import com.mystock.mygestock.model.TypeMvtStk;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Data
public class MvtStkDto {
    private Long id ;
    private Instant dateMvt ;
    private BigDecimal quantite;
    private ArticleDto article;
    private TypeMvtStk typeMvtStk;
    private SourceMvtStk sourceMvtStk;

    public static MvtStkDto fromEntity(MvtStk mvtStk) {
        if (mvtStk == null) {
            return null;
        }
        return MvtStkDto.builder()
                .id(mvtStk.getId())
                .quantite(mvtStk.getQuantite())
                .article(ArticleDto.fromEntity(mvtStk.getArticle()))
                .typeMvtStk(mvtStk.getTypeMvtStk())
                .sourceMvtStk(mvtStk.getSourceMvtStk())
                .build();
    }
    public static MvtStk toEntity(MvtStkDto mvtStkDto){
        MvtStk mvtStk = new MvtStk();
        mvtStk.setId(mvtStkDto.getId());
        mvtStk.setQuantite(mvtStkDto.getQuantite());
        mvtStk.setTypeMvtStk(mvtStkDto.getTypeMvtStk());
        mvtStk.setSourceMvtStk(mvtStkDto.getSourceMvtStk());
        return mvtStk;
    }
}
