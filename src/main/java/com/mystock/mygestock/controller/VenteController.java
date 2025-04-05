package com.mystock.mygestock.controller;

import com.mystock.mygestock.controller.api.VenteApi;
import com.mystock.mygestock.dto.VenteDto;
import com.mystock.mygestock.service.VenteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vente")
public class VenteController implements VenteApi {
    private final VenteService venteService ;

    public VenteController(VenteService venteService) {
        this.venteService = venteService;
    }

    @Override
    public VenteDto save(VenteDto dto) {
        return venteService.save(dto);
    }

    @Override
    public List<VenteDto> findAll() {
        return venteService.findAll();
    }

    @Override
    public VenteDto findById(Long id) {
        return venteService.findById(id);
    }

    @Override
    public void delete(Long id) {
        venteService.delete(id);
    }
}
