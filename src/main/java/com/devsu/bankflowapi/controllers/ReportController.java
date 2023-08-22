package com.devsu.bankflowapi.controllers;


import com.devsu.bankflowapi.models.dto.ReportResponseDTO;
import com.devsu.bankflowapi.services.IReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;


/**
 * Controlador que maneja las solicitudes relacionadas con la generación de reportes de movimientos.
 */
@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final IReportService reportService;

    /**
     * Genera un informe de transacciones para un cliente en un rango de fechas específico.
     *
     * @param startDate Fecha de inicio del rango de fechas.
     * @param endDate   Fecha de fin del rango de fechas.
     * @param clientId  ID del cliente para el cual se generará el informe.
     * @return Una lista ReportResponseDTO que contiene los informes de movimientos generados.
     */
    @GetMapping
    public ResponseEntity<ReportResponseDTO> generateReport(
            @RequestParam("fecha-inicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("fecha-fin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam("cliente-id") Long clientId) {

        log.info("[ReportController]-[generateReport]-Generando informe de transacciones...");

        LocalDateTime endDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault())
                .with(LocalTime.MAX);
        endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        ReportResponseDTO report = reportService.generateReport(startDate, endDate, clientId);

        log.info("[ReportController]-[generateReport]-Informe de transacciones generado exitosamente.");

        return ResponseEntity.ok(report);
    }
}

