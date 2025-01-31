package com.yuri.freire.Cash_Stream.util.expense;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseRequest;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseRequestCreator {
    public static ExpenseRequest createExpenseRequest(){
        return ExpenseRequest.builder()
                .expenseDescription("Pizza Calabresa")
                .expenseAmount(new BigDecimal(130.00))
                .expenseMethod(ExpenseMethodCreator.createValidExpenseMethod().getExpenseMethodName())
                .recurrence(ExpenseCreator.createValidExpense().getRecurrence().getRecurrenceFrequency())
                .expenseDate(LocalDate.parse("2025-03-01"))
                .expenseCategory(ExpenseCategoryCreator.createValidExpenseCategory().getCategoryName())
                .expenseSubcategory(ExpenseSubcategoryCreator.createValidExpenseSubcategory().getSubCategoryName())
                .build();
    }

    public static ExpenseRequest createInvalidExpenseRequest(){
        return ExpenseRequest.builder()
                .expenseDescription("")
                .expenseAmount(new BigDecimal(0.00))
                .expenseMethod(null)
                .recurrence(null)
                .expenseDate(null)
                .expenseCategory("")
                .expenseSubcategory("")
                .build();
    }
}
