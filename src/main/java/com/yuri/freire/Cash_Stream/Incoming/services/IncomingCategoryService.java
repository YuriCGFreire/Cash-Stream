package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingCategoryRepository;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.Response.ResponseUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class IncomingCategoryService {
    private final IncomingCategoryRepository incomingCategoryRepository;

    public IncomingCategoryResponse createIncomingCategory(IncomingCategoryRequest incomingCategoryRequest){
        IncomingCategory incomingCategory = IncomingCategory.builder()
                .categoryName(incomingCategoryRequest.getCategoryName())
                .build();
        incomingCategoryRepository.save(incomingCategory);
        return IncomingCategoryResponse.builder()
                .incomingCategoryId(incomingCategory.getIncomingCategoryId())
                .incomingCategoryName(incomingCategory.getCategoryName())
                .build();
    }

    public Page<IncomingCategoryResponse> findAll(Pageable pageable) {
        return incomingCategoryRepository.findAll(pageable)
                .map(incomingCategory -> IncomingCategoryResponse.builder()
                        .incomingCategoryId(incomingCategory.getIncomingCategoryId())
                        .incomingCategoryName(incomingCategory.getCategoryName())
                        .build());
    }

    public IncomingCategory findByCategoryName(String categoryName){
        return incomingCategoryRepository
                .findByCategoryName(categoryName)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryName));
    }

    public String deleteByCategoryId(Integer categoryId){
        IncomingCategory incomingCategory = incomingCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
        incomingCategoryRepository.deleteById(categoryId);
        return incomingCategory.getCategoryName();
    }
}
