package com.yuri.freire.Cash_Stream.Expense.entities.repositories;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    @Query("""
            SELECT ex
            FROM Expense ex
            WHERE ex.expenseId = :expenseId
            AND ex.deletedAt IS NULL
            """)
    Optional<Expense> findExpenseById(@Param("expenseId") Integer expenseId);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse(
            ex.expenseId,
            ex.expenseDescription,
            ex.expenseAmount,
            ex.expenseDate,
            ex.isEssential,
            ex.expenseMethod.expenseMethodName,
            ex.recurrence.recurrenceFrequency,
            ex.expenseCategory.categoryName,
            ex.expenseSubcategory.subCategoryName
            )
            FROM Expense ex
            JOIN ex.expenseMethod
            JOIN ex.recurrence
            JOIN ex.expenseCategory
            JOIN ex.expenseSubcategory
            WHERE ex.deletedAt IS NULL
            """)
    Page<ExpenseResponse>  findAllExpenses(Pageable pageable);
    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse(
            ex.expenseId,
            ex.expenseDescription,
            ex.expenseAmount,
            ex.expenseDate,
            ex.isEssential,
            ex.expenseMethod.expenseMethodName,
            ex.recurrence.recurrenceFrequency,
            ex.expenseCategory.categoryName,
            ex.expenseSubcategory.subCategoryName
            )
            FROM Expense ex
            JOIN ex.expenseMethod
            JOIN ex.recurrence
            JOIN ex.expenseCategory exc
            JOIN ex.expenseSubcategory
            WHERE exc.categoryName = :categoryName
            AND ex.deletedAt IS NULL   
            """)
    Page<ExpenseResponse> findAllByCategory(@Param("categoryName") String categoryName, Pageable pageable);
    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse(
            ex.expenseId,
            ex.expenseDescription,
            ex.expenseAmount,
            ex.expenseDate,
            ex.isEssential,
            ex.expenseMethod.expenseMethodName,
            ex.recurrence.recurrenceFrequency,
            ex.expenseCategory.categoryName,
            ex.expenseSubcategory.subCategoryName
            )
            FROM Expense ex
            JOIN ex.expenseMethod
            JOIN ex.recurrence
            JOIN ex.expenseCategory 
            JOIN ex.expenseSubcategory esc
            WHERE esc.subCategoryName = :subCategoryName
            AND ex.deletedAt IS NULL         
            """)
    Page<ExpenseResponse> findAllBySubcategory(@Param("subCategoryName") String subCategoryName, Pageable pageable);
    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse(
            ex.expenseId,
            ex.expenseDescription,
            ex.expenseAmount,
            ex.expenseDate,
            ex.isEssential,
            ex.expenseMethod.expenseMethodName,
            ex.recurrence.recurrenceFrequency,
            ex.expenseCategory.categoryName,
            ex.expenseSubcategory.subCategoryName
            )
            FROM Expense ex
            JOIN ex.expenseMethod exm
            JOIN ex.recurrence
            JOIN ex.expenseCategory 
            JOIN ex.expenseSubcategory 
            WHERE exm.expenseMethodName = :expenseMethodName
            AND ex.deletedAt IS NULL   
            """)
    Page<ExpenseResponse> findAllBYPaymentMethod(@Param("expenseMethodName") ExpenseMethodType expenseMethodName, Pageable pageable);
    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse(
            ex.expenseId,
            ex.expenseDescription,
            ex.expenseAmount,
            ex.expenseDate,
            ex.isEssential,
            ex.expenseMethod.expenseMethodName,
            ex.recurrence.recurrenceFrequency,
            ex.expenseCategory.categoryName,
            ex.expenseSubcategory.subCategoryName
            )
            FROM Expense ex
            JOIN ex.expenseMethod
            JOIN ex.recurrence
            JOIN ex.expenseCategory
            JOIN ex.expenseSubcategory
            WHERE ex.isEssential = :isEssential
            AND ex.deletedAt IS NULL   
            """)
    Page<ExpenseResponse> findAllByEssentiality(@Param("isEssential") boolean isEssential, Pageable pageable);
}
