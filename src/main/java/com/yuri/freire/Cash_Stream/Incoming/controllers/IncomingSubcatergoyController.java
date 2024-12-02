package com.yuri.freire.Cash_Stream.Incoming.controllers;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingSubcategoryService;
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
@RequestMapping("/incoming-subcategory")
@RequiredArgsConstructor
public class IncomingSubcatergoyController {
    private final IncomingSubcategoryService incomingSubcategoryService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<IncomingSubcategoryResponse>> createIncomingSubcategory(@Valid @RequestBody IncomingSubcategoryRequest incomingSubcategoryRequest, HttpServletRequest request){
        IncomingSubcategoryResponse createdSubcategory = incomingSubcategoryService.createIncomingSubcategory(incomingSubcategoryRequest);
        ApiResponse<IncomingSubcategoryResponse> response = ResponseUtil.success(createdSubcategory, "Subcategory created successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse<Page<IncomingSubcategoryResponse>>>findAllIncomingSubcategory(Pageable pageable, HttpServletRequest request){
        Page<IncomingSubcategoryResponse> incomingSubcategories = incomingSubcategoryService.findAllSubcategory(pageable);
        ApiResponse<Page<IncomingSubcategoryResponse>> response = ResponseUtil.success(incomingSubcategories, "List of subcategories fetched successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all-by-category")
    public ResponseEntity<ApiResponse<Page<IncomingSubcategoryResponse>>> findAllByCategoryName(@RequestParam String categoryName, Pageable pageable, HttpServletRequest request){
        Page<IncomingSubcategoryResponse> incomingSubcategories = incomingSubcategoryService.findAllByCategoryName(categoryName, pageable);
        ApiResponse<Page<IncomingSubcategoryResponse>> response = ResponseUtil.success(incomingSubcategories, "list of subcategories by category name fetched successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<ApiResponse<String>> deleteByIncomingSubcategoryId(@RequestParam Integer incomingSubcategoryId, HttpServletRequest request){
        String deletedSubcategory = incomingSubcategoryService.deleteBySubcategoryId(incomingSubcategoryId);
        ApiResponse<String> response = ResponseUtil.success(deletedSubcategory,"Subcategory deleted successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
