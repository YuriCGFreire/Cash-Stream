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
            AND esc.user.username = :username
            """)
    Optional<ExpenseSubcategory> findBySubcategoryId(@Param("username") String username, @Param("expenseSubcategoryId") Integer expenseSubcategoryId);
    @Query("""
            SELECT esc
            FROM ExpenseSubcategory esc
            WHERE esc.subCategoryName = :subCategoryName
            AND esc.deletedAt IS NULL
            AND esc.user.username = :username
            """)
    Optional<ExpenseSubcategory> findBySubCategoryName(@Param("username") String username, @Param("subCategoryName") String subCategoryName);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse(
            esc.expenseSubcategoryId,
            esc.subCategoryName,
            esc.expenseCategory.categoryName,
            esc.user.username
            )
            FROM ExpenseSubcategory esc
            JOIN esc.expenseCategory 
            JOIN esc.user
            WHERE esc.deletedAt IS NULL
            AND esc.user.username = :username
            """)
    Page<ExpenseSubcategoryResponse> findAllSubcategoryExpenses(@Param("username") String username, Pageable pageable);

    @Query("""
            select new com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse(
            esc.expenseSubcategoryId,
            esc.subCategoryName,
            esc.expenseCategory.categoryName,
            esc.user.username
            ) 
            FROM ExpenseSubcategory esc
            JOIN esc.expenseCategory exc
            JOIN esc.user
            WHERE exc.categoryName = :categoryName
            AND esc.deletedAt IS NULL
            AND esc.user.username = :username
            """)
    Page<ExpenseSubcategoryResponse> findAllSubcategoryExpensesByCategory(@Param("username") String username, @Param("categoryName") String categoryName, Pageable pageable);
}
