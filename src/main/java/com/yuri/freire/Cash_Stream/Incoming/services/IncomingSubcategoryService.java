package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.services.UserService;
import com.yuri.freire.Cash_Stream.Exceptions.AlreadyExistsException;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingSubcategoryRepository;
import com.yuri.freire.Cash_Stream.Incoming.services.factory.IncomingFactory;
import com.yuri.freire.Cash_Stream.Utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class IncomingSubcategoryService {
    private final IncomingSubcategoryRepository incomingSubcategoryRepository;
    private final IncomingCategoryService incomingCategoryService;
    private final IncomingFactory incomingFactory;
    private final UserService userService;

    public IncomingSubcategoryResponse createIncomingSubcategory(IncomingSubcategoryRequest incomingSubcategoryRequest, String username){
        User user = userService.findUserByUsername(username);
        Optional<IncomingSubcategory> fetchedSubcategory = incomingSubcategoryRepository.findBySubCategoryName(incomingSubcategoryRequest.getSubcategoryName(), username);
        if(fetchedSubcategory.isPresent()){
            throw new AlreadyExistsException("Incoming subcategory with name '"
                    + incomingSubcategoryRequest.getSubcategoryName() + "' already exists");
        }
        IncomingCategory incomingCategory = incomingCategoryService.findByCategoryName(incomingSubcategoryRequest.getIncomingCategoryName(), username);
        IncomingSubcategory incomingSubcategory = incomingFactory.createIncomingSubcategory(incomingSubcategoryRequest, incomingCategory, user);
        IncomingSubcategory savedSubcategory = incomingSubcategoryRepository.save(incomingSubcategory);
        return incomingFactory.createIncomingSubcategoryResponse(savedSubcategory);
    }

    public IncomingSubcategory findBySubcategoryName(String subcategoryName, String username){
        return incomingSubcategoryRepository.findBySubCategoryName(subcategoryName, username)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + subcategoryName));
    }

    public Page<IncomingSubcategoryResponse> findAllSubcategory(Pageable pageable, String username){
        return incomingSubcategoryRepository.findAllSubcategory(pageable, username);
    }

    public Page<IncomingSubcategoryResponse> findAllByCategoryName(String categoryName, Pageable pageable, String username){
        IncomingCategory category = incomingCategoryService.findByCategoryName(categoryName, username);
        return incomingSubcategoryRepository.findAllByCategory(category.getCategoryName(), pageable, username);
    }

    public String deleteBySubcategoryId(Integer incomginSubcategoryId, String username){
        IncomingSubcategory subcategory = incomingSubcategoryRepository.findBySubcategoryId(incomginSubcategoryId, username)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + incomginSubcategoryId));
        incomingSubcategoryRepository.deleteById(subcategory.getIncomingSubcategoryId());
        return subcategory.getSubCategoryName();
    }
}
