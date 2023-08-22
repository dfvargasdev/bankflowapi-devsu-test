package com.devsu.bankflowapi.controllers;

import com.devsu.bankflowapi.controllers.dto.AccountDTO;
import com.devsu.bankflowapi.models.dto.AccountResponseDTO;
import com.devsu.bankflowapi.services.IAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final IAccountService accountService;

    /**
     * Crea una nueva cuenta.
     *
     * @param accountDTO Datos de la cuenta a crear.
     * @return ResponseEntity con la cuenta creada y estado HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        log.info("[AccountController] - [createAccount] - Creando nueva cuenta...");

        // Llama al servicio para crear la cuenta y obtiene la respuesta.
        AccountResponseDTO createdAccount = accountService.createAccount(accountDTO);

        log.info("[AccountController] - [createAccount] - Nueva cuenta creada: {}", createdAccount);

        // Devuelve la respuesta con la cuenta creada y estado HTTP 201 (Created).
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    /**
     * Obtiene una cuenta por su ID.
     *
     * @param accountId ID de la cuenta a buscar.
     * @return ResponseEntity con la cuenta encontrada y estado HTTP 200 (OK), o estado HTTP 404 (Not Found) si la cuenta no existe.
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable Long accountId) {
        log.info("[AccountController] - [getAccountById] - Buscando cuenta por ID: {}", accountId);

        // Llama al servicio para obtener la cuenta por su ID.
        AccountResponseDTO accountResponseDTO = accountService.getAccountById(accountId);

        if (accountResponseDTO != null) {
            log.info("[AccountController] - [getAccountById] - Cuenta encontrada: {}", accountResponseDTO);
            // Devuelve la cuenta encontrada y estado HTTP 200 (OK).
            return ResponseEntity.ok(accountResponseDTO);
        } else {
            log.info("[AccountController] - [getAccountById] - Cuenta no encontrada para ID: {}", accountId);
            // Devuelve estado HTTP 404 (Not Found) si la cuenta no existe.
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Actualiza una cuenta por su ID.
     *
     * @param accountId  ID de la cuenta a actualizar.
     * @param accountDTO Objeto AccountDTO con los nuevos datos de la cuenta.
     * @return ResponseEntity con la cuenta actualizada y estado HTTP 200 (OK), o estado HTTP 404 (Not Found) si la cuenta no existe.
     */
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> updateAccount(@PathVariable Long accountId, @RequestBody AccountDTO accountDTO) {
        log.info("[AccountController] - [updateAccount] - Actualizando cuenta con ID: {}", accountId);

        // Llama al servicio para actualizar la cuenta por su ID y los nuevos datos.
        AccountResponseDTO accountResponseDTO = accountService.updateAccount(accountId, accountDTO);

        if (accountResponseDTO != null) {
            log.info("[AccountController] - [updateAccount] - Cuenta actualizada: {}", accountResponseDTO);
            // Devuelve la cuenta actualizada y estado HTTP 200 (OK).
            return ResponseEntity.ok(accountResponseDTO);
        } else {
            log.info("[AccountController] - [updateAccount] - Cuenta no encontrada para actualizar, ID: {}", accountId);
            // Devuelve estado HTTP 404 (Not Found) si la cuenta no existe.
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Actualiza parcialmente una cuenta por su ID.
     *
     * @param accountId  ID de la cuenta a actualizar.
     * @param accountDTO Objeto AccountDTO con los datos a actualizar de la cuenta.
     * @return ResponseEntity con la cuenta actualizada y estado HTTP 200 (OK), o estado HTTP 404 (Not Found) si la cuenta no existe.
     */
    @PatchMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> updatePatchAccount(@PathVariable Long accountId, @RequestBody AccountDTO accountDTO) {
        log.info("[AccountController] - [updatePatchAccount] - Actualizando parcialmente cuenta con ID: {}", accountId);

        // Llama al servicio para actualizar parcialmente la cuenta por su ID y los datos proporcionados.
        AccountResponseDTO accountResponseDTO = accountService.updateAccount(accountId, accountDTO);

        if (accountResponseDTO != null) {
            log.info("[AccountController] - [updatePatchAccount] - Cuenta actualizada parcialmente: {}", accountResponseDTO);
            // Devuelve la cuenta actualizada y estado HTTP 200 (OK).
            return ResponseEntity.ok(accountResponseDTO);
        } else {
            log.info("[AccountController] - [updatePatchAccount] - Cuenta no encontrada para actualizar, ID: {}", accountId);
            // Devuelve estado HTTP 404 (Not Found) si la cuenta no existe.
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Elimina una cuenta por su ID.
     *
     * @param accountId ID de la cuenta a eliminar.
     * @return ResponseEntity con estado HTTP 204 (No Content) si la cuenta se eliminó con éxito,
     *         o estado HTTP 404 (Not Found) si la cuenta no existe.
     */
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        log.info("[AccountController] - [deleteAccount] - Eliminando cuenta con ID: {}", accountId);

        // Llama al servicio para eliminar la cuenta por su ID.
        accountService.deleteAccount(accountId);

        log.info("[AccountController] - [deleteAccount] - Cuenta eliminada con éxito, ID: {}", accountId);
        // Devuelve estado HTTP 204 (No Content) si la cuenta se eliminó con éxito.
        return ResponseEntity.noContent().build();
    }

}
