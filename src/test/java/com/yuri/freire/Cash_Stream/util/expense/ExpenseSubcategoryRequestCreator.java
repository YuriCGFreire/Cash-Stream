package com.yuri.freire.Cash_Stream.util.expense;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryRequest;

public class ExpenseSubcategoryRequestCreator {
    public static ExpenseSubcategoryRequest createExpenseSubcategoryRequest(){
        return ExpenseSubcategoryRequest.builder()
                .subcategoryName(ExpenseSubcategoryCreator.createValidExpenseSubcategory().getSubCategoryName())
                .categoryName(ExpenseCategoryCreator.createValidExpenseCategory().getCategoryName())
                .build();
    }
}
