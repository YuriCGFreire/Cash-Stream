package com.yuri.freire.Cash_Stream.Expense.entities.repositories;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Integer> {

    Optional<ExpenseCategory> findByCategoryName(String categoryName);

    @Query("""
       SELECT new com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryResponse(
       exc.expenseCategoryId,
       exc.categoryName)
       FROM ExpenseCategory exc
       """)
    Page<ExpenseCategoryResponse> findAllCategoryExpenses(Pageable pageable);
}
