package com.devsu.bankflowapi.services.implementation;

import com.devsu.bankflowapi.controllers.dto.TransactionReportDTO;
import com.devsu.bankflowapi.models.Account;
import com.devsu.bankflowapi.models.Client;
import com.devsu.bankflowapi.models.Transaction;
import com.devsu.bankflowapi.models.dto.ReportResponseDTO;
import com.devsu.bankflowapi.repositories.IAccountRepository;
import com.devsu.bankflowapi.repositories.IClientRepository;
import com.devsu.bankflowapi.repositories.ITransactionRepository;
import com.devsu.bankflowapi.services.IReportService;
import com.devsu.bankflowapi.services.exeptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements IReportService {

    private final IAccountRepository accountRepository;
    private final ITransactionRepository transactionRepository;
    private final IClientRepository clientRepository;

    /**
     * Genera un informe de movimientos para un cliente en un rango de fechas específico.
     *
     * @param startDate Fecha de inicio del rango de fechas.
     * @param endDate   Fecha de fin del rango de fechas.
     * @param clientId  ID del cliente para el cual se generará el informe.
     * @return Un objeto {@link ReportResponseDTO} que contiene los informes de movimientos generados.
     * @throws NotFoundException Si el cliente no puede ser encontrado en la base de datos.
     */
    @Override
    public ReportResponseDTO generateReport(Date startDate, Date endDate, Long clientId) {
        log.info("[ReportServiceImpl]-[generateReport]-Comenzando la generacion del reporte...");

        Client client = getClient(clientId);
        List<Transaction> transactions = getTransactions(clientId, startDate, endDate);

        List<TransactionReportDTO> transactionReports = transactions.stream()
                .map(transaction -> convertTransactionToReport(transaction, client))
                .collect(Collectors.toList());

        ReportResponseDTO responseDTO = new ReportResponseDTO();
        responseDTO.getReport().addAll(transactionReports);

        log.info("[ReportServiceImpl]-[generateReport]-Reporte generado.");
        return responseDTO;
    }

    /**
     * Obtiene el cliente a partir de su ID.
     *
     * @param clientId ID del cliente a buscar.
     * @return El cliente correspondiente al ID.
     * @throws NotFoundException Si el cliente no puede ser encontrado en la base de datos.
     */
    private Client getClient(Long clientId) {
        log.debug("[ReportServiceImpl]-[getClient]-Obteniendo el cliente con ID: {}", clientId);

        return clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    log.error("[ReportServiceImpl]-[getClient]-Cliente no encontrado con ID: {}", clientId);
                    return new NotFoundException("Cliente no encontrado");
                });
    }

    /**
     * Obtiene una lista de transacciones para un cliente en un rango de fechas específico.
     *
     * @param clientId  ID del cliente para el cual se obtendrán las transacciones.
     * @param startDate Fecha de inicio del rango de fechas.
     * @param endDate   Fecha de fin del rango de fechas.
     * @return Una lista de transacciones del cliente en el rango de fechas especificado.
     */
    private List<Transaction> getTransactions(Long clientId, Date startDate, Date endDate) {
        log.debug("[ReportServiceImpl]-[getTransactions]-Obteniendo transacciones para el cliente con ID: {}, en el rango de fechas: {} a {}", clientId, startDate, endDate);

        List<Transaction> transactions = transactionRepository.getTransactionsByAccountAndDateRange(clientId, startDate, endDate);

        log.debug("[ReportServiceImpl]-[getTransactions]-Se obtuvieron {} transacciones para el cliente con ID: {}, en el rango de fechas: {} a {}", transactions.size(), clientId, startDate, endDate);

        return transactions;
    }


    /**
     * Convierte una transacción en un objeto TransactionReportDTO para el informe.
     *
     * @param transaction La transacción a convertir.
     * @param client      El cliente asociado a la transacción.
     * @return Un objeto TransactionReportDTO con la información de la transacción.
     * @throws NotFoundException Si la cuenta asociada a la transacción no puede ser encontrada en la base de datos.
     */
    private TransactionReportDTO convertTransactionToReport(Transaction transaction, Client client) {
        log.debug("[ReportServiceImpl]-[convertTransactionToReport]-Convirtiendo transacción a informe: {}", transaction);

        TransactionReportDTO reportDTO = new TransactionReportDTO();
        Account account = getAccount(transaction.getAccount().getId());

        reportDTO.setDate(transaction.getDate());
        reportDTO.setClient(client.getName());
        reportDTO.setAccountNumber(account.getAccountNumber());
        reportDTO.setAccountType(account.getAccountType());
        reportDTO.setInitialBalance(account.getInitialBalance());
        reportDTO.setStatus(account.getStatus());
        reportDTO.setTransaction(transaction.getValue());
        reportDTO.setAvailableBalance(transaction.getBalance());

        log.debug("[ReportServiceImpl]-[convertTransactionToReport]-Transacción convertida exitosamente a informe: {}", reportDTO);
        return reportDTO;
    }



    /**
     * Obtiene la cuenta a partir de su ID.
     *
     * @param accountId ID de la cuenta a buscar.
     * @return La cuenta correspondiente al ID.
     * @throws NotFoundException Si la cuenta no puede ser encontrada en la base de datos.
     */
    private Account getAccount(Long accountId) {
        log.debug("[ReportServiceImpl]-[getAccount]-Obteniendo la cuenta con ID: {}", accountId);

        return accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("[ReportServiceImpl]-[getAccount]-Cuenta no encontrada con ID: {}", accountId);
                    return new NotFoundException("Cuenta no encontrada");
                });
    }

}
