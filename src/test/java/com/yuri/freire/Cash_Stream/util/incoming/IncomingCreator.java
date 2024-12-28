package com.yuri.freire.Cash_Stream.util.incoming;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;

import java.math.BigDecimal;

public class IncomingCreator {

    public static Incoming createValidIncomingToBeSaved(){
        Recurrence recurrence = Recurrence.builder()
                .recurrenceId(5)
                .recurrenceFrequency(RecurrenceType.ANNUAL)
                .build();
        return Incoming.builder()
                .incomingDescription("Dividendos Amazon")
                .grossIncoming(new BigDecimal("2500.00"))
                .netIncoming(new BigDecimal("1800.00"))
                .recurrence(recurrence)
                .incomingCategory(IncomingCategoryCreator.createValidCategoryForRepository())
                .incomingSubcategory(IncomingSubcategoryCreator.createValidSubcategoryRepository())
                .build();
    }

    public static Incoming createValidIncoming(){
        Recurrence recurrence = Recurrence.builder()
                .recurrenceFrequency(RecurrenceType.ANNUAL)
                .build();
        return Incoming.builder()
                .incomingId(1)
                .incomingDescription("Dividendos Amazon")
                .grossIncoming(new BigDecimal("2500.00"))
                .netIncoming(new BigDecimal("1800.00"))
                .recurrence(recurrence)
                .incomingCategory(IncomingCategoryCreator.createValidCategoryForRepository())
                .incomingSubcategory(IncomingSubcategoryCreator.createValidSubcategoryRepository())
                .build();
    }

    public static IncomingResponse createValidIncomingResponse(){
        return IncomingResponse.builder()
                .incomingId(1)
                .incomingDescription("Dividendos Amazon")
                .grossIncoming(new BigDecimal("2500.00"))
                .netIncoming(new BigDecimal("1800.00"))
                .recurrence(RecurrenceType.ANNUAL)
                .categoryName(IncomingCategoryCreator.createValidCategoryForRepository().getCategoryName())
                .subCategoryName(IncomingSubcategoryCreator.createValidSubcategoryRepository().getSubCategoryName())
                .build();
    }
}
