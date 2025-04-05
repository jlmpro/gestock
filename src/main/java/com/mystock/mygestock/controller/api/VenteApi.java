package com.mystock.mygestock.controller.api;

import com.mystock.mygestock.dto.VenteDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vente", description = "Vente API")
public interface VenteApi {

    @PostMapping(value = "/save")
    VenteDto save(@RequestBody VenteDto dto);
    @GetMapping(value = "/all")
    List<VenteDto> findAll();
    @GetMapping(value = "/find/{id}")
    VenteDto findById(@PathVariable Long id);
    @DeleteMapping(value = "/delete/{id}")
    void delete(@PathVariable Long id);

}
