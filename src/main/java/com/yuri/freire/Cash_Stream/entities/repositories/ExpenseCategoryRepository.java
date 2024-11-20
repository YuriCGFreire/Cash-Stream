package com.yuri.freire.Cash_Stream.entities.repositories;

import com.yuri.freire.Cash_Stream.entities.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Integer> {
    ExpenseCategory findByCategoryName(String expenseCategoryName);
}
