package com.yuri.freire.Cash_Stream.util.expense;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryRequest;

public class ExpenseCategoryRequestCreator {
    public static ExpenseCategoryRequest createExpenseCategoryRequest(){
        return ExpenseCategoryRequest.builder()
                .categoryName("Magic")
                .build();
    }

    public static ExpenseCategoryRequest createInvalidCategoryRequest(){
        return ExpenseCategoryRequest.builder()
                .categoryName("")
                .build();
    }
}
