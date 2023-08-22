package com.devsu.bankflowapi.models.dto;

import com.devsu.bankflowapi.controllers.dto.TransactionReportDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ReportResponseDTO {
    private List<TransactionReportDTO> report = new ArrayList<>();

}
