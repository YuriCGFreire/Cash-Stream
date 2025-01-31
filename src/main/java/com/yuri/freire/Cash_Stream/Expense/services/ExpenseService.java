package com.yuri.freire.Cash_Stream.Expense.services;

import com.yuri.freire.Cash_Stream.Authentication.services.JwtService;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseRepository;
import com.yuri.freire.Cash_Stream.Expense.services.facade.ExpenseFacade;
import com.yuri.freire.Cash_Stream.Utils.CookieUtils;
import com.yuri.freire.Cash_Stream.Utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseFacade expenseFacade;
    public ExpenseResponse createExpense(ExpenseRequest expenseRequest, String username){
        Expense expense = expenseFacade.createExpense(expenseRequest, username);
        Expense savedExpense = expenseRepository.save(expense);
        return expenseFacade.createExpenseResponse(savedExpense);
    }

    public Page<ExpenseResponse> findAllExpenses(Pageable pageable, String username){
        return expenseRepository.findAllExpenses(pageable, username);
    }

    public Page<ExpenseResponse> findAllExpensesByCategoryName(String categoryName, Pageable pageable, String username){
//      Validação da category
        String expenseCategoryName = expenseFacade.findExpenseCategoryByName(categoryName, username).getCategoryName();
        return expenseRepository.findAllByCategory(expenseCategoryName, pageable, username);
    }

    public Page<ExpenseResponse> findAllBySubcategoryName(String subcategoryName, Pageable pageable, String username){
//      Validação de subcategory
        String expenseSubcategoryByName = expenseFacade.findExpenseSubcategoryByName(subcategoryName, username).getSubCategoryName();
        return expenseRepository.findAllBySubcategory(expenseSubcategoryByName, pageable, username);
    }

    public Page<ExpenseResponse> findAllExpensesByPaymentMethod(ExpenseMethodType methodType, Pageable pageable, String username){
        ExpenseMethodType expenseMethodType = expenseFacade.findExpenseMethod(methodType).getExpenseMethodName();
        return expenseRepository.findAllBYPaymentMethod(expenseMethodType, pageable, username);
    }

    public Page<ExpenseResponse> findAllExpensesByIsEssential(boolean isEssential, Pageable pageable, String username){
        return expenseRepository.findAllByEssentiality(isEssential, pageable, username);
    }

    public String softDeleteExpense(Integer expenseId, String username){
        Expense expense = expenseRepository.findExpenseById(expenseId, username)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found with id: " + expenseId));
        expenseRepository.delete(expense);
        return expense.getExpenseDescription();
    }

}
