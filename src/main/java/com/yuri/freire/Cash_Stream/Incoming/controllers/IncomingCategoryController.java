package com.yuri.freire.Cash_Stream.Incoming.controllers;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingCategoryService;
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
@RequestMapping("/incoming-category")
@RequiredArgsConstructor
public class IncomingCategoryController {
    private final IncomingCategoryService incomingCategoryService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<IncomingCategoryResponse>> createIncomingCategory(@Valid @RequestBody IncomingCategoryRequest incomingCategoryRequest, HttpServletRequest request){
        IncomingCategoryResponse incomingCategory = incomingCategoryService.createIncomingCategory(incomingCategoryRequest);
        ApiResponse<IncomingCategoryResponse> response = ResponseUtil.success(incomingCategory, "Category created successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse<Page<IncomingCategoryResponse>>> findAllIncomingCategory(Pageable pageable, HttpServletRequest request){
        Page<IncomingCategoryResponse> incomingCategories = incomingCategoryService.findAll(pageable);
        ApiResponse<Page<IncomingCategoryResponse>> response = ResponseUtil.success(incomingCategories, "List of categories fetched successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<ApiResponse<String>> deleteByCategoryId(@RequestParam Integer categoryId, HttpServletRequest request){
        String deletedCategory = incomingCategoryService.deleteByCategoryId(categoryId);
        ApiResponse<String> response = ResponseUtil.success(deletedCategory, "Category deleted successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
