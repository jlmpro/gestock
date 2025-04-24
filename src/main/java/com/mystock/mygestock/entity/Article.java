package com.mystock.mygestock.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedBy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Article extends AbstractEntity{
    @Column(name = "CODE_ARTICLE")
    private String codeArticle;
    @Column(name = "DESIGNATION")
    private  String designation ;
    @Column(name = "PRIX_UNITAIRE")
    private BigDecimal prixUnitaire ;
    @Column(name = "TAUX_TVA")
    private BigDecimal tauxTva ;
    @Column(name = "PHOTO")
    private String photo ;
    @ManyToOne
    @JoinColumn(name = "ID_CATEGORY")
    private Category category ;
    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "UTILISATEUR_ID")
    private Utilisateur utilisateur;
    @Column(name = "TVA_APPLICABLE")
    private Boolean tvaApplicable ;
    @Column(name = "QUANTITE_STOCK")
    private BigDecimal quantiteStock;
    @Column(name = "ACTIF")
    private Boolean actif ;

    @Transient
    private BigDecimal prixUnitaireTtc ;




    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Article article = (Article) o;
        return getId() != null && Objects.equals(getId(), article.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public BigDecimal getPrixUnitaireTtc() {
        if (Boolean.TRUE.equals(this.tvaApplicable) && prixUnitaire != null && tauxTva != null) {
            BigDecimal tva = prixUnitaire.multiply(tauxTva)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            return prixUnitaire.add(tva);
        }
        return prixUnitaire != null ? prixUnitaire : BigDecimal.ZERO;
    }

}
