package com.yuri.freire.Cash_Stream.Expense.services;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.services.JwtService;
import com.yuri.freire.Cash_Stream.Authentication.services.UserService;
import com.yuri.freire.Cash_Stream.Exceptions.AlreadyExistsException;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseCategoryRepository;
import com.yuri.freire.Cash_Stream.Expense.services.facade.ExpenseFacade;
import com.yuri.freire.Cash_Stream.Expense.services.factory.ExpenseFactory;
import com.yuri.freire.Cash_Stream.Utils.CookieUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryService {
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final ExpenseFactory expenseFactory;
    private final JwtService jwtService;
    private final UserService userService;

    public ExpenseCategoryResponse createExpenseCategory(@Valid ExpenseCategoryRequest expenseCategoryRequest, HttpServletRequest request){
        String username = this.extractUsername(request);
        Optional<ExpenseCategory> fetchedExpenseCategory = expenseCategoryRepository.findByCategoryName(expenseCategoryRequest.getCategoryName(), username);
        User user = userService.findUserByUsername(username);
        if(fetchedExpenseCategory.isPresent()){
            throw new AlreadyExistsException("Expense category with name '"
                    + expenseCategoryRequest.getCategoryName() + "' already exists");
        }
        ExpenseCategory expenseCategory = expenseFactory.createExpenseCategory(expenseCategoryRequest, user);
        ExpenseCategory savedExpenseCategory = expenseCategoryRepository.save(expenseCategory);
        return expenseFactory.createExpenseCategoryResponse(savedExpenseCategory);
    }

    public Page<ExpenseCategoryResponse> findAllCategoryExpenses(Pageable pageable, HttpServletRequest request){
        String username = this.extractUsername(request);
        return expenseCategoryRepository.findAllCategoryExpenses(username, pageable);
    }

    public ExpenseCategory findByCategoryName(String categoryName, String username){
        return expenseCategoryRepository
                .findByCategoryName(username, categoryName)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryName));
    }

    public String deleteByCategoryId(Integer categoryId, HttpServletRequest request){
        String username = this.extractUsername(request);
        ExpenseCategory expenseCategory = expenseCategoryRepository.findByCategoryId(username, categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
        expenseCategoryRepository.deleteById(categoryId);
        return expenseCategory.getCategoryName();
    }

    public String extractUsername(HttpServletRequest request){
        String jwtFromCookie = CookieUtils.getJwtFromCookie(request);
        return jwtService.extractUsername(jwtFromCookie);
    }

}
