package com.yuri.freire.Cash_Stream.entities.repositories;

import com.yuri.freire.Cash_Stream.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    List<Expense> findAll();
}
