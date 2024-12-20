package com.yuri.freire.Cash_Stream.Expense.services.facade;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseMethod;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseSubcategory;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Expense.services.ExpenseCategoryService;
import com.yuri.freire.Cash_Stream.Expense.services.ExpenseMethodService;
import com.yuri.freire.Cash_Stream.Expense.services.ExpenseSubcategoryService;
import com.yuri.freire.Cash_Stream.Expense.services.factory.ExpenseFactory;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.services.RecurrenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseFacade {
    private final ExpenseCategoryService expenseCategoryService;

    private final ExpenseSubcategoryService expenseSubcategoryService;

    private final ExpenseMethodService expenseMethodService;

    private final RecurrenceService recurrenceService;

    private final ExpenseFactory expenseFactory;

    public Expense createExpense(ExpenseRequest expenseRequest){
        ExpenseCategory expenseCategory = expenseCategoryService.findByCategoryName(expenseRequest.getExpenseCategory());
        ExpenseSubcategory expenseSubcategory = expenseSubcategoryService.findBySubcategoryName(expenseRequest.getExpenseSubcategory());
        ExpenseMethod expenseMethod = expenseMethodService.findByExpenseMethodName(expenseRequest.getExpenseMethod());
        Recurrence recurrence = recurrenceService.findByRecurrenFrequency(expenseRequest.getRecurrence());

        return expenseFactory.createExpense(expenseRequest, expenseCategory, expenseSubcategory, expenseMethod, recurrence);
    }

    public ExpenseCategory findExpenseCategoryByName(String categoryName){
        return expenseCategoryService.findByCategoryName(categoryName);
    }

    public ExpenseSubcategory findExpenseSubcategoryByName(String subcategoryName){
        return expenseSubcategoryService.findBySubcategoryName(subcategoryName);
    }

    public ExpenseMethod findExpenseMethod(ExpenseMethodType expenseMethodType){
        return expenseMethodService.findByExpenseMethodName(expenseMethodType);
    }
    public ExpenseResponse createExpenseResponse(Expense expense){
        return expenseFactory.createExpenseResponse(expense);
    }
}
