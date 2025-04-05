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
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "lignevente")
public class LigneVente extends AbstractEntity{
    @ManyToOne
    @JoinColumn(name = "idvente")
    private Vente vente ;
    @Column(name = "quantite")
    private BigDecimal quantite ;
    @Column(name = "prixunitaire")
    private BigDecimal prixUnitaire;
    @ManyToOne
    @JoinColumn(name = "idarticle")
    private Article article ;

}
