package com.yuri.freire.Cash_Stream.util.expense;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import com.yuri.freire.Cash_Stream.util.user.UserCreator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

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
                .expenseDate(LocalDate.parse("2025-03-01"))
                .expenseCategory(ExpenseCategoryCreator.createValidExpenseCategory())
                .expenseSubcategory(ExpenseSubcategoryCreator.createValidExpenseSubcategory())
                .user(UserCreator.createValidUser())
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
                .expenseDate(LocalDate.parse("2025-03-01"))
                .categoryName(ExpenseCategoryCreator.createValidExpenseCategory().getCategoryName())
                .subCategoryName(ExpenseSubcategoryCreator.createValidExpenseSubcategory().getSubCategoryName())
                .username(UserCreator.createValidUser().getUsername())
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
                .expenseDate(LocalDate.parse("2025-03-01"))
                .expenseCategory(ExpenseCategoryCreator.createValidExpenseCategory())
                .expenseSubcategory(ExpenseSubcategoryCreator.createValidExpenseSubcategory())
                .user(UserCreator.createValidUser())
                .build();
    }
}
