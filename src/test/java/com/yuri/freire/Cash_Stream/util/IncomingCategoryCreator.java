package com.yuri.freire.Cash_Stream.util;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;

public class IncomingCategoryCreator {

    public static IncomingCategory createCategoryToBeSaved(){
        return IncomingCategory.builder()
                .categoryName("Stocks")
                .build();
    }

    public static IncomingCategoryResponse createValidCategory(){
        return IncomingCategoryResponse.builder()
                .incomingCategoryId(1)
                .incomingCategoryName("Stocks")
                .build();
    }

    public static IncomingCategory createValidCategoryForRepository(){
        return IncomingCategory.builder()
                .incomingCategoryId(1)
                .categoryName("Stocks")
                .build();
    }

    public static IncomingCategory createValidUpdatedCategory(){
        return IncomingCategory.builder()
                .incomingCategoryId(1)
                .categoryName("Stocks atualizado")
                .build();
    }
}
