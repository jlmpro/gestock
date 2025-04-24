package com.mystock.mygestock.security.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mystock.mygestock.exception.EmailAlreadyExistsException;
import com.mystock.mygestock.entity.Roles;
import com.mystock.mygestock.entity.Utilisateur;
import com.mystock.mygestock.repository.RolesRepository;
import com.mystock.mygestock.repository.UtilisateurRepository;
import com.mystock.mygestock.security.config.JwtService;
import com.mystock.mygestock.security.token.Token;
import com.mystock.mygestock.security.token.TokenRepository;
import com.mystock.mygestock.security.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.List;

@Service
public class AuthenticationService {
    @Autowired
    private final UtilisateurRepository repository;
    @Autowired
    private final TokenRepository tokenRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final RolesRepository rolesRepository;
    @Autowired
    private final UtilisateurRepository utilisateurRepository;

    public AuthenticationService(UtilisateurRepository repository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, RolesRepository rolesRepository, UtilisateurRepository utilisateurRepository) {
        this.repository = repository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.rolesRepository = rolesRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        // Récupérer le rôle USER
        Roles userRole = rolesRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        boolean existEmail =  this.utilisateurRepository.existsByEmail(request.getEmail());

        if (existEmail){
            throw  new EmailAlreadyExistsException("Cet email est déjà utilisé !");
        }

        // Créer l'utilisateur
        var utilisateur = Utilisateur.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(userRole)) // Attribuer le rôle USER
                .build();

        // Sauvegarder l'utilisateur
        var savedUtilisateur = utilisateurRepository.save(utilisateur);

        // Générer les tokens JWT
        var jwtToken = jwtService.generateToken(utilisateur);
        var refreshToken = jwtService.generateRefreshToken(utilisateur);

        // Sauvegarder le token
        saveUtilisateurToken(savedUtilisateur, jwtToken);

        // Retourner la réponse
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e){
            e.printStackTrace();
            System.out.print("Authentification erreur " + e);
        }
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUtilisateurToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUtilisateurToken(Utilisateur utilisateur, String jwtToken) {
        var token = Token.builder()
                .utilisateur(utilisateur)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Utilisateur user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUtilisateur(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUtilisateurToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
