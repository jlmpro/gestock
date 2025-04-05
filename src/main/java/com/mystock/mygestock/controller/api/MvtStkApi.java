package com.mystock.mygestock.controller.api;

import com.mystock.mygestock.dto.MvtStkDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Mouvement du stock")
public interface MvtStkApi {
    @GetMapping("/stockreel/{idArticle}")
    BigDecimal stockReelArticle(@PathVariable Long idArticle);
    @GetMapping("/filter/article/{idArticle}")
    List<MvtStkDto> mvtStkArticle(@PathVariable Long idArticle);
    @PostMapping("/entree")
    MvtStkDto entreeStock(@RequestBody MvtStkDto dto);
    @PostMapping("/sortie")
    MvtStkDto sortieStock(@RequestBody MvtStkDto dto);
    @PostMapping("correctionpos")
    MvtStkDto correctionStockNeg(@RequestBody MvtStkDto dto);
    @PostMapping("correctioneg")
    MvtStkDto correctionStockPos(@RequestBody MvtStkDto dto);

}
