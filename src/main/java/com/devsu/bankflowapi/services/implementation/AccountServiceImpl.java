package com.devsu.bankflowapi.services.implementation;

import com.devsu.bankflowapi.controllers.dto.AccountDTO;
import com.devsu.bankflowapi.models.Account;
import com.devsu.bankflowapi.models.Client;
import com.devsu.bankflowapi.models.dto.AccountResponseDTO;
import com.devsu.bankflowapi.repositories.IAccountRepository;
import com.devsu.bankflowapi.repositories.IClientRepository;
import com.devsu.bankflowapi.services.IAccountService;
import com.devsu.bankflowapi.services.exeptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository accountRepository;
    private final IClientRepository clientRepository;

    /**
     * Crea una nueva cuenta utilizando la información proporcionada en el DTO.
     *
     * @param accountDTO DTO con la información de la cuenta a crear.
     * @return DTO que contiene la información de la cuenta recién creada.
     */
    @Override
    public AccountResponseDTO createAccount(AccountDTO accountDTO) {
        Account account = convertDtoToEntity(accountDTO);
        log.info("[AccountServiceImpl]-[createAccount]-Comenzando la creación de cuenta...");
        account = accountRepository.save(account);
        log.info("[AccountServiceImpl]-[createAccount]-Cuenta creada exitosamente");
        return convertEntityToResponseDto(account);
    }

    /**
     * Obtiene la información de una cuenta por su identificador.
     *
     * @param accountId Identificador de la cuenta.
     * @return DTO que contiene la información de la cuenta encontrada.
     * @throws IllegalArgumentException si la cuenta no se encuentra.
     */
    @Override
    public AccountResponseDTO getAccountById(Long accountId) {
        log.info("[AccountServiceImpl]-[getAccountById]-Obteniendo información de la cuenta con ID: {}", accountId);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("[AccountServiceImpl]-[getAccountById]-Cuenta con ID {} no encontrada", accountId);
                    return new IllegalArgumentException("Cuenta no encontrada");
                });
        log.info("[AccountServiceImpl]-[getAccountById]-Información de la cuenta con ID {} obtenida exitosamente", accountId);
        return convertEntityToResponseDto(account);
    }


    /**
     * Actualiza la información de una cuenta existente.
     *
     * @param accountId  Identificador de la cuenta a actualizar.
     * @param accountDTO DTO con la nueva información de la cuenta.
     * @return DTO que contiene la información actualizada de la cuenta.
     * @throws NotFoundException si la cuenta no se encuentra.
     */
    @Override
    public AccountResponseDTO updateAccount(Long accountId, AccountDTO accountDTO) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
        updateAccountFromDTO(existingAccount, accountDTO);
        existingAccount = accountRepository.save(existingAccount);
        log.debug("[AccountServiceImpl]-[updateAccount]-Cuenta actualizada exitosamente");
        return convertEntityToResponseDto(existingAccount);
    }

    /**
     * Elimina una cuenta por su identificador.
     *
     * @param accountId Identificador de la cuenta a eliminar.
     * @throws NotFoundException si la cuenta no se encuentra.
     */
    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
        accountRepository.delete(account);
        log.info("[AccountServiceImpl]-[deleteAccount]-Cuenta eliminada exitosamente");
    }

    /**
     * Convierte un DTO de cuenta en una entidad de cuenta.
     *
     * @param accountDTO DTO de cuenta a convertir.
     * @return Entidad de cuenta convertida.
     */
    private Account convertDtoToEntity(AccountDTO accountDTO) {
        log.debug("[AccountServiceImpl]-[convertDtoToEntity]-Convirtiendo DTO a entidad...");
        Account account = new Account();
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setAccountType(accountDTO.getAccountType());
        account.setInitialBalance(accountDTO.getInitialBalance());
        account.setStatus(accountDTO.getStatus());

        if(accountDTO.getClientId() != null) {
            Client client = clientRepository.findById(accountDTO.getClientId()).orElse(null);
            account.setClient(client);
        }

        return account;
    }

    /**
     * Convierte una entidad de cuenta en un DTO de respuesta de cuenta.
     *
     * @param account Entidad de cuenta a convertir.
     * @return DTO de respuesta de cuenta convertido.
     */
    private AccountResponseDTO convertEntityToResponseDto(Account account) {
        log.debug("[AccountServiceImpl]-[convertEntityToResponseDto]-Convirtiendo entidad a DTO...");
        AccountResponseDTO responseDTO = new AccountResponseDTO();
        responseDTO.setId(account.getId());
        responseDTO.setAccountNumber(account.getAccountNumber());
        responseDTO.setAccountType(account.getAccountType());
        responseDTO.setInitialBalance(account.getInitialBalance());
        responseDTO.setStatus(account.getStatus());
        responseDTO.setClient(account.getClient());
        return responseDTO;
    }

    /**
     * Actualiza los campos de una entidad de cuenta a partir de un DTO de cuenta.
     *
     * @param account     Entidad de cuenta a actualizar.
     * @param accountDTO  DTO de cuenta con los campos actualizados.
     */
    private void updateAccountFromDTO(Account account, AccountDTO accountDTO) {
        log.debug("[AccountServiceImpl]-[updateAccountFromDTO]-Actualizando cuenta desde DTO...");
        if (accountDTO.getAccountNumber() != null && accountDTO.getAccountNumber() != "") {
            account.setAccountNumber(accountDTO.getAccountNumber());
        }
        if (accountDTO.getAccountType() != null && accountDTO.getAccountType() != "") {
            account.setAccountType(accountDTO.getAccountType());
        }
        if (accountDTO.getInitialBalance() != null) {
            account.setInitialBalance(accountDTO.getInitialBalance());
        }
        if (accountDTO.getStatus() != null) {
            account.setStatus(accountDTO.getStatus());
        }
        if(accountDTO.getClientId() != null) {
            Client client = clientRepository.findById(accountDTO.getClientId()).orElse(null);
            account.setClient(client);
        }
    }

}
