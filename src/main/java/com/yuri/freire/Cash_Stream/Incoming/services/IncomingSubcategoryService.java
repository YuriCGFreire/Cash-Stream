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

    public IncomingSubcategoryResponse createIncomingSubcategory(IncomingSubcategoryRequest incomingSubcategoryRequest){
        String currentUsername = SecurityUtils.getCurrentUsername();
        User user = userService.findUserByUsername(currentUsername);
        Optional<IncomingSubcategory> fetchedSubcategory = incomingSubcategoryRepository.findBySubCategoryName(incomingSubcategoryRequest.getSubcategoryName(), currentUsername);
        if(fetchedSubcategory.isPresent()){
            throw new AlreadyExistsException("Incoming subcategory with name '"
                    + incomingSubcategoryRequest.getSubcategoryName() + "' already exists");
        }
        IncomingCategory incomingCategory = incomingCategoryService.findByCategoryName(incomingSubcategoryRequest.getIncomingCategoryName());
        IncomingSubcategory incomingSubcategory = incomingFactory.createIncomingSubcategory(incomingSubcategoryRequest, incomingCategory, user);
        IncomingSubcategory savedSubcategory = incomingSubcategoryRepository.save(incomingSubcategory);
        return incomingFactory.createIncomingSubcategoryResponse(savedSubcategory);
    }

    public IncomingSubcategory findBySubcategoryName(String subcategoryName){
        String currentUsername = SecurityUtils.getCurrentUsername();
        return incomingSubcategoryRepository.findBySubCategoryName(subcategoryName, currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + subcategoryName));
    }

    public Page<IncomingSubcategoryResponse> findAllSubcategory(Pageable pageable){
        String currentUsername = SecurityUtils.getCurrentUsername();
        return incomingSubcategoryRepository.findAllSubcategory(pageable, currentUsername);
    }

    public Page<IncomingSubcategoryResponse> findAllByCategoryName(String categoryName, Pageable pageable){
        String currentUsername = SecurityUtils.getCurrentUsername();
        IncomingCategory category = incomingCategoryService.findByCategoryName(categoryName);
        return incomingSubcategoryRepository.findAllByCategory(category.getCategoryName(), pageable, currentUsername);
    }

    public String deleteBySubcategoryId(Integer incomginSubcategoryId){
        String currentUsername = SecurityUtils.getCurrentUsername();
        IncomingSubcategory subcategory = incomingSubcategoryRepository.findBySubcategoryId(incomginSubcategoryId, currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + incomginSubcategoryId));
        incomingSubcategoryRepository.deleteById(subcategory.getIncomingSubcategoryId());
        return subcategory.getSubCategoryName();
    }
}
