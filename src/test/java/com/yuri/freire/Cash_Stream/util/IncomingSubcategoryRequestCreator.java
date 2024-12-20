package com.yuri.freire.Cash_Stream.util;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryRequest;

public class IncomingSubcategoryRequestCreator {
    public static IncomingSubcategoryRequest createIncomingSubcategoryRequest(){
        return IncomingSubcategoryRequest.builder()
                .subcategoryName(IncomingSubcategoryCreator.createValidSubcategoryResponse().getSubCategoryName())
                .incomingCategoryName(IncomingSubcategoryCreator.createValidSubcategoryResponse().getCategoryName())
                .build();
    }
}
