package com.mystock.mygestock.service.Impl;



import com.mystock.mygestock.dto.ChangerPasswordUtilisateur;
import com.mystock.mygestock.dto.RolesDto;
import com.mystock.mygestock.dto.UtilisateurDto;
import com.mystock.mygestock.exception.EntityNotFoundException;
import com.mystock.mygestock.exception.ErrorCodes;
import com.mystock.mygestock.exception.InvalidEntityException;
import com.mystock.mygestock.exception.InvalidOperationException;
import com.mystock.mygestock.model.Roles;
import com.mystock.mygestock.model.Utilisateur;
import com.mystock.mygestock.repository.UtilisateurRepository;
import com.mystock.mygestock.service.UtilisateurService;
import com.mystock.mygestock.validator.UtilisateurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override

    public UtilisateurDto save(UtilisateurDto dto) {
        List<String> errors = UtilisateurValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Utilisateur is not valid {}", dto);
            throw new InvalidEntityException("L'utilisateur n'est pas valide", ErrorCodes.UTILISATEUR_NOT_VALID, errors);
        }

        Utilisateur utilisateur = UtilisateurDto.toEntity(dto);
        String encodedPassword = passwordEncodere().encode(utilisateur.getPassword());
        utilisateur.setPassword(encodedPassword);

        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
        return UtilisateurDto.fromEntity(savedUtilisateur);
    }

    @Bean
    public PasswordEncoder passwordEncodere() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public List<UtilisateurDto> findAll() {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        return utilisateurs.stream()
                .map(UtilisateurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UtilisateurDto findById(Long id) {

       Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'ID = " + id + " n'a été trouvé dans la base",
                        ErrorCodes.CLIENT_NOT_FOUND));
        return UtilisateurDto.fromEntity(utilisateur);
    }

    @Override
    public UtilisateurDto changePassword(ChangerPasswordUtilisateur dto) {

         validate(dto);
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(dto.getId());
        if (utilisateurOptional.isEmpty()){
            log.warn("Aucun utilisateur n'a été trouvé avec l'ID "+dto.getId(),
                    ErrorCodes.UTILISATEUR_NOT_FOUND);
        }

        Utilisateur utilisateur = utilisateurOptional.get();
        utilisateur.setPassword(dto.getPassword());

        return UtilisateurDto.fromEntity(
                utilisateurRepository.save(utilisateur)
        );

    }

    @Override
    public void delete(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'ID = " + id + " n'a été trouvé dans la base",
                        ErrorCodes.CLIENT_NOT_FOUND));
        utilisateurRepository.delete(utilisateur);
    }

    public UtilisateurDto findByEmail(String email){
        return utilisateurRepository.findByEmail(email)
                .map(UtilisateurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'email " + email + "n'a été trouvé dans la BDD",
                        ErrorCodes.UTILISATEUR_NOT_FOUND)
                );
    }

    private void validate(ChangerPasswordUtilisateur dto){
        if (dto == null){
            log.warn("Impossible de changer le mot de passe avec un objet null");
            throw new InvalidOperationException(
                    "Aucune information n'a été fourni pour pouvoir modifier le mot de passe",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
        }

        if (dto.getId() == null){
            log.warn("Impossible de changer le mot de passe avec un ID null");
            throw new InvalidOperationException(
                    "Aucune information n'a été fourni pour pouvoir modifier le mot de passe",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
        }

        if (!StringUtils.hasLength(dto.getPassword()) || !StringUtils.hasLength(dto.getNewPassword()) ) {
            log.warn("Impossible de changer le mot de passe avec un mot de passe null");
            throw new InvalidOperationException(
                    "Aucune information n'a été fourni pour pouvoir modifier le mot de passe",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
        }

        if (!dto.getPassword().equals(dto.getNewPassword()) ) {
            log.warn("Impossible de changer le mot de passe avec deux mots de passe différent");
            throw new InvalidOperationException(
                    "Les deux champs de mot de passe ne correspondent pas : Impossible de modifier le mot de passe",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
        }
    }
}
