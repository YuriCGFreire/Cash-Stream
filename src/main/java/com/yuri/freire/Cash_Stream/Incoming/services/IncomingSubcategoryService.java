package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingSubcategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class IncomingSubcategoryService {
    private final IncomingSubcategoryRepository incomingSubcategoryRepository;

    private final IncomingCategoryService incomingCategoryService;

    public IncomingSubcategoryResponse createIncomingSubcategory(IncomingSubcategoryRequest incomingSubcategoryRequest){
        IncomingCategory incomingCategory = incomingCategoryService.findByCategoryName(incomingSubcategoryRequest.getIncomingCategoryName());

        IncomingSubcategory incomingSubcategory = IncomingSubcategory.builder()
                .subCategoryName(incomingSubcategoryRequest.getSubcategoryName())
                .incomingCategory(incomingCategory)
                .build();
         incomingSubcategoryRepository.save(incomingSubcategory);
         return IncomingSubcategoryResponse.builder()
                 .incomingSubcategoryId(incomingSubcategory.getIncomingSubcategoryId())
                 .incomingSubcategoryName(incomingSubcategory.getSubCategoryName())
                 .incomingCategoryName(incomingCategory.getCategoryName())
                 .build();
    }

    public IncomingSubcategory findBySubcategoryName(String subcategoryName){
        return incomingSubcategoryRepository.findBySubCategoryName(subcategoryName)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + subcategoryName));
    }

    public Page<IncomingSubcategoryResponse> findAllSubcategory(Pageable pageable){
        return incomingSubcategoryRepository.findAllSubcategory(pageable)
                .map(incomginSubcategory -> IncomingSubcategoryResponse.builder()
                        .incomingSubcategoryId(incomginSubcategory.getIncomingSubcategoryId())
                        .incomingSubcategoryName(incomginSubcategory.getSubCategoryName())
                        .incomingCategoryName(incomginSubcategory.getIncomingCategory().getCategoryName())
                        .build());
    }

    public Page<IncomingSubcategoryResponse> findAllByCategoryName(String categoryName, Pageable pageable){
        return incomingSubcategoryRepository.findAllByCategory(categoryName, pageable)
                .map(incomginSubcategory -> IncomingSubcategoryResponse.builder()
                        .incomingSubcategoryId(incomginSubcategory.getIncomingSubcategoryId())
                        .incomingSubcategoryName(incomginSubcategory.getSubCategoryName())
                        .incomingCategoryName(incomginSubcategory.getIncomingCategory().getCategoryName())
                        .build());
    }

    public String deleteBySubcategoryId(Integer incomginSubcategoryId){
        IncomingSubcategory subcategory = incomingSubcategoryRepository.findById(incomginSubcategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + incomginSubcategoryId));
        incomingSubcategoryRepository.deleteById(incomginSubcategoryId);
        return subcategory.getSubCategoryName();
    }
}
