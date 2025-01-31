package com.yuri.freire.Cash_Stream.Expense.controllers;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.services.ExpenseSubcategoryService;
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
@RequestMapping("/expense-subcategory")
@RequiredArgsConstructor
public class ExpenseSubcategoryController {

    private final ExpenseSubcategoryService expenseSubcategoryService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ExpenseSubcategoryResponse>> createExpenseSubcategory(
            @Valid @RequestBody ExpenseSubcategoryRequest expenseSubcategoryRequest,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails){
        ExpenseSubcategoryResponse expenseSubcategory = expenseSubcategoryService.createExpenseSubcategory(expenseSubcategoryRequest, userDetails.getUsername());
        ApiResponse<ExpenseSubcategoryResponse> response = ResponseUtil.success(expenseSubcategory,"Subcategory created successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse<Page<ExpenseSubcategoryResponse>>> findAllSubcategoryExpenses(
            Pageable pageable,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails){
        Page<ExpenseSubcategoryResponse> expenseSubcategories = expenseSubcategoryService.findAllSubcategoryExpenses(pageable, userDetails.getUsername());
        ApiResponse<Page<ExpenseSubcategoryResponse>> response = ResponseUtil.success(expenseSubcategories, "Subcategories fetched successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all-by-category")
    public ResponseEntity<ApiResponse<Page<ExpenseSubcategoryResponse>>> findAllSubcategoryExpensesByCategory(
            @RequestParam String categoryName,
            Pageable pageable,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails){
        Page<ExpenseSubcategoryResponse> expenseSubcategories = expenseSubcategoryService.findAllSubcategoryExpensesByCategory(categoryName, pageable, userDetails.getUsername());
        ApiResponse<Page<ExpenseSubcategoryResponse>> response = ResponseUtil.success(expenseSubcategories, "Subcategories fetched by category successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{subcategoryId}")
    public ResponseEntity<ApiResponse<String>> deleteSubcategoryById(
            @PathVariable Integer subcategoryId,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails){
        String deletedSubcategory = expenseSubcategoryService.deleteBySubcategoryId(subcategoryId, userDetails.getUsername());
        ApiResponse<String> response = ResponseUtil.success(deletedSubcategory, "Subcategory deleted successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
