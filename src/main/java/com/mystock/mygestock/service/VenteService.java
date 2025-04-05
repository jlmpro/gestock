package com.mystock.mygestock.service;


import com.mystock.mygestock.dto.VenteDto;

import java.util.List;

public interface VenteService {
    List<VenteDto> findAll();

    VenteDto save(VenteDto dto);

    VenteDto findById(Long id);

    void delete(Long id);

}
