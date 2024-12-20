package com.yuri.freire.Cash_Stream.Incoming.controllers.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomingSubcategoryResponse {
    private Integer incomingSubcategoryId;
    private String subCategoryName;
    private String categoryName;
}
