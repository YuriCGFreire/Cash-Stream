package com.yuri.freire.Cash_Stream.Incoming.controllers;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingSubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/incoming-subcategory")
@RequiredArgsConstructor
public class IncomingSubcatergoyController {
    private final IncomingSubcategoryService incomingSubcategoryService;

    @PostMapping("/create")
    public ResponseEntity<IncomingSubcategoryResponse> createIncomingSubcategory(@RequestBody IncomingSubcategoryRequest incomingSubcategoryRequest){
        return ResponseEntity.ok(incomingSubcategoryService.createIncomingSubcategory(incomingSubcategoryRequest));
    }
}
