package com.yuri.freire.Cash_Stream.Incoming.controllers.model;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomingRequest {

    @NotEmpty(message = "Incoming description cannot be null")
    @Size(min = 3, max = 50)
    private String incomingDescription;

    @NotEmpty(message = "Gross incomingo cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Gross incoming must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Gross incoming must be a valid monetary value with up to 10 digits and 2 decimal places")
    private BigDecimal grossIncoming;

    @NotEmpty(message = "Net incoming cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Net incoming must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Net incoming must be a valid monetary value with up to 10 digits and 2 decimal places")
    private BigDecimal netIncoming;

    @NotNull(message = "Recurrence cannot be null")
    private RecurrenceType recurrence;

    @NotNull(message = "Incoming category cannot be null")
    private String incomingCategory;

    @NotNull(message = "Incoming subcategory cannot be null")
    private String incomingSubcategory;
}
