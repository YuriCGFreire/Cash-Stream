package com.yuri.freire.Cash_Stream.util.expense;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryRequest;

public class ExpenseSubcategoryRequestCreator {
    public static ExpenseSubcategoryRequest createExpenseSubcategoryRequest(){
        return ExpenseSubcategoryRequest.builder()
                .subcategoryName("McDonalds")
                .categoryName(ExpenseCategoryCreator.createValidExpenseCategory().getCategoryName())
                .build();
    }

    public static ExpenseSubcategoryRequest createInvalidSubcategoryRequest(){
        return ExpenseSubcategoryRequest.builder()
                .subcategoryName("a")
                .categoryName(null)
                .build();
    }
}
