package com.yuri.freire.Cash_Stream.Incoming.controllers;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/incoming-category")
@RequiredArgsConstructor
public class IncomingCategoryController {
    private final IncomingCategoryService incomingCategoryService;

    @PostMapping("/create")
    public ResponseEntity<IncomingCategoryResponse> createIncomingCategory(@RequestBody IncomingCategoryRequest incomingCategoryRequest){
        return ResponseEntity.ok(incomingCategoryService.createIncomingCategory(incomingCategoryRequest));
    }

    @GetMapping("/find-all")
    public ResponseEntity<ArrayList<IncomingCategoryResponse>> find(){
        return ResponseEntity.ok(incomingCategoryService.findAll());
    }
}
