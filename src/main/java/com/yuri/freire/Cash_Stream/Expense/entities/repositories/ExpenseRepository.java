package com.yuri.freire.Cash_Stream.Expense.entities.repositories;

import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    Page<Expense>  findAllExpenses(Pageable pageable);
    Page<Expense> findAllByCategory(@Param("categoryName") String categoryName, Pageable pageable);
    Page<Expense> findAllBySubcategory(@Param("subcategoryName") String subcategoryName, Pageable pageable);
    Page<Expense> findAllBYPaymentMethod(@Param("expenseMethod") String expenseMethod, Pageable pageable);
    Page<Expense> findAllByEssentiality(@Param("isEssential") boolean isEssential, Pageable pageable);
}
