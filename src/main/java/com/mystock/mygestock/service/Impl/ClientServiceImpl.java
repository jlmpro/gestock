package com.mystock.mygestock.service.Impl;


import com.mystock.mygestock.controller.ClientController;
import com.mystock.mygestock.dto.ClientDto;
import com.mystock.mygestock.exception.EntityNotFoundException;
import com.mystock.mygestock.exception.ErrorCodes;
import com.mystock.mygestock.exception.InvalidEntityException;
import com.mystock.mygestock.exception.InvalidOperationException;
import com.mystock.mygestock.model.Client;
import com.mystock.mygestock.model.CommandeClient;
import com.mystock.mygestock.repository.ClientRepository;
import com.mystock.mygestock.repository.CommandeClientRepository;
import com.mystock.mygestock.service.ClientService;
import com.mystock.mygestock.validator.ClientValidator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

   private final CommandeClientRepository commandeClientRepository;
    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, CommandeClientRepository commandeClientRepository) {
        this.clientRepository = clientRepository;
        this.commandeClientRepository = commandeClientRepository;
    }
    @Override
    public ClientDto save(ClientDto dto) {
        LOGGER.debug("Requête pour enregistrer un client : {}", dto);

        List<String> errors = ClientValidator.validate(dto);
        if (!errors.isEmpty()){
            throw new InvalidEntityException(errors.toString(), ErrorCodes.CLIENT_NOT_VALID,errors);
        }
        Client client = ClientDto.toEntity(dto);
        Client savedClient = clientRepository.save(client);
        LOGGER.debug("Client enregistré avec succès : {}", savedClient);

        return ClientDto.fromEntity(savedClient);
    }

    @Override
    public List<ClientDto> findAll() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(ClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDto findById(Long id) {

            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Aucun client avec l'ID = " + id + " n'a été trouvé dans la base",
                            ErrorCodes.CLIENT_NOT_FOUND));
            return ClientDto.fromEntity(client);
        }



    @Override
    public void delete(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun client avec l'ID = " + id + " n'a été trouvé dans la base",
                        ErrorCodes.CLIENT_NOT_FOUND));
        List<CommandeClient> commandeClients = commandeClientRepository.findAllByClientId(id);
        if (!commandeClients.isEmpty()){
            throw new InvalidOperationException(
                    "Impossible de supprimer un client qui a des commandes",
                    ErrorCodes.CLIENT_ALREADY_IN_USE);
        }
        clientRepository.delete(client);


    }
}
