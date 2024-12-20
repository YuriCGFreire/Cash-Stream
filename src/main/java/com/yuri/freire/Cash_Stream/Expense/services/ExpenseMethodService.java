package com.yuri.freire.Cash_Stream.Expense.services;

import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseMethod;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseMethodRespository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseMethodService {

    private final ExpenseMethodRespository expenseMethodRespository;

    public ExpenseMethod findByExpenseMethodName(ExpenseMethodType expenseMethodType){
        return expenseMethodRespository.findByExpenseMethodName(expenseMethodType)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + expenseMethodType));
    }

}
