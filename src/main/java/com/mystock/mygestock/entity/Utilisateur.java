package com.mystock.mygestock.entity;

import com.mystock.mygestock.security.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@Entity
@Table(name = "utilisateur")
public class Utilisateur extends AbstractEntity implements UserDetails {
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "email")
    private String email;
    @Column(name = "datedenaissance")
    private String dateDeNaissance;
    @Column(name = "motdepasse")
    private String password;
    @Column(name = "adresse")
    private Adresse adresse;
    @Column(name = "photo")
    private String photo ;
    @OneToMany(mappedBy = "utilisateur")
    @ToString.Exclude
    private List<Token> tokens;

    @OneToMany(mappedBy = "utilisateur", fetch = FetchType.EAGER)
    private List<Roles> roles;

    public static class Builder {
        private String firstname;
        private String lastname;
        private String email;
        private String dateDeNaissance;
        private String password;
        private Adresse adresse;
        private String photo;
        private List<Token> tokens;
        private List<Roles> roles;

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder dateDeNaissance(String dateDeNaissance) {
            this.dateDeNaissance = dateDeNaissance;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder adresse(Adresse adresse) {
            this.adresse = adresse;
            return this;
        }

        public Builder photo(String photo) {
            this.photo = photo;
            return this;
        }

        public Builder tokens(List<Token> tokens) {
            this.tokens = tokens;
            return this;
        }

        public Builder roles(List<Roles> roles) {
            this.roles = roles;
            return this;
        }

        public Utilisateur build() {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.firstname = this.firstname;
            utilisateur.lastname = this.lastname;
            utilisateur.email = this.email;
            utilisateur.dateDeNaissance = this.dateDeNaissance;
            utilisateur.password = this.password;
            utilisateur.adresse = this.adresse;
            utilisateur.photo = this.photo;
            utilisateur.tokens = this.tokens;
            utilisateur.roles = this.roles;
            return utilisateur;
        }
    }

    // Méthode statique pour créer un builder
    public static Builder builder() {
        return new Builder();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(roles.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Utilisateur that = (Utilisateur) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(String dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", dateDeNaissance='" + dateDeNaissance + '\'' +
                ", password='" + password + '\'' +
                ", adresse=" + adresse +
                ", photo='" + photo + '\'' +
                ", nbTokens=" + (tokens != null ? tokens.size() : 0) +
                ", roles=" + roles +
                '}';
    }
}
