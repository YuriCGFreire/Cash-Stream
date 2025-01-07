package com.yuri.freire.Cash_Stream.Expense.controllers.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseSubcategoryRequest {

    @NotNull(message = "Category name cannot be null")
    @Size(min = 3, max = 50, message = "Subcategory name length must be between 3 and 50 characters")
    private String subcategoryName;

    @NotNull(message = "Category name cannot be null")
    @Size(min = 3, max = 50, message = "Category name length must be between 3 and 50 characters")
    private String categoryName;

}
