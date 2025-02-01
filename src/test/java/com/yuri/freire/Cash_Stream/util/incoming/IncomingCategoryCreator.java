package com.yuri.freire.Cash_Stream.util.incoming;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.util.user.UserCreator;

public class IncomingCategoryCreator {

    public static IncomingCategory createCategoryToBeSaved(){
        return IncomingCategory.builder()
                .categoryName("Stocks")
                .user(UserCreator.createValidUser())
                .build();
    }

    public static IncomingCategoryResponse createValidCategory(){
        return IncomingCategoryResponse.builder()
                .incomingCategoryId(1)
                .categoryName("Stocks")
                .username(UserCreator.createValidUser().getUsername())
                .build();
    }

    public static IncomingCategory createValidCategoryForRepository(){
        return IncomingCategory.builder()
                .incomingCategoryId(1)
                .categoryName("Stocks")
                .user(UserCreator.createValidUser())
                .build();
    }

    public static IncomingCategory createValidUpdatedCategory(){
        return IncomingCategory.builder()
                .incomingCategoryId(1)
                .categoryName("Stocks atualizado")
                .build();
    }
}
