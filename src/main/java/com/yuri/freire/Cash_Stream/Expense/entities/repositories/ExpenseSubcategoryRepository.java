package com.yuri.freire.Cash_Stream.Expense.entities.repositories;

import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseSubcategoryRepository extends JpaRepository<ExpenseSubcategory, Integer> {
    Optional<ExpenseSubcategory> findBySubCategoryName(String expenseSubcategoryName);
}
