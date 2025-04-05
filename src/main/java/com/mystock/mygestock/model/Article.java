package com.mystock.mygestock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
public class Article extends AbstractEntity{
    @Column(name = "codearticle")
    private String codeArticle;
    @Column(name = "designation")
    private  String designation ;
    @Column(name = "prixunitaire")
    private BigDecimal prixUnitaire ;
    @Column(name = "tauxtva")
    private BigDecimal tauxTva ;
    @Column(name = "prixunitairettc")
    private BigDecimal prixUnitaireTtc ;
    @Column(name = "photo")
    private String photo ;
    @ManyToOne
    @JoinColumn(name = "idcategory")
    private Category category ;

}
