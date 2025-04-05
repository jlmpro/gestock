package com.mystock.mygestock.service;



import com.mystock.mygestock.dto.ClientDto;

import java.util.List;

public interface ClientService {

 ClientDto save(ClientDto dto);

 List<ClientDto> findAll();

 ClientDto findById(Long id );

 void delete(Long id);
}
