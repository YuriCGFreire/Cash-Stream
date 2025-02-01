package com.yuri.freire.Cash_Stream.util.incoming;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCategoryCreator;
import com.yuri.freire.Cash_Stream.util.user.UserCreator;

public class IncomingSubcategoryCreator {
    public static IncomingSubcategory createSubcategoryToBeSaved(){
        IncomingCategory category = IncomingCategoryCreator.createValidCategoryForRepository();
        return IncomingSubcategory.builder()
                .subCategoryName("Apple")
                .incomingCategory(category)
                .user(UserCreator.createValidUser())
                .build();
    }

    public static IncomingSubcategory createValidSubcategoryRepository(){
        return IncomingSubcategory.builder()
                .incomingSubcategoryId(1)
                .subCategoryName("Apple")
                .incomingCategory(IncomingCategoryCreator.createValidCategoryForRepository())
                .user(UserCreator.createValidUser())
                .build();
    }

    public static IncomingSubcategoryResponse createValidSubcategoryResponse(){
        IncomingCategory category = IncomingCategoryCreator.createValidCategoryForRepository();
        return IncomingSubcategoryResponse.builder()
                .incomingSubcategoryId(1)
                .subCategoryName("Apple")
                .categoryName(category.getCategoryName())
                .username(UserCreator.createValidUser().getUsername())
                .build();
    }

    public static IncomingSubcategory createValidUpdatedCategory(){
        return IncomingSubcategory.builder()
                .incomingSubcategoryId(1)
                .subCategoryName("Apple atualizada")
                .build();
    }
}
