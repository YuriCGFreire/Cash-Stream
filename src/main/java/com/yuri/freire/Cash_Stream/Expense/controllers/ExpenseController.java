package com.yuri.freire.Cash_Stream.Expense.controllers;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Expense.services.ExpenseService;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.Response.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ExpenseResponse>> createExpense(@Valid @RequestBody ExpenseRequest expenseRequest, HttpServletRequest request){
        ExpenseResponse expense = expenseService.createExpense(expenseRequest);
        ApiResponse<ExpenseResponse> response = ResponseUtil.success(expense,"Expense created successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> findAllExpenses(Pageable pageable, HttpServletRequest request){
        Page<ExpenseResponse> allExpenses = expenseService.findAllExpenses(pageable);
        ApiResponse<Page<ExpenseResponse>> response = ResponseUtil.success(allExpenses, "Expenses fetched successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/find-by-category")
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> findAllExpensesByCategoryName(@RequestParam String categoryName, Pageable pageable, HttpServletRequest request){
        Page<ExpenseResponse> allExpensesByCategory = expenseService.findAllExpensesByCategoryName(categoryName, pageable);
        ApiResponse<Page<ExpenseResponse>> response = ResponseUtil.success(allExpensesByCategory, "Expenses fetched by category successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/find-by-subcategory")
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> findAllExpensesBySubcategoryName(@RequestParam String subcategoryName, Pageable pageable, HttpServletRequest request){
        Page<ExpenseResponse> allExpensesByCategory = expenseService.findAllBySubcategoryName(subcategoryName, pageable);
        ApiResponse<Page<ExpenseResponse>> response = ResponseUtil.success(allExpensesByCategory,  "Expenses fetched by subcategory successfuly", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/find-by-method")
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> findAllExpensesByPaymentMethod(@RequestParam ExpenseMethodType expenseMethodType, Pageable pageable, HttpServletRequest request){
        Page<ExpenseResponse> allExpensesByCategory = expenseService.findAllExpensesByPaymentMethod(expenseMethodType, pageable);
        ApiResponse<Page<ExpenseResponse>> response = ResponseUtil.success(allExpensesByCategory, "Expenses fetched by payment method successfuly", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/isEssential")
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> findAllByIsEssential(@RequestParam boolean isEssential, Pageable pageable, HttpServletRequest request){
        Page<ExpenseResponse> findByIsEssential = expenseService.findAllExpensesByIsEssential(isEssential,pageable);
        ApiResponse<Page<ExpenseResponse>> response = ResponseUtil.success(findByIsEssential, "Expenses fetched by essentiality successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{expenseId}/soft")
    public ResponseEntity<ApiResponse<String>> softDeleteExpense(@PathVariable Integer expenseId, HttpServletRequest request){
        String softDeleteExpense = expenseService.softDeleteExpense(expenseId);
        ApiResponse<String> response = ResponseUtil.success(softDeleteExpense, "Expense deleted successfuly", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
