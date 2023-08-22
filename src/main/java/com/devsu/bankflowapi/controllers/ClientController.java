package com.devsu.bankflowapi.controllers;

import com.devsu.bankflowapi.controllers.dto.ClientDTO;
import com.devsu.bankflowapi.models.dto.ClientResponseDTO;
import com.devsu.bankflowapi.services.IClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Clase donde se implementa el servicio REST para clientes
 */
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final IClientService clientService;

    /**
     * Crea un nuevo cliente.
     *
     * @param clientDTO Datos del cliente a crear.
     * @return ResponseEntity con el cliente creado y estado HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody ClientDTO clientDTO) {
        log.info("[ClientController] - [createClient] - Creando nuevo cliente...");

        ClientResponseDTO createdClient = clientService.createClient(clientDTO);

        log.info("[ClientController] - [createClient] - Nuevo cliente creado: {}", createdClient);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

}
