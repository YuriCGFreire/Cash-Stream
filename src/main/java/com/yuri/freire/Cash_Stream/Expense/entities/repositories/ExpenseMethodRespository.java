package com.yuri.freire.Cash_Stream.Expense.entities.repositories;

import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseMethod;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseMethodRespository extends JpaRepository<ExpenseMethod, Integer> {
    Optional<ExpenseMethod> findByExpenseMethodName(ExpenseMethodType expenseMethodType);
}
