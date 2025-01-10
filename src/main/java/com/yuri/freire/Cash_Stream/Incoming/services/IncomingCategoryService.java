package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Exceptions.AlreadyExistsException;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingCategoryRepository;
import com.yuri.freire.Cash_Stream.Incoming.services.factory.IncomingFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class IncomingCategoryService {
    private final IncomingCategoryRepository incomingCategoryRepository;
    private final IncomingFactory incomingFactory;

    public IncomingCategoryResponse createIncomingCategory(@Valid IncomingCategoryRequest incomingCategoryRequest){
        Optional<IncomingCategory> fetchedIncomingCategory = incomingCategoryRepository.findByCategoryName(incomingCategoryRequest.getCategoryName());
        if(fetchedIncomingCategory.isPresent()){
            throw new AlreadyExistsException("Incoming category with name '"
                    + incomingCategoryRequest.getCategoryName() + "' already exists");
        }
        IncomingCategory incomingCategory = incomingFactory.createIncomingCategory(incomingCategoryRequest);
        IncomingCategory incomingCategorySaved = incomingCategoryRepository.save(incomingCategory);
        return incomingFactory.createIncomingCategoryResponse(incomingCategorySaved);
    }

    public Page<IncomingCategoryResponse> findAll(Pageable pageable) {
        return incomingCategoryRepository.findAllIncomingCategory(pageable);
    }

    public IncomingCategory findByCategoryName(String categoryName){
        return incomingCategoryRepository
                .findByCategoryName(categoryName)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryName));
    }

    public String deleteByCategoryId(Integer categoryId){
        IncomingCategory incomingCategory = incomingCategoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
        incomingCategoryRepository.deleteById(categoryId);
        return incomingCategory.getCategoryName();
    }
}
