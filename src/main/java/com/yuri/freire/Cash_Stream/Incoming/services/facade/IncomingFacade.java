package com.yuri.freire.Cash_Stream.Incoming.services.facade;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingCategoryService;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingSubcategoryService;
import com.yuri.freire.Cash_Stream.Incoming.services.factory.IncomingFactory;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.services.RecurrenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncomingFacade {

    private final IncomingSubcategoryService incomingSubcategoryService;
    private final IncomingCategoryService incomingCategoryService;
    private final RecurrenceService recurrenceService;

    private final IncomingFactory incomingFactory;

    public Incoming createIncoming(IncomingRequest incomingRequest){
        IncomingCategory incomingCategory = incomingCategoryService.findByCategoryName(incomingRequest.getIncomingCategory());
        IncomingSubcategory incomingSubcategory = incomingSubcategoryService.findBySubcategoryName(incomingRequest.getIncomingSubcategory());
        Recurrence recurrence = recurrenceService.findByRecurrenFrequency(incomingRequest.getRecurrence());

        return incomingFactory.createIncoming(incomingRequest, incomingCategory, incomingSubcategory, recurrence);
    }

    public IncomingResponse createIncomingResponse(Incoming incoming){
        return incomingFactory.createIncomingResponse(incoming);
    }

}
