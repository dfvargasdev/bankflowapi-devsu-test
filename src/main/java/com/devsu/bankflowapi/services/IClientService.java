package com.devsu.bankflowapi.services;

import com.devsu.bankflowapi.controllers.dto.ClientDTO;
import com.devsu.bankflowapi.models.dto.ClientResponseDTO;

public interface IClientService {
    ClientResponseDTO createClient(ClientDTO clientDTO);
}
