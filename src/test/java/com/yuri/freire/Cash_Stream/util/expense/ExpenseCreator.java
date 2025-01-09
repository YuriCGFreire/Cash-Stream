package com.yuri.freire.Cash_Stream.util.expense;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;

import java.math.BigDecimal;

public class ExpenseCreator {
    public static Expense createValidExpense(){
        Recurrence recurrence = Recurrence.builder()
                .recurrenceFrequency(RecurrenceType.NONRECURRING)
                .build();
        return Expense.builder()
                .expenseId(1)
                .expenseDescription("Pizza Calabresa")
                .expenseAmount(new BigDecimal(130.00))
                .expenseMethod(ExpenseMethodCreator.createValidExpenseMethod())
                .recurrence(recurrence)
                .isEssential(false)
                .expenseCategory(ExpenseCategoryCreator.createValidExpenseCategory())
                .expenseSubcategory(ExpenseSubcategoryCreator.createValidExpenseSubcategory())
                .build();
    }

    public static ExpenseResponse createValidExpenseResponse(){
        return ExpenseResponse.builder()
                .expenseId(1)
                .expenseDescription("Pizza Calabresa")
                .expenseAmount(new BigDecimal(130.00))
                .expenseMethod(ExpenseMethodCreator.createValidExpenseMethod().getExpenseMethodName())
                .recurrence(RecurrenceType.NONRECURRING)
                .isEssential(false)
                .categoryName(ExpenseCategoryCreator.createValidExpenseCategory().getCategoryName())
                .subCategoryName(ExpenseSubcategoryCreator.createValidExpenseSubcategory().getSubCategoryName())
                .build();
    }

    public static Expense createExpenseToBeSaved(){
        Recurrence recurrence = Recurrence.builder()
                .recurrenceId(6)
                .recurrenceFrequency(RecurrenceType.NONRECURRING)
                .build();
        return Expense.builder()
                .expenseDescription("Pizza Calabresa")
                .expenseAmount(new BigDecimal(130.00))
                .expenseMethod(ExpenseMethodCreator.createValidExpenseMethod())
                .recurrence(recurrence)
                .isEssential(false)
                .expenseCategory(ExpenseCategoryCreator.createValidExpenseCategory())
                .expenseSubcategory(ExpenseSubcategoryCreator.createValidExpenseSubcategory())
                .build();
    }
}
