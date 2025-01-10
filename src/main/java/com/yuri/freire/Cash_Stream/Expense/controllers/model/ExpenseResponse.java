package com.yuri.freire.Cash_Stream.Expense.controllers.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {
    private Integer expenseId;
    private String expenseDescription;
    private BigDecimal expenseAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expenseDate;
    private boolean isEssential;
    private ExpenseMethodType expenseMethod;
    private RecurrenceType recurrence;
    private String categoryName;
    private String subCategoryName;
}
