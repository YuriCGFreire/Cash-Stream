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

    @Query("""
            SELECT esc
            FROM ExpenseSubcategory esc
            WHERE esc.expenseSubcategoryId = :expenseSubcategoryId
            AND esc.deletedAt IS NULL
            """)
    Optional<ExpenseSubcategory> findBySubcategoryId(@Param("expenseSubcategoryId") Integer expenseSubcategoryId);
    @Query("""
            SELECT esc
            FROM ExpenseSubcategory esc
            WHERE esc.subCategoryName = :subCategoryName
            AND esc.deletedAt IS NULL
            """)
    Optional<ExpenseSubcategory> findBySubCategoryName(@Param("subCategoryName") String subCategoryName);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse(
            esc.expenseSubcategoryId,
            esc.subCategoryName,
            esc.expenseCategory.categoryName
            )
            FROM ExpenseSubcategory esc
            JOIN esc.expenseCategory 
            WHERE esc.deletedAt IS NULL
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
            AND esc.deletedAt IS NULL
            """)
    Page<ExpenseSubcategoryResponse> findAllSubcategoryExpensesByCategory(@Param("categoryName") String categoryName, Pageable pageable);
}
