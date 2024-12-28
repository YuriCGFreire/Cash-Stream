package com.yuri.freire.Cash_Stream.util.expense;

import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseMethod;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;

public class ExpenseMethodCreator {
    public static ExpenseMethod createValidExpenseMethod(){
        return ExpenseMethod.builder()
                .expenseMethodId(1)
                .expenseMethodName(ExpenseMethodType.CREDIT)
                .build();
    }
}
