package com.yuri.freire.Cash_Stream.Expense.services.facade;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.services.JwtService;
import com.yuri.freire.Cash_Stream.Authentication.services.UserService;
import com.yuri.freire.Cash_Stream.Exceptions.AlreadyExistsException;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryRequest;
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
import com.yuri.freire.Cash_Stream.Utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseFacade {
    private final ExpenseCategoryService expenseCategoryService;
    private final UserService userService;
    private final JwtService jwtService;
    private final ExpenseSubcategoryService expenseSubcategoryService;
    private final ExpenseMethodService expenseMethodService;
    private final RecurrenceService recurrenceService;
    private final ExpenseFactory expenseFactory;

    public Expense createExpense(ExpenseRequest expenseRequest, HttpServletRequest request){
        String username = extractUsernameFromCookie(request);
        User user = findUserByName(username);
        ExpenseCategory expenseCategory = expenseCategoryService.findByCategoryName(expenseRequest.getExpenseCategory(), username);
        ExpenseSubcategory expenseSubcategory = expenseSubcategoryService.findBySubcategoryName(expenseRequest.getExpenseSubcategory(), request);
        ExpenseMethod expenseMethod = expenseMethodService.findByExpenseMethodName(expenseRequest.getExpenseMethod());
        Recurrence recurrence = recurrenceService.findByRecurrenFrequency(expenseRequest.getRecurrence());

        return expenseFactory.createExpense(expenseRequest, expenseCategory, expenseSubcategory, expenseMethod, recurrence, user);
    }

    public ExpenseCategory findExpenseCategoryByName(String categoryName, String username){
        return expenseCategoryService.findByCategoryName(categoryName, username);
    }

    public ExpenseSubcategory findExpenseSubcategoryByName(String subcategoryName,HttpServletRequest request){
        return expenseSubcategoryService.findBySubcategoryName(subcategoryName, request);
    }

    public ExpenseMethod findExpenseMethod(ExpenseMethodType expenseMethodType){
        return expenseMethodService.findByExpenseMethodName(expenseMethodType);
    }
    public ExpenseResponse createExpenseResponse(Expense expense){
        return expenseFactory.createExpenseResponse(expense);
    }

    public User findUserByName(String username){
        return userService.findUserByUsername(username);
    }

    public String extractUsernameFromCookie(HttpServletRequest request){
        String jwtFromCookie = CookieUtils.getJwtFromCookie(request);
        return jwtService.extractUsername(jwtFromCookie);
    }
}
