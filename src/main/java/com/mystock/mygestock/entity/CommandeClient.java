package com.mystock.mygestock.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Entity
@Table(name = "commandeclient")
public class CommandeClient extends AbstractEntity{

    @ToString.Include
    @Column(name = "code")
    private String code;

    @ToString.Include
    @Column(name = "dateCommande")
    private Instant dateCommande;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "etat_commande")
    private EtatCommande etatCommande;

    @ManyToOne
    @JoinColumn(name = "idclient")
    @ToString.Exclude // 🔥 très important pour éviter l'appel infini
    private Client client;

    @OneToMany(mappedBy = "commandeClient")
    @ToString.Exclude
    private List<LigneCommandeClient> ligneCommandeClients;
    @Override
    protected boolean canEqual(Object other) {
        return super.canEqual(other);
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        return "CommandeClient{" +
                ", code='" + code + '\'' +
                ", dateCommande=" + dateCommande +
                ", etatCommande=" + etatCommande +
                ", client=" + (client != null ? client.getNom() : "null") +
                ", lignes=" + (ligneCommandeClients != null ? ligneCommandeClients.size() : 0) +
                '}';
    }




    public CommandeClient(String code, Instant dateCommande, EtatCommande etatCommande, Client client, List<LigneCommandeClient> ligneCommandeClients) {
        this.code = code;
        this.dateCommande = dateCommande;
        this.etatCommande = etatCommande;
        this.client = client;
        this.ligneCommandeClients = ligneCommandeClients;
    }

    @Override
    public Instant getCreatedDate() {
        return super.getCreatedDate();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Instant dateCommande) {
        this.dateCommande = dateCommande;
    }

    public EtatCommande getEtatCommande() {
        return etatCommande;
    }

    public void setEtatCommande(EtatCommande etatCommande) {
        this.etatCommande = etatCommande;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<LigneCommandeClient> getLigneCommandeClients() {
        return ligneCommandeClients;
    }

    public void setLigneCommandeClients(List<LigneCommandeClient> ligneCommandeClients) {
        this.ligneCommandeClients = ligneCommandeClients;
    }

    @Override
    public Instant getLastModifiedDate() {
        return super.getLastModifiedDate();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public void setCreatedDate(Instant createdDate) {
        super.setCreatedDate(createdDate);
    }

    @Override
    public void setLastModifiedDate(Instant lastModifiedDate) {
        super.setLastModifiedDate(lastModifiedDate);
    }


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CommandeClient that = (CommandeClient) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
