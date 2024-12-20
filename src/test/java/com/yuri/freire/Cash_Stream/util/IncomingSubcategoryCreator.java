package com.yuri.freire.Cash_Stream.util;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;

public class IncomingSubcategoryCreator {
    public static IncomingSubcategory createSubcategoryToBeSaved(){
        IncomingCategory category = IncomingCategoryCreator.createValidCategoryForRepository();
        return IncomingSubcategory.builder()
                .subCategoryName("Apple")
                .incomingCategory(category)
                .build();
    }

    public static IncomingSubcategory createValidSubcategoryRepository(){
        return IncomingSubcategory.builder()
                .incomingSubcategoryId(1)
                .subCategoryName("Apple")
                .incomingCategory(IncomingCategoryCreator.createValidCategoryForRepository())
                .build();
    }

    public static IncomingSubcategoryResponse createValidSubcategoryResponse(){
        IncomingCategory category = IncomingCategoryCreator.createValidCategoryForRepository();
        return IncomingSubcategoryResponse.builder()
                .incomingSubcategoryId(1)
                .subCategoryName("Apple")
                .categoryName(category.getCategoryName())
                .build();
    }

    public static IncomingSubcategory createValidUpdatedCategory(){
        return IncomingSubcategory.builder()
                .incomingSubcategoryId(1)
                .subCategoryName("Apple atualizada")
                .build();
    }
}
