package com.mystock.mygestock.controller;

import com.mystock.mygestock.controller.api.ClientApi;
import com.mystock.mygestock.dto.ClientDto;
import com.mystock.mygestock.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/client")
public class ClientController implements ClientApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public ClientDto save(ClientDto dto) {
        return clientService.save(dto);
    }

    @Override
    public List<ClientDto> findAll() {
        return clientService.findAll();
    }

    @Override
    public ClientDto findById(Long id) {
        return clientService.findById(id);
    }

    @Override
    public void delete(Long id) {
        clientService.delete(id);
    }
}
