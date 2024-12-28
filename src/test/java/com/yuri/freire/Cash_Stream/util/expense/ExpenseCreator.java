package com.yuri.freire.Cash_Stream.util.expense;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.Expense;

import java.math.BigDecimal;

public class ExpenseCreator {
    public static Expense createValidExpense(){
        return Expense.builder()
                .expenseId(1)
                .expenseDescription("Pizza no ifood")
                .expenseAmount(new BigDecimal("130.00"))
                .expenseMethod(ExpenseMethodCreator.createValidExpenseMethod())
                .isEssential(false)
                .expenseCategory(ExpenseCategoryCreator.createValidExpenseCategory())
                .expenseSubcategory(ExpenseSubcategoryCreator.createValidExpenseSubcategory())
                .build();
    }

    public static ExpenseResponse createValidExpenseResponse(){
        return ExpenseResponse.builder()
                .expenseId(1)
                .expenseDescription("Pizza no ifood")
                .expenseAmount(new BigDecimal("130.00"))
                .expenseMethod(ExpenseMethodCreator.createValidExpenseMethod().getExpenseMethodName())
                .isEssential(false)
                .categoryName(ExpenseCategoryCreator.createValidExpenseCategory().getCategoryName())
                .subCategoryName(ExpenseSubcategoryCreator.createValidExpenseSubcategory().getSubCategoryName())
                .build();
    }

    public static Expense createExpenseToBeSaved(){
        return Expense.builder()
                .expenseDescription("Pizza no ifood")
                .expenseAmount(new BigDecimal("130.00"))
                .expenseMethod(ExpenseMethodCreator.createValidExpenseMethod())
                .isEssential(false)
                .expenseCategory(ExpenseCategoryCreator.createValidExpenseCategory())
                .expenseSubcategory(ExpenseSubcategoryCreator.createValidExpenseSubcategory())
                .build();
    }
}
