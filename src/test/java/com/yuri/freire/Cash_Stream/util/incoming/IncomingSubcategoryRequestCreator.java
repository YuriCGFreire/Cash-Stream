package com.yuri.freire.Cash_Stream.util.incoming;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryRequest;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingSubcategoryCreator;

public class IncomingSubcategoryRequestCreator {
    public static IncomingSubcategoryRequest createIncomingSubcategoryRequest(){
        return IncomingSubcategoryRequest.builder()
                .subcategoryName(IncomingSubcategoryCreator.createValidSubcategoryResponse().getSubCategoryName())
                .incomingCategoryName(IncomingSubcategoryCreator.createValidSubcategoryResponse().getCategoryName())
                .build();
    }
}
