package com.yuri.freire.Cash_Stream.Incoming.controllers;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingService;
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
@RequestMapping("/incoming")
@RequiredArgsConstructor
public class IncomingController {
    private final IncomingService incomingService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<IncomingResponse>> createIncoming(@Valid @RequestBody IncomingRequest incomingRequest, HttpServletRequest request){
        IncomingResponse incomingCreated = incomingService.createIncoming(incomingRequest);
        ApiResponse<IncomingResponse> response = ResponseUtil.success(incomingCreated, "Incoming created successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse<Page<IncomingResponse>>> findAllIncomings(HttpServletRequest request, Pageable pageable){
        Page<IncomingResponse> fetchedIncomings = incomingService.findAllIncomings(pageable);
        ApiResponse<Page<IncomingResponse>> response = ResponseUtil.success(fetchedIncomings, "List of incomings fetched successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all-by-category")
    public ResponseEntity<ApiResponse<Page<IncomingResponse>>> findAllIncomingsByCategory(@RequestParam String categoryName, HttpServletRequest request, Pageable pageable){
        Page<IncomingResponse> fetchedIncomings = incomingService.findAllIncomingsByCategory(categoryName, pageable);
        ApiResponse<Page<IncomingResponse>> response = ResponseUtil.success(fetchedIncomings, "List of incomings by category fetched successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all-by-subcategory")
    public ResponseEntity<ApiResponse<Page<IncomingResponse>>> findAllIncomingsBySubcategory(@RequestParam String subcategoryName, HttpServletRequest request, Pageable pageable){
        Page<IncomingResponse> fetchedIncomings = incomingService.findAllIncomingsBySubcategory(subcategoryName, pageable);
        ApiResponse<Page<IncomingResponse>> response = ResponseUtil.success(fetchedIncomings, "List of incomings by subcategory fetched successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{incomingId}")
    public ResponseEntity<ApiResponse<String>> deleteByIncomingId(@PathVariable Integer incomingId, HttpServletRequest request){
        String deletedCategory = incomingService.deleteByIncomingId(incomingId);
        ApiResponse<String> response = ResponseUtil.success(deletedCategory, "Incoming deleted successfully", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
