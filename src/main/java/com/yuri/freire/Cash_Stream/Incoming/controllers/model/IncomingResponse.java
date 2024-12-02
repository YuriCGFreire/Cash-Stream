package com.yuri.freire.Cash_Stream.Incoming.controllers.model;

import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomingResponse {

    private String incomingDescription;

    private BigDecimal grossIncoming;

    private BigDecimal netIncoming;

    private RecurrenceType recurrence;

    private String incomingCategory;

    private String incomingSubcategory;
}
