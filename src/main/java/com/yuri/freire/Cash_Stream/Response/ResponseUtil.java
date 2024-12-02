package com.yuri.freire.Cash_Stream.Response;

import java.util.Arrays;
import java.util.List;

public class ResponseUtil {

    public static <T> ApiResponse<T> success(T data, String message, String path){
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .errors(null)
                .errorCode(0)
                .timestamp(System.currentTimeMillis())
                .path(path)
                .build();
    }

    public static <T> ApiResponse<T> error(List<String> errors, String message, int errorCode, String path){
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .errors(errors)
                .errorCode(errorCode)
                .timestamp(System.currentTimeMillis())
                .path(path)
                .build();
    }

    public static <T> ApiResponse<T> error(String error, String message, int errorCode, String path) {
        return error(Arrays.asList(error), message, errorCode, path);
    }
}
