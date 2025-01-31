package com.yuri.freire.Cash_Stream.Expense.controllers;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.services.ExpenseCategoryService;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.Response.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expense-category")
@RequiredArgsConstructor
public class ExpenseCategoryController {
    private final ExpenseCategoryService expenseCategoryService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ExpenseCategoryResponse>> createExpenseCategory(
            @Valid @RequestBody ExpenseCategoryRequest expenseCategoryRequest,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails){
        ExpenseCategoryResponse expenseCategoryResponse = expenseCategoryService.createExpenseCategory(expenseCategoryRequest, userDetails.getUsername());
        ApiResponse<ExpenseCategoryResponse> response = ResponseUtil.success(expenseCategoryResponse,"Category created successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse<Page<ExpenseCategoryResponse>>> findAllExpenses(
            Pageable pageable,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails){
        Page<ExpenseCategoryResponse> expenseCategoriesResponse = expenseCategoryService.findAllCategoryExpenses(pageable, userDetails.getUsername());
        ApiResponse<Page<ExpenseCategoryResponse>> response = ResponseUtil.success(expenseCategoriesResponse, "List of categories fetched successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse<String>> deleteByCategoryId(
            @PathVariable Integer categoryId,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails){
        String deletedExpenseCaategory = expenseCategoryService.deleteByCategoryId(categoryId, userDetails.getUsername());
        ApiResponse<String> response = ResponseUtil.success(deletedExpenseCaategory, "Category deleted successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
