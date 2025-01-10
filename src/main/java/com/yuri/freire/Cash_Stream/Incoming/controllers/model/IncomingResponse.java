package com.yuri.freire.Cash_Stream.Incoming.controllers.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomingResponse {
    private Integer incomingId;
    private String incomingDescription;
    private BigDecimal grossIncoming;
    private BigDecimal netIncoming;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate incomingDate;
    private RecurrenceType recurrence;
    private String categoryName;
    private String subCategoryName;
}
