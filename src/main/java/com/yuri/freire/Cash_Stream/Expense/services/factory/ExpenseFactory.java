package com.yuri.freire.Cash_Stream.Expense.services.factory;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.*;
import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseMethod;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseSubcategory;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseFactory {

    public Expense createExpense(ExpenseRequest expenseRequest,
                                 ExpenseCategory expenseCategory,
                                 ExpenseSubcategory expenseSubcategory,
                                 ExpenseMethod expenseMethod,
                                 Recurrence recurrence){
        return Expense.builder()
                .expenseDescription(expenseRequest.getExpenseDescription())
                .expenseAmount(expenseRequest.getExpenseAmount())
                .isEssential(expenseRequest.isEssential())
                .expenseMethod(expenseMethod)
                .recurrence(recurrence)
                .expenseCategory(expenseCategory)
                .expenseSubcategory(expenseSubcategory)
                .build();
    }

    public ExpenseCategory createExpenseCategory(ExpenseCategoryRequest expenseCategoryRequest){
        return ExpenseCategory.builder()
                .categoryName(expenseCategoryRequest.getCategoryName())
                .build();
    }

    public ExpenseSubcategory createExpenseSubcategory(ExpenseSubcategoryRequest expenseSubcategoryRequest, ExpenseCategory expenseCategory){
        return ExpenseSubcategory.builder()
                .subCategoryName(expenseSubcategoryRequest.getSubcategoryName())
                .expenseCategory(expenseCategory)
                .build();
    }

    public ExpenseResponse createExpenseResponse(Expense expense){
        return ExpenseResponse.builder()
                .expenseId(expense.getExpenseId())
                .expenseDescription(expense.getExpenseDescription())
                .expenseAmount(expense.getExpenseAmount())
                .expenseMethod(expense.getExpenseMethod().getExpenseMethodName())
                .recurrence(expense.getRecurrence().getRecurrenceFrequency())
                .categoryName(expense.getExpenseCategory().getCategoryName())
                .subCategoryName(expense.getExpenseSubcategory().getSubCategoryName())
                .build();
    }

    public ExpenseCategoryResponse createExpenseCategoryResponse(ExpenseCategory expenseCategory){
        return ExpenseCategoryResponse.builder()
                .expenseCategoryId(expenseCategory.getExpenseCategoryId())
                .categoryName(expenseCategory.getCategoryName())
                .build();
    }

    public ExpenseSubcategoryResponse createExpenseSubcategoryResponse(ExpenseSubcategory expenseSubcategory){
        return ExpenseSubcategoryResponse.builder()
                .expenseSubcategoryId(expenseSubcategory.getExpenseSubcategoryId())
                .subCategoryName(expenseSubcategory.getSubCategoryName())
                .categoryName(expenseSubcategory.getExpenseCategory().getCategoryName())
                .build();
    }

}
