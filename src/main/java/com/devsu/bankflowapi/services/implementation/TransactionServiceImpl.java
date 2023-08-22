package com.devsu.bankflowapi.services.implementation;

import com.devsu.bankflowapi.controllers.dto.TransactionDTO;
import com.devsu.bankflowapi.enums.TransactionType;
import com.devsu.bankflowapi.models.Account;
import com.devsu.bankflowapi.models.Transaction;
import com.devsu.bankflowapi.models.dto.TransactionResponseDTO;
import com.devsu.bankflowapi.repositories.IAccountRepository;
import com.devsu.bankflowapi.repositories.ITransactionRepository;
import com.devsu.bankflowapi.services.ITransactionService;
import com.devsu.bankflowapi.services.exeptions.InsufficientBalanceException;
import com.devsu.bankflowapi.services.exeptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final IAccountRepository accountRepository;
    private final BigDecimal DAILY_WITHDRAWAL_LIMIT = new BigDecimal("1000");

    /**
     * Crea un nuevo movimiento en la cuenta y realiza las validaciones necesarias.
     *
     * @param transactionDTO DTO que contiene los detalles del movimiento.
     * @return DTO de respuesta del movimiento creado.
     * @throws InsufficientBalanceException Si no hay saldo suficiente para un retiro.
     * @throws NotFoundException Si no se encuentra la cuenta o hay algún otro error.
     */
    @Override
    public TransactionResponseDTO createTransaction(TransactionDTO transactionDTO) {
        try {
            log.info("[TransactionServiceImpl]-[createTransaction]-Comenzando la creación del movimiento...");
            Transaction transaction = convertDtoToEntity(transactionDTO);
            transaction = transactionRepository.save(transaction);
            log.info("[TransactionServiceImpl]-[createTransaction]-Movimiento creado exitosamente");
            return convertEntityToResponseDto(transaction);
        } catch (InsufficientBalanceException e) {
            log.error("Saldo insuficiente al intentar crear el movimiento", e);
            throw new InsufficientBalanceException("Saldo no disponible");
        } catch (Exception e) {
            log.error("Error al crear el movimiento", e);
            throw new NotFoundException("Error al crear el movimiento", e);
        }
    }

    /**
     * Obtiene un movimiento por su ID.
     *
     * @param transactionId ID del movimiento a buscar.
     * @return DTO del movimiento encontrado.
     * @throws NotFoundException Si no se encuentra el movimiento con el ID proporcionado.
     */
    @Override
    public TransactionResponseDTO getTransactionById(Long transactionId) {
        log.info("[TransactionServiceImpl] - [getTransactionById] - Buscando movimiento por ID: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("movimiento no encontrado"));

        TransactionResponseDTO responseDTO = convertEntityToResponseDto(transaction);

        log.info("[TransactionServiceImpl] - [getTransactionById] - movimiento encontrado: {}", transaction);

        return responseDTO;
    }


    @Override
    public TransactionResponseDTO updateTransaction(Long transactionId, TransactionDTO transactionDTO) {
        log.info("[TransactionServiceImpl] - [updateTransaction] - Actualizando movimiento con ID: {}", transactionId);

        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("movimiento no encontrada"));

        updateTransactionFields(existingTransaction, transactionDTO);
        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        TransactionResponseDTO responseDTO = convertEntityToResponseDto(updatedTransaction);

        log.info("[TransactionServiceImpl] - [updateTransaction] - movimiento actualizado: {}", updatedTransaction);

        return responseDTO;
    }

    @Override
    public void deleteTransaction(Long transactionId) {
        log.info("[TransactionServiceImpl] - [deleteTransaction] - Eliminando movimiento con ID: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("movimiento no encontrado"));

        transactionRepository.delete(transaction);

        log.info("[TransactionServiceImpl] - [deleteTransaction] - movimiento eliminado: {}", transaction);
    }

    /**
     * Convierte un DTO de movimiento en una entidad Transaction.
     * Realiza cálculos de saldo y determina el tipo de movimiento.
     *
     * @param transactionDTO El DTO del movimiento a convertir.
     * @return Una instancia de Transaction con los datos convertidos.
     * @throws NotFoundException Si la cuenta asociada al movimiento no se encuentra.
     */
    private Transaction convertDtoToEntity(TransactionDTO transactionDTO) {
        log.info("Comenzando la conversión de DTO a entidad Transaction.");
        Transaction transaction = new Transaction();
        transaction.setValue(transactionDTO.getValue());

        transaction.setDate(new Date());

        log.debug("Buscando la cuenta en el repositorio por ID: {}", transactionDTO.getAccountId());
        Account account = accountRepository.findById(transactionDTO.getAccountId())
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        BigDecimal balance = calculateBalance(transactionDTO, account);
        transaction.setBalance(balance);

        TransactionType transactionType = determineTransactionType(transactionDTO.getValue());
        transaction.setTransactionType(transactionType.getDisplayName());

        transaction.setAccount(account);

        return transaction;
    }

    /**
     * Convierte una entidad Transaction en un DTO TransactionResponseDTO.
     *
     * @param transaction La entidad Transaction a convertir.
     * @return Un objeto TransactionResponseDTO con los datos del movimiento convertido.
     */
    private TransactionResponseDTO convertEntityToResponseDto(Transaction transaction) {
        log.info("Iniciando conversión de Transaction a TransactionResponseDTO");

        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setId(transaction.getId());
        responseDTO.setValue(transaction.getValue());
        responseDTO.setDate(transaction.getDate());
        responseDTO.setAccount(transaction.getAccount());
        responseDTO.setBalance(transaction.getBalance());
        responseDTO.setTransactionType(transaction.getTransactionType());
        responseDTO.setTransaction(transaction.getTransactionType() + " de " + transaction.getValue().abs());

        log.info("Conversión de Transaction a TransactionResponseDTO completada con éxito");
        return responseDTO;
    }

    /**
     * Calcula el saldo de la cuenta considerando las transacciones anteriores y las validaciones diarias.
     *
     * @param transactionDTO El DTO del movimiento actual.
     * @param account La cuenta asociada al movimiento.
     * @return El saldo calculado de la cuenta después del movimiento.
     * @throws InsufficientBalanceException Si no hay suficiente saldo disponible para un retiro.
     * @throws IllegalArgumentException Si ocurre un error en los cálculos de saldo.
     */
    private BigDecimal calculateBalance(TransactionDTO transactionDTO, Account account) {
        BigDecimal totalTransactions = transactionRepository
                .getLastBalanceByAccountId(transactionDTO.getAccountId());
        BigDecimal valueTransaction = transactionDTO.getValue();
        BigDecimal initialBalance = account.getInitialBalance();

        log.debug("Calculando el saldo: Cuenta={}, Total movimientos={}, Valor movimiento={}, Saldo inicial={}",
                transactionDTO.getAccountId(), totalTransactions, valueTransaction, initialBalance);

        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        Date startOfDayDate = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());

        BigDecimal balanceTransaction = totalTransactions.add(valueTransaction).add(initialBalance);

        if (valueTransaction.compareTo(BigDecimal.ZERO) < 0 ) {
            if (balanceTransaction.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientBalanceException("Saldo no disponible");
            }

            BigDecimal currentDailyTotal = transactionRepository
                    .getDailyTransactionTypeTotal(transactionDTO.getAccountId(), startOfDayDate, TransactionType.WITHDRAWAL.getDisplayName());
            BigDecimal newDailyTotal = currentDailyTotal.add(valueTransaction);

            if (newDailyTotal.abs().compareTo(DAILY_WITHDRAWAL_LIMIT) > 0) {
                throw new IllegalArgumentException("Cupo diario Excedido");
            }
        }
        log.debug("Saldo calculado: {}", balanceTransaction);
        return balanceTransaction;
    }

    /**
     * Determina el tipo de movimiento (Retiro o Depósito) en función del valor del movimiento.
     *
     * @param valueTransaction El valor del movimiento.
     * @return El tipo de movimiento (Retiro o Depósito) basado en el valor del movimiento.
     */
    private TransactionType determineTransactionType(BigDecimal valueTransaction) {
        log.debug("Determinando el tipo de movimiento para el valor: {}", valueTransaction);

        return valueTransaction.compareTo(BigDecimal.ZERO) < 0 ?
                TransactionType.WITHDRAWAL : TransactionType.DEPOSIT;
    }


    private void updateTransactionFields(Transaction existingTransaction, TransactionDTO transactionDTO) {
        existingTransaction.setValue(transactionDTO.getValue());
    }
}
