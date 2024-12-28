package com.yuri.freire.Cash_Stream.Expense.services;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseCategoryRepository;
import com.yuri.freire.Cash_Stream.Expense.services.factory.ExpenseFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryService {
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final ExpenseFactory expenseFactory;

    public ExpenseCategoryResponse createExpenseCategory(@Valid ExpenseCategoryRequest expenseCategoryRequest){
        ExpenseCategory expenseCategory = expenseFactory.createExpenseCategory(expenseCategoryRequest);
        ExpenseCategory expenseCategorySaved = expenseCategoryRepository.save(expenseCategory);
        return expenseFactory.createExpenseCategoryResponse(expenseCategorySaved);
    }

    public Page<ExpenseCategoryResponse> findAllCategoryExpenses(Pageable pageable){
        return expenseCategoryRepository.findAllCategoryExpenses(pageable);
    }

    public ExpenseCategory findByCategoryName(String categoryName){
        return expenseCategoryRepository
                .findByCategoryName(categoryName)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryName));
    }

    public String deleteByCategoryId(Integer categoryId){
        ExpenseCategory expenseCategory = expenseCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
        expenseCategoryRepository.deleteById(categoryId);
        return expenseCategory.getCategoryName();
    }
}
