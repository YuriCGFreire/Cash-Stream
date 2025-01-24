package com.yuri.freire.Cash_Stream.Expense.services;

import com.yuri.freire.Cash_Stream.Authentication.services.JwtService;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseRepository;
import com.yuri.freire.Cash_Stream.Expense.services.facade.ExpenseFacade;
import com.yuri.freire.Cash_Stream.Utils.CookieUtils;
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
    public ExpenseResponse createExpense(ExpenseRequest expenseRequest, HttpServletRequest request){
        Expense expense = expenseFacade.createExpense(expenseRequest, request);
        Expense savedExpense = expenseRepository.save(expense);
        return expenseFacade.createExpenseResponse(savedExpense);
    }

    public Page<ExpenseResponse> findAllExpenses(Pageable pageable, HttpServletRequest request ){
        String username = expenseFacade.extractUsernameFromCookie(request);
        return expenseRepository.findAllExpenses(username, pageable);
    }

    public Page<ExpenseResponse> findAllExpensesByCategoryName(HttpServletRequest request, String categoryName, Pageable pageable){
//      Validação da category
        String username = expenseFacade.extractUsernameFromCookie(request);
        String expenseCategoryName = expenseFacade.findExpenseCategoryByName(categoryName, username).getCategoryName();
        return expenseRepository.findAllByCategory(username, expenseCategoryName, pageable);
    }

    public Page<ExpenseResponse> findAllBySubcategoryName(HttpServletRequest request, String subcategoryName, Pageable pageable){
//      Validação de subcategory
        String username = expenseFacade.extractUsernameFromCookie(request);
        String expenseSubcategoryByName = expenseFacade.findExpenseSubcategoryByName(subcategoryName, request).getSubCategoryName();
        return expenseRepository.findAllBySubcategory(username, expenseSubcategoryByName, pageable);
    }

    public Page<ExpenseResponse> findAllExpensesByPaymentMethod(HttpServletRequest request, ExpenseMethodType methodType, Pageable pageable){
        String username = expenseFacade.extractUsernameFromCookie(request);
        ExpenseMethodType expenseMethodType = expenseFacade.findExpenseMethod(methodType).getExpenseMethodName();
        return expenseRepository.findAllBYPaymentMethod(username, expenseMethodType, pageable);
    }

    public Page<ExpenseResponse> findAllExpensesByIsEssential(HttpServletRequest request, boolean isEssential, Pageable pageable){
        String username = expenseFacade.extractUsernameFromCookie(request);
        return expenseRepository.findAllByEssentiality(username, isEssential, pageable);
    }

    public String softDeleteExpense(HttpServletRequest request, Integer expenseId){
        String username = expenseFacade.extractUsernameFromCookie(request);
        Expense expense = expenseRepository.findExpenseById(username, expenseId)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found with id: " + expenseId));
        expenseRepository.delete(expense);
        return expense.getExpenseDescription();
    }

}
