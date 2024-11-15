package com.yuri.freire.Cash_Stream.entities.repositories;

import com.yuri.freire.Cash_Stream.entities.ExpenseMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseMethodRespository extends JpaRepository<ExpenseMethod, Integer> {
    ExpenseMethod findByExpenseMethodName(String expenseMethodName);
}
