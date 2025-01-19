package com.yuri.freire.Cash_Stream.Expense.services;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.services.JwtService;
import com.yuri.freire.Cash_Stream.Authentication.services.UserService;
import com.yuri.freire.Cash_Stream.Exceptions.AlreadyExistsException;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseSubcategory;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseSubcategoryRepository;
import com.yuri.freire.Cash_Stream.Expense.services.facade.ExpenseFacade;
import com.yuri.freire.Cash_Stream.Expense.services.factory.ExpenseFactory;
import com.yuri.freire.Cash_Stream.Utils.CookieUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpenseSubcategoryService {
    private final ExpenseSubcategoryRepository expenseSubcategoryRepository;
    private final ExpenseFactory expenseFactory;
    private final ExpenseCategoryService expenseCategoryService;
    private final JwtService jwtService;
    private final UserService userService;

    public ExpenseSubcategoryResponse createExpenseSubcategory(ExpenseSubcategoryRequest expenseSubcategoryRequest, HttpServletRequest request){
        String username = this.extractUsername(request);
        Optional<ExpenseSubcategory> fectchedExpenseSubcategory = expenseSubcategoryRepository.findBySubCategoryName(username, expenseSubcategoryRequest.getSubcategoryName());
        User fetchedUser = userService.findUserByUsername(username);
        if(fectchedExpenseSubcategory.isPresent()){
            throw new AlreadyExistsException("Expense subcategory with name '"
                    + expenseSubcategoryRequest.getSubcategoryName() + "' already exists");
        }
        ExpenseCategory fetchedExpenseCategory = expenseCategoryService.findByCategoryName(expenseSubcategoryRequest.getCategoryName(), username);
        ExpenseSubcategory expenseSubcategory = expenseFactory.createExpenseSubcategory(expenseSubcategoryRequest, fetchedExpenseCategory, fetchedUser);
        ExpenseSubcategory savedExpenseSubcategory = expenseSubcategoryRepository.save(expenseSubcategory);
        return expenseFactory.createExpenseSubcategoryResponse(savedExpenseSubcategory);
    }
//oi amor, te amo. vc codifica programas, mas decodifica meu coração s2
    public ExpenseSubcategory findBySubcategoryName(String subcategoryName, HttpServletRequest request){
        String username = this.extractUsername(request);
        return expenseSubcategoryRepository.findBySubCategoryName(username, subcategoryName)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + subcategoryName));
    }

    public Page<ExpenseSubcategoryResponse> findAllSubcategoryExpenses(Pageable pageable, HttpServletRequest request){
        String username = this.extractUsername(request);
        return expenseSubcategoryRepository.findAllSubcategoryExpenses(username, pageable);
    }

    public Page<ExpenseSubcategoryResponse> findAllSubcategoryExpensesByCategory(String categoryName, Pageable pageable, HttpServletRequest request){
        String username = this.extractUsername(request);
        ExpenseCategory expenseCategory = expenseCategoryService.findByCategoryName(categoryName, username);
        return expenseSubcategoryRepository.findAllSubcategoryExpensesByCategory(username, expenseCategory.getCategoryName(), pageable);
    }

    public String deleteBySubcategoryId(Integer subcategoryId, HttpServletRequest request){
        String username = this.extractUsername(request);
        ExpenseSubcategory expenseSubcategory = expenseSubcategoryRepository.findBySubcategoryId(username, subcategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + subcategoryId));
        expenseSubcategoryRepository.deleteById(subcategoryId);
        return expenseSubcategory.getSubCategoryName();
    }

    public String extractUsername(HttpServletRequest request){
        String jwtFromCookie = CookieUtils.getJwtFromCookie(request);
        return jwtService.extractUsername(jwtFromCookie);
    }
}
//fim?
//eu amo o meu amor. meu homenzarrão