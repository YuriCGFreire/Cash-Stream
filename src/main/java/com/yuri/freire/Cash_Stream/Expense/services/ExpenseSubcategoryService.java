package com.yuri.freire.Cash_Stream.Expense.services;

import com.yuri.freire.Cash_Stream.Exceptions.AlreadyExistsException;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseSubcategory;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseSubcategoryRepository;
import com.yuri.freire.Cash_Stream.Expense.services.factory.ExpenseFactory;
import jakarta.persistence.EntityNotFoundException;
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

    public ExpenseSubcategoryResponse createExpenseSubcategory(ExpenseSubcategoryRequest expenseSubcategoryRequest){
        Optional<ExpenseSubcategory> fectchedExpenseSubcategory = expenseSubcategoryRepository.findBySubCategoryName(expenseSubcategoryRequest.getSubcategoryName());
        if(fectchedExpenseSubcategory.isPresent()){
            throw new AlreadyExistsException("Expense subcategory with name '"
                    + expenseSubcategoryRequest.getSubcategoryName() + "' already exists");
        }
        ExpenseCategory fetchedExpenseCategory = expenseCategoryService.findByCategoryName(expenseSubcategoryRequest.getCategoryName());
        ExpenseSubcategory expenseSubcategory = expenseFactory.createExpenseSubcategory(expenseSubcategoryRequest, fetchedExpenseCategory);
        ExpenseSubcategory savedExpenseSubcategory = expenseSubcategoryRepository.save(expenseSubcategory);
        return expenseFactory.createExpenseSubcategoryResponse(savedExpenseSubcategory);
    }
//oi amor, te amo. vc codifica programas, mas decodifica meu coração s2
    public ExpenseSubcategory findBySubcategoryName(String subcategoryName){
        ExpenseSubcategory expenseSubcategory = expenseSubcategoryRepository.findBySubCategoryName(subcategoryName)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + subcategoryName));
        return expenseSubcategory;
    }

    public Page<ExpenseSubcategoryResponse> findAllSubcategoryExpenses(Pageable pageable){
        return expenseSubcategoryRepository.findAllSubcategoryExpenses(pageable);
    }

    public Page<ExpenseSubcategoryResponse> findAllSubcategoryExpensesByCategory(String categoryName, Pageable pageable){
        ExpenseCategory expenseCategory = expenseCategoryService.findByCategoryName(categoryName);
        return expenseSubcategoryRepository.findAllSubcategoryExpensesByCategory(expenseCategory.getCategoryName(), pageable);
    }

    public String deleteBySubcategoryId(Integer subcategoryId){
        ExpenseSubcategory expenseSubcategory = expenseSubcategoryRepository.findBySubcategoryId(subcategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + subcategoryId));
        expenseSubcategoryRepository.deleteById(subcategoryId);
        return expenseSubcategory.getSubCategoryName();
    }
}
//fim?
//eu amo o meu amor. meu homenzarrão