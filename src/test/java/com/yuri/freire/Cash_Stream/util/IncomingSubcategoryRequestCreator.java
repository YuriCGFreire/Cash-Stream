package com.yuri.freire.Cash_Stream.util;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryRequest;

public class IncomingSubcategoryRequestCreator {
    public static IncomingSubcategoryRequest createIncomingSubcategoryRequest(){
        return IncomingSubcategoryRequest.builder()
                .subcategoryName(IncomingSubcategoryCreator.createValidSubcategoryResponse().getIncomingSubcategoryName())
                .incomingCategoryName(IncomingSubcategoryCreator.createValidSubcategoryResponse().getIncomingCategoryName())
                .build();
    }
}
