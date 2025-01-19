package com.yuri.freire.Cash_Stream.Expense.controllers.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCategoryResponse {
    private Integer expenseCategoryId;
    private String categoryName;
    private String username;
}
