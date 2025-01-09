package com.yuri.freire.Cash_Stream.Expense.entities.repositories;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Integer> {

    @Query("""
       SELECT exc 
       FROM ExpenseCategory exc
       WHERE exc.expenseCategoryId = :expenseCategoryId
       AND exc.deletedAt IS NULL
       """)
    Optional<ExpenseCategory> findByCategoryId(@Param("expenseCategoryId") Integer expenseCategoryId);

    @Query("""
       SELECT exc
       FROM ExpenseCategory exc
       WHERE exc.categoryName = :categoryName
       AND exc.deletedAt IS NULL
       """)
    Optional<ExpenseCategory> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("""
       SELECT new com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryResponse(
       exc.expenseCategoryId,
       exc.categoryName)
       FROM ExpenseCategory exc
       WHERE exc.deletedAt IS NULL
       """)
    Page<ExpenseCategoryResponse> findAllCategoryExpenses(Pageable pageable);
}
