package com.devsu.bankflowapi.controllers;

import com.devsu.bankflowapi.controllers.dto.TransactionDTO;
import com.devsu.bankflowapi.models.dto.TransactionResponseDTO;
import com.devsu.bankflowapi.services.ITransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador que maneja las solicitudes relacionadas con la creación de transacciones.
 */
@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final ITransactionService transactionService;

    /**
     * Crea un nuevo movimiento.
     *
     * @param transactionDTO Objeto que contiene los detalles del movimiento a crear.
     * @return El movimiento creado en forma de TransactionResponseDTO.
     */
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        log.info("[TransactionController]-[createTransaction]-Creando un nuevo movimiento...");

        TransactionResponseDTO createdTransaction = transactionService.createTransaction(transactionDTO);

        log.info("[TransactionController]-[createTransaction]-Movimiento creado exitosamente.");

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }


    /**
     * Obtiene un movimiento por su ID.
     *
     * @param transactionId ID del movimiento que se desea obtener.
     * @return La movimiento encontrada en forma de {@link TransactionResponseDTO}.
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable Long transactionId) {
        log.info("[TransactionController]-[getTransaction]-Obteniendo movimiento con ID: {}", transactionId);

        TransactionResponseDTO transaction = transactionService.getTransactionById(transactionId);

        log.info("[TransactionController]-[getTransaction]-movimiento obtenida exitosamente.");

        return ResponseEntity.ok(transaction);
    }

    /**
     * Actualiza un movimiento existente.
     *
     * @param transactionId   ID del movimiento que se desea actualizar.
     * @param transactionDTO  Objeto que contiene los detalles del movimiento actualizada.
     * @return La movimiento actualizada en forma de {@link TransactionResponseDTO}.
     */
    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long transactionId, @RequestBody TransactionDTO transactionDTO) {
        log.info("[TransactionController]-[updateTransaction]-Actualizando movimiento con ID: {}", transactionId);

        TransactionResponseDTO updatedTransaction = transactionService.updateTransaction(transactionId, transactionDTO);

        log.info("[TransactionController]-[updateTransaction]-movimiento actualizada exitosamente.");

        return ResponseEntity.ok(updatedTransaction);
    }

    /**
     * Elimina un movimiento por su ID.
     *
     * @param transactionId ID del movimiento que se desea eliminar.
     * @return Una respuesta con el estado correspondiente a la eliminación exitosa.
     */
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {
        log.info("[TransactionController]-[deleteTransaction]-Eliminando movimiento con ID: {}", transactionId);

        transactionService.deleteTransaction(transactionId);

        log.info("[TransactionController]-[deleteTransaction]-movimiento eliminado exitosamente.");

        return ResponseEntity.noContent().build();
    }
}

