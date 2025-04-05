package com.mystock.mygestock.service;



import com.mystock.mygestock.dto.FournisseurDto;

import java.util.List;

public interface FournisseurService {

 FournisseurDto save(FournisseurDto dto);

 List<FournisseurDto> findAll();

 FournisseurDto findById(Long id );

 void delete(Long id);
}
