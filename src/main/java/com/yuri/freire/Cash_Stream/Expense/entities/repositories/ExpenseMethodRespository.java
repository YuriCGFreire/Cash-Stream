package com.yuri.freire.Cash_Stream.Expense.entities.repositories;

import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseMethod;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseMethodRespository extends JpaRepository<ExpenseMethod, Integer> {
    ExpenseMethod findByExpenseMethodName(ExpenseMethodType expenseMethodType);
}
