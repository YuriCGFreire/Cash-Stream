package com.yuri.freire.Cash_Stream.Expense.services;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseMethod;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseRepository;
import com.yuri.freire.Cash_Stream.Expense.services.facade.ExpenseFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseFacade expenseFacade;
    public ExpenseResponse createExpense(ExpenseRequest expenseRequest){
        Expense expense = expenseFacade.createExpense(expenseRequest);
        Expense savedExpense = expenseRepository.save(expense);
        return expenseFacade.createExpenseResponse(savedExpense);
    }

    public Page<ExpenseResponse> findAllExpenses(Pageable pageable){
        return expenseRepository.findAllExpenses(pageable);
    }

    public Page<ExpenseResponse> findAllExpensesByCategoryName(String categoryName, Pageable pageable){
//      Validação da category
        String expenseCategoryName = expenseFacade.findExpenseCategoryByName(categoryName).getCategoryName();
        return expenseRepository.findAllByCategory(expenseCategoryName, pageable);
    }

    public Page<ExpenseResponse> findAllBySubcategoryName(String subcategoryName, Pageable pageable){
//      Validação de subcategory
        String expenseSubcategoryByName = expenseFacade.findExpenseSubcategoryByName(subcategoryName).getSubCategoryName();
        return expenseRepository.findAllBySubcategory(expenseSubcategoryByName, pageable);
    }

    public Page<ExpenseResponse> findAllExpensesByPaymentMethod(ExpenseMethodType methodType, Pageable pageable){
        ExpenseMethodType expenseMethodType = expenseFacade.findExpenseMethod(methodType).getExpenseMethodName();
        return expenseRepository.findAllBYPaymentMethod(expenseMethodType, pageable);
    }

    public Page<ExpenseResponse> findAllExpensesByIsEssential(boolean isEssential, Pageable pageable){
        return expenseRepository.findAllByEssentiality(isEssential, pageable);
    }
}
