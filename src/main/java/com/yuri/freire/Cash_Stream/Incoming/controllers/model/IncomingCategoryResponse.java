package com.yuri.freire.Cash_Stream.Incoming.controllers.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomingCategoryResponse {
    private Integer incomingCategoryId;
    private String categoryName;
}
