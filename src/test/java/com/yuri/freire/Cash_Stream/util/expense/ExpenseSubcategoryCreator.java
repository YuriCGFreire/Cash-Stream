package com.yuri.freire.Cash_Stream.util.expense;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseSubcategory;

public class ExpenseSubcategoryCreator {
    public static ExpenseSubcategory createValidExpenseSubcategory(){
        return ExpenseSubcategory.builder()
                .expenseSubcategoryId(1)
                .subCategoryName("Ifood")
                .expenseCategory(ExpenseCategoryCreator.createValidExpenseCategory())
                .build();
    }

    public static ExpenseSubcategoryResponse createValidExpenseSubcategoryResponse(){
        return ExpenseSubcategoryResponse.builder()
                .expenseSubcategoryId(1)
                .subCategoryName("Ifood")
                .categoryName(ExpenseCategoryCreator.createValidExpenseCategory().getCategoryName())
                .build();
    }

    public static ExpenseSubcategory createValidExpenseSubcategoryTobeSaved(){
        return ExpenseSubcategory.builder()
                .subCategoryName("Ifood")
                .expenseCategory(ExpenseCategoryCreator.createValidExpenseCategory())
                .build();
    }
}
