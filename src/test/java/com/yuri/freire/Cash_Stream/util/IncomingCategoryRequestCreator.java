package com.yuri.freire.Cash_Stream.util;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryRequest;

public class IncomingCategoryRequestCreator {
    public static IncomingCategoryRequest createIncomingCategoryRequest(){
        return IncomingCategoryRequest.builder()
                .categoryName(IncomingCategoryCreator.createCategoryToBeSaved().getCategoryName())
                .build();
    }
}
