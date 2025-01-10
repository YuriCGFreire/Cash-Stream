package com.yuri.freire.Cash_Stream.Expense.controllers.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRequest {

    @NotEmpty(message = "Expense description cannot be null")
    @Size(min = 3, max = 50, message = "Expense description length must be between 3 and 50 characters")
    private String expenseDescription;

    @NotNull(message = "Expense amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Expense amount must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Expense amount must be a valid monetary value")
    private BigDecimal expenseAmount;

    @NotNull(message = "Expense date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expenseDate;

    @NotNull(message = "Expense essentiality cannot be null")
    @JsonProperty("isEssential")
    private boolean isEssential;

    @NotNull(message = "Expense method cannot be null")
    private ExpenseMethodType expenseMethod;

    @NotNull(message = "Recurrence cannot be null")
    private RecurrenceType recurrence;

    @NotNull(message = "Expense category cannot be null")
    @Size(min = 3, max = 50, message = "Expense category name length must be between 3 and 50 characters")
    private String expenseCategory;

    @NotNull(message = "Expense subcategory cannot be null")
    @Size(min = 3, max = 50, message = "Expense subcategory name length must be between 3 and 50 characters")
    private String expenseSubcategory;
}
