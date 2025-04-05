package com.mystock.mygestock.controller.api;

import com.mystock.mygestock.dto.UtilisateurDto;
import com.mystock.mygestock.dto.UtilisateurDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Utilisateur", description = "Utilisateur API")
public interface UtilisateurApi {
    @PostMapping(value = "/save")
    UtilisateurDto save(@RequestBody UtilisateurDto dto);

    @GetMapping(value = "/all")
    List<UtilisateurDto> findAll();

    @GetMapping(value = "/find/{id}")
    UtilisateurDto findById(@PathVariable Long id);
    @GetMapping(value = "/find/email/{email}")
    UtilisateurDto findByEmail(@PathVariable String email);

    @DeleteMapping(value = "/delete/{id}")
    void delete(@PathVariable Long id);
}
