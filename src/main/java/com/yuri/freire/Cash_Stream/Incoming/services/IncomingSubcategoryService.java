package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingSubcategoryRepository;
import com.yuri.freire.Cash_Stream.Incoming.services.factory.IncomingFactory;
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
    private final IncomingFactory incomingFactory;


    public IncomingSubcategoryResponse createIncomingSubcategory(IncomingSubcategoryRequest incomingSubcategoryRequest){
        IncomingCategory incomingCategory = incomingCategoryService.findByCategoryName(incomingSubcategoryRequest.getIncomingCategoryName());
        IncomingSubcategory incomingSubcategory = incomingFactory.createIncomingSubcategory(incomingSubcategoryRequest, incomingCategory);
        IncomingSubcategory savedSubcategory = incomingSubcategoryRepository.save(incomingSubcategory);
        return incomingFactory.createIncomingSubcategoryResponse(savedSubcategory);
    }

    public IncomingSubcategory findBySubcategoryName(String subcategoryName){
        return incomingSubcategoryRepository.findBySubCategoryName(subcategoryName)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + subcategoryName));
    }

    public Page<IncomingSubcategoryResponse> findAllSubcategory(Pageable pageable){
        return incomingSubcategoryRepository.findAllSubcategory(pageable);
    }

    public Page<IncomingSubcategoryResponse> findAllByCategoryName(String categoryName, Pageable pageable){
        IncomingCategory category = incomingCategoryService.findByCategoryName(categoryName);
        return incomingSubcategoryRepository.findAllByCategory(category.getCategoryName(), pageable);
    }

    public String deleteBySubcategoryId(Integer incomginSubcategoryId){
        IncomingSubcategory subcategory = incomingSubcategoryRepository.findById(incomginSubcategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + incomginSubcategoryId));
        incomingSubcategoryRepository.deleteById(subcategory.getIncomingSubcategoryId());
        return subcategory.getSubCategoryName();
    }


}
