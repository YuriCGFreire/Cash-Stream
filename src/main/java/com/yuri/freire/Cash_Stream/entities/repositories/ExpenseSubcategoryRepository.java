package com.yuri.freire.Cash_Stream.entities.repositories;

import com.yuri.freire.Cash_Stream.entities.ExpenseSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseSubcategoryRepository extends JpaRepository<ExpenseSubcategory, Integer> {
    ExpenseSubcategory findExpenseSubcategoryByName(String expenseSubcategoryName);
}
