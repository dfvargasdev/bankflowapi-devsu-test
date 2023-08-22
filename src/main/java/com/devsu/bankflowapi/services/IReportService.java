package com.devsu.bankflowapi.services;

import com.devsu.bankflowapi.models.dto.ReportResponseDTO;

import java.time.LocalDate;
import java.util.Date;

public interface IReportService {
    ReportResponseDTO generateReport(Date startDate, Date endDate, Long clientId);
}
