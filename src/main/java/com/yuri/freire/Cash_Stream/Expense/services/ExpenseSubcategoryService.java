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
import com.yuri.freire.Cash_Stream.Utils.SecurityUtils;
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

    public ExpenseSubcategoryResponse createExpenseSubcategory(ExpenseSubcategoryRequest expenseSubcategoryRequest, String username){
        Optional<ExpenseSubcategory> fectchedExpenseSubcategory = expenseSubcategoryRepository.findBySubCategoryName(expenseSubcategoryRequest.getSubcategoryName(), username);
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
    public ExpenseSubcategory findBySubcategoryName(String subcategoryName, String username){
        return expenseSubcategoryRepository.findBySubCategoryName(subcategoryName, username)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + subcategoryName));
    }

    public Page<ExpenseSubcategoryResponse> findAllSubcategoryExpenses(Pageable pageable, String username){
        return expenseSubcategoryRepository.findAllSubcategoryExpenses(pageable, username);
    }

    public Page<ExpenseSubcategoryResponse> findAllSubcategoryExpensesByCategory(String categoryName, Pageable pageable, String username){
        ExpenseCategory expenseCategory = expenseCategoryService.findByCategoryName(categoryName, username);
        return expenseSubcategoryRepository.findAllSubcategoryExpensesByCategory(expenseCategory.getCategoryName(), pageable, username);
    }

    public String deleteBySubcategoryId(Integer subcategoryId, String username){
        ExpenseSubcategory expenseSubcategory = expenseSubcategoryRepository.findBySubcategoryId(subcategoryId, username)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + subcategoryId));
        expenseSubcategoryRepository.deleteById(subcategoryId);
        return expenseSubcategory.getSubCategoryName();
    }
}
//fim?
//eu amo o meu amor. meu homenzarrão