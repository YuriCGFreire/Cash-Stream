package com.yuri.freire.Cash_Stream.util.incoming;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingRequest;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCreator;

public class IncomingRequestCreator {
    public static IncomingRequest createIncomningRequest(){
        return IncomingRequest.builder()
                .incomingDescription(IncomingCreator.createValidIncomingToBeSaved().getIncomingDescription())
                .grossIncoming(IncomingCreator.createValidIncomingToBeSaved().getGrossIncoming())
                .netIncoming(IncomingCreator.createValidIncomingToBeSaved().getNetIncoming())
                .recurrence(IncomingCreator.createValidIncomingToBeSaved().getRecurrence().getRecurrenceFrequency())
                .incomingCategory(IncomingCreator.createValidIncomingToBeSaved().getIncomingCategory().getCategoryName())
                .incomingSubcategory(IncomingCreator.createValidIncomingToBeSaved().getIncomingSubcategory().getSubCategoryName())
                .build();
    }
}
