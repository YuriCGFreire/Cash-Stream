package com.yuri.freire.Cash_Stream.Incoming.services.factory;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.*;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncomingFactory {

    public Incoming createIncoming(IncomingRequest incomingRequest,
                                   IncomingCategory incomingCategory,
                                   IncomingSubcategory incomingSubcategory,
                                   Recurrence recurrence){
        return Incoming.builder()
                .incomingDescription(incomingRequest.getIncomingDescription())
                .grossIncoming(incomingRequest.getGrossIncoming())
                .netIncoming(incomingRequest.getNetIncoming())
                .incomingCategory(incomingCategory)
                .incomingSubcategory(incomingSubcategory)
                .recurrence(recurrence)
                .build();
    }

    public IncomingResponse createIncomingResponse(Incoming incoming){
        return IncomingResponse.builder()
                .incomingId(incoming.getIncomingId())
                .incomingDescription(incoming.getIncomingDescription())
                .grossIncoming(incoming.getGrossIncoming())
                .netIncoming(incoming.getNetIncoming())
                .recurrence(incoming.getRecurrence().getRecurrenceFrequency())
                .categoryName(incoming.getIncomingSubcategory().getSubCategoryName())
                .subCategoryName(incoming.getIncomingCategory().getCategoryName())
                .build();
    }

    public IncomingSubcategory createIncomingSubcategory(IncomingSubcategoryRequest incomingSubcategoryRequest, IncomingCategory incomingCategory){
        return IncomingSubcategory.builder()
                .subCategoryName(incomingSubcategoryRequest.getSubcategoryName())
                .incomingCategory(incomingCategory)
                .build();
    }

    public IncomingSubcategoryResponse createIncomingSubcategoryResponse(IncomingSubcategory incomingSubcategory){
        return IncomingSubcategoryResponse.builder()
                .incomingSubcategoryId(incomingSubcategory.getIncomingSubcategoryId())
                .subCategoryName(incomingSubcategory.getSubCategoryName())
                .categoryName(incomingSubcategory.getIncomingCategory().getCategoryName())
                .build();
    }
    public IncomingCategory createIncomingCategory(IncomingCategoryRequest incomingCategoryRequest){
        return IncomingCategory.builder()
                .categoryName(incomingCategoryRequest.getCategoryName())
                .build();
    }

    public IncomingCategoryResponse createIncomingCategoryResponse(IncomingCategory incomingCategory){
        return IncomingCategoryResponse.builder()
                .incomingCategoryId(incomingCategory.getIncomingCategoryId())
                .categoryName(incomingCategory.getCategoryName())
                .build();
    }

}
