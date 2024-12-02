package com.yuri.freire.Cash_Stream.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T>{
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private int errorCode;
    private long timestamp;
    private String path;
}
