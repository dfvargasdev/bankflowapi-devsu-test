package com.devsu.bankflowapi.repositories;

import com.devsu.bankflowapi.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Recupera el último saldo para la cuenta especificada.
     *
     * @param accountId El ID de la cuenta para la cual se va a recuperar el saldo.
     * @return El último saldo de la cuenta, o BigDecimal.ZERO si no se encuentran transacciones.
     */
    @Query("SELECT COALESCE(SUM(t.value), 0) FROM Transaction t WHERE t.account.id = :accountId")
    BigDecimal getLastBalanceByAccountId(@Param("accountId") Long accountId);

    /**
     * Calcula el monto total de retiro para la cuenta especificada en un día dado.
     *
     * @param accountId     El ID de la cuenta para la cual se calculará el monto total de retiro.
     * @param startOfDay    El comienzo del día para el cual se calculará el monto total de retiro.
     * @param transactionType El tipo de transacción (por ejemplo, 'Retiro').
     * @return El monto total del tipo de movimiento para la cuenta en el día especificado.
     */
    @Query("SELECT COALESCE(SUM(t.value), 0) FROM Transaction t WHERE t.account.id = :accountId " +
            "AND t.date >= :startOfDay AND t.transactionType = :transactionType")
    BigDecimal getDailyTransactionTypeTotal(@Param("accountId") Long accountId, @Param("startOfDay") Date startOfDay, @Param("transactionType") String transactionType);

    /**
     * Obtiene una lista de movimientos para un cliente en un rango de fechas específico.
     *
     * @param clientId   ID del cliente para el cual se obtendrán los movimientos.
     * @param startDate  Fecha de inicio del rango de fechas.
     * @param endDate    Fecha de fin del rango de fechas.
     * @return Una lista de objetos {@link Transaction} que representan los movimientos generadas.
     */
    @Query("SELECT t FROM Transaction t JOIN t.account a WHERE a.client.id = :clientId AND t.date >= :startDate " +
            "AND t.date <= :endDate ORDER BY t.date")
    List<Transaction> getTransactionsByAccountAndDateRange(@Param("clientId") Long clientId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
