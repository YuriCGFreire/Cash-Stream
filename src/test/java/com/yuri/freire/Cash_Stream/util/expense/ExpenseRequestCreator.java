package com.yuri.freire.Cash_Stream.util.expense;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseRequest;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;

import java.math.BigDecimal;

public class ExpenseRequestCreator {
    public static ExpenseRequest createExpenseRequest(){
        return ExpenseRequest.builder()
                .expenseDescription("Pizza Calabresa")
                .expenseAmount(new BigDecimal(120.00))
                .expenseMethod(ExpenseMethodCreator.createValidExpenseMethod().getExpenseMethodName())
                .expenseCategory(ExpenseCategoryCreator.createValidExpenseCategory().getCategoryName())
                .expenseSubcategory(ExpenseSubcategoryCreator.createValidExpenseSubcategory().getSubCategoryName())
                .build();
    }
}
