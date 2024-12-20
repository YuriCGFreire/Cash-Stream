package com.yuri.freire.Cash_Stream.Expense.controllers.model;

import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {
    private Integer expenseId;
    private String expenseDescription;
    private BigDecimal expenseAmount;
    private boolean isEssential;
    private ExpenseMethodType expenseMethod;
    private RecurrenceType recurrence;
    private String categoryName;
    private String subCategoryName;
}
