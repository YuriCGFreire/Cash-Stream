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
            AND ex.user.username = :username
            """)
    Optional<Expense> findExpenseById(@Param("expenseId") Integer expenseId, @Param("username") String username);

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
            ex.expenseSubcategory.subCategoryName,
            ex.user.username
            )
            FROM Expense ex
            JOIN ex.expenseMethod
            JOIN ex.recurrence
            JOIN ex.expenseCategory
            JOIN ex.expenseSubcategory
            JOIN ex.user
            WHERE ex.deletedAt IS NULL
            AND ex.user.username = :username
            """)
    Page<ExpenseResponse>  findAllExpenses(Pageable pageable, @Param("username") String username);
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
            ex.expenseSubcategory.subCategoryName,
            ex.user.username
            )
            FROM Expense ex
            JOIN ex.expenseMethod
            JOIN ex.recurrence
            JOIN ex.expenseCategory exc
            JOIN ex.expenseSubcategory
            JOIN ex.user
            WHERE exc.categoryName = :categoryName
            AND ex.deletedAt IS NULL   
            AND ex.user.username = :username
            """)
    Page<ExpenseResponse> findAllByCategory(@Param("categoryName") String categoryName, Pageable pageable, @Param("username") String username);
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
            ex.expenseSubcategory.subCategoryName,
            ex.user.username
            )
            FROM Expense ex
            JOIN ex.expenseMethod
            JOIN ex.recurrence
            JOIN ex.expenseCategory 
            JOIN ex.expenseSubcategory esc
            JOIN ex.user
            WHERE esc.subCategoryName = :subCategoryName
            AND ex.deletedAt IS NULL    
            AND ex.user.username = :username     
            """)
    Page<ExpenseResponse> findAllBySubcategory(@Param("subCategoryName") String subCategoryName, Pageable pageable, @Param("username") String username);
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
            ex.expenseSubcategory.subCategoryName,
            ex.user.username
            )
            FROM Expense ex
            JOIN ex.expenseMethod exm
            JOIN ex.recurrence
            JOIN ex.expenseCategory 
            JOIN ex.expenseSubcategory
            JOIN ex.user 
            WHERE exm.expenseMethodName = :expenseMethodName
            AND ex.deletedAt IS NULL   
            AND ex.user.username = :username
            """)
    Page<ExpenseResponse> findAllBYPaymentMethod(@Param("expenseMethodName") ExpenseMethodType expenseMethodName, Pageable pageable, @Param("username") String username);
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
            ex.expenseSubcategory.subCategoryName,
            ex.user.username
            )
            FROM Expense ex
            JOIN ex.expenseMethod
            JOIN ex.recurrence
            JOIN ex.expenseCategory
            JOIN ex.expenseSubcategory
            JOIN ex.user
            WHERE ex.isEssential = :isEssential
            AND ex.deletedAt IS NULL  
            AND ex.user.username = :username 
            """)
    Page<ExpenseResponse> findAllByEssentiality(@Param("isEssential") boolean isEssential, Pageable pageable, @Param("username") String username);
}
