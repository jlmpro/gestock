package com.mystock.mygestock.controller;

import com.mystock.mygestock.controller.api.FournisseurApi;
import com.mystock.mygestock.dto.FournisseurDto;
import com.mystock.mygestock.service.FournisseurService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fournisseur")
public class FournisseurController implements FournisseurApi {
    private final FournisseurService fournisseurService;

    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @Override
    public FournisseurDto save(FournisseurDto dto) {
        return fournisseurService.save(dto);
    }

    @Override
    public List<FournisseurDto> findAll() {
        return fournisseurService.findAll();
    }

    @Override
    public FournisseurDto findById(Long id) {
        return fournisseurService.findById(id);
    }

    @Override
    public void delete(Long id) {
        fournisseurService.delete(id);
    }
}
