package com.mystock.mygestock.service;

import com.mystock.mygestock.dto.MvtStkDto;

import java.math.BigDecimal;
import java.util.List;

public interface MvtStkService {
    BigDecimal stockReelArticle(Long idArticle);
    List<MvtStkDto> mvtStkArticle(Long idArticle);
    MvtStkDto entreeStock(MvtStkDto dto);
    MvtStkDto sortieStock(MvtStkDto dto);
    MvtStkDto correctionStockPos(MvtStkDto dto);
    MvtStkDto correctionStockNeg(MvtStkDto dto);


}
