package com.devsu.bankflowapi.services.implementation;

import com.devsu.bankflowapi.controllers.dto.ClientDTO;
import com.devsu.bankflowapi.models.Client;
import com.devsu.bankflowapi.models.dto.ClientResponseDTO;
import com.devsu.bankflowapi.repositories.IClientRepository;
import com.devsu.bankflowapi.services.IClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    /**
     * Crea un nuevo cliente en la base de datos.
     *
     * @param clientDTO Datos del cliente a crear.
     * @return DTO de respuesta que contiene los detalles del cliente creado.
     */
    @Override
    public ClientResponseDTO createClient(ClientDTO clientDTO) {
        log.info("[ClientServiceImpl] - [createClient] - Creando nuevo cliente: {}", clientDTO.getName());

        // Convierte el DTO a entidad y guarda el cliente en la base de datos.
        Client client = convertDtoToEntity(clientDTO);
        client = clientRepository.save(client);

        log.info("[ClientServiceImpl] - [createClient] - Cliente creado con éxito, ID: {}", client.getId());

        // Convierte la entidad a DTO de respuesta y lo devuelve.
        return convertEntityToResponseDto(client);
    }

    // Convierte el DTO a entidad Client.
    private Client convertDtoToEntity(ClientDTO clientDTO) {
        log.debug("[ClientServiceImpl] - [convertDtoToEntity] - Convirtiendo DTO a entidad Client");

        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setAddress(clientDTO.getAddress());
        client.setPhone(clientDTO.getPhone());
        client.setPassword(clientDTO.getPassword());
        client.setStatus(clientDTO.getStatus());

        log.debug("[ClientServiceImpl] - [convertDtoToEntity] - Conversión de DTO a entidad Client completada");

        return client;
    }

    // Convierte la entidad Client a DTO de respuesta.
    private ClientResponseDTO convertEntityToResponseDto(Client client) {
        log.debug("[ClientServiceImpl] - [convertEntityToResponseDto] - Convirtiendo entidad a DTO de respuesta");

        ClientResponseDTO responseDTO = new ClientResponseDTO();
        responseDTO.setId(client.getId());
        responseDTO.setName(client.getName());
        responseDTO.setAddress(client.getAddress());
        responseDTO.setPhone(client.getPhone());
        responseDTO.setPassword(client.getPassword());
        responseDTO.setStatus(client.getStatus());

        log.debug("[ClientServiceImpl] - [convertEntityToResponseDto] - Conversión de entidad a DTO de respuesta completada");

        return responseDTO;
    }
}
