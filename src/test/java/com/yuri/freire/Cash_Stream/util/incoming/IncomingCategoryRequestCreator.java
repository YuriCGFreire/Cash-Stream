package com.yuri.freire.Cash_Stream.util.incoming;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryRequest;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCategoryCreator;

public class IncomingCategoryRequestCreator {
    public static IncomingCategoryRequest createIncomingCategoryRequest(){
        return IncomingCategoryRequest.builder()
                .categoryName("Trabalho")
                .build();
    }
}
