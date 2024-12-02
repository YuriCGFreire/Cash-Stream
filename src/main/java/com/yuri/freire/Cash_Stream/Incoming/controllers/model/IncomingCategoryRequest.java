package com.yuri.freire.Cash_Stream.Incoming.controllers.model;

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
public class IncomingCategoryRequest {

    @NotNull(message = "Category name cannot be null")
    @Size(min = 3, max = 50)
    private String categoryName;

}
