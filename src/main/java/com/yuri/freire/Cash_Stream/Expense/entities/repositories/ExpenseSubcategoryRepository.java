package com.yuri.freire.Cash_Stream.Expense.entities.repositories;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseSubcategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExpenseSubcategoryRepository extends JpaRepository<ExpenseSubcategory, Integer> {
    Optional<ExpenseSubcategory> findBySubCategoryName(String expenseSubcategoryName);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse(
            esc.expenseSubcategoryId,
            esc.subCategoryName,
            esc.expenseCategory.categoryName
            )
            FROM ExpenseSubcategory esc
            JOIN esc.expenseCategory 
            """)
    Page<ExpenseSubcategoryResponse> findAllSubcategoryExpenses(Pageable pageable);

    @Query("""
            select new com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse(
            esc.expenseSubcategoryId,
            esc.subCategoryName,
            esc.expenseCategory.categoryName
            ) 
            FROM ExpenseSubcategory esc
            JOIN esc.expenseCategory exc
            WHERE exc.categoryName = :categoryName
            """)
    Page<ExpenseSubcategoryResponse> findAllSubcategoryExpensesByCategory(@Param("categoryName") String categoryName, Pageable pageable);
}
