package com.yuri.freire.Cash_Stream.util.expense;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;

public class ExpenseCategoryCreator {

    public static ExpenseCategory createValidExpenseCategory(){
        return ExpenseCategory.builder()
                .expenseCategoryId(1)
                .categoryName("Alimentação")
                .build();
    }

    public static ExpenseCategoryResponse createValidExpenseCategoryResponse(){
        return ExpenseCategoryResponse.builder()
                .expenseCategoryId(1)
                .categoryName("Alimentação")
                .build();
    }

    public static ExpenseCategory createExpenseCategoryToBeSaved(){
        return ExpenseCategory.builder()
                .categoryName("Alimentação")
                .build();
    }

}
