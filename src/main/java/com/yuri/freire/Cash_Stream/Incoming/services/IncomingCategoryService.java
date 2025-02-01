package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.services.UserService;
import com.yuri.freire.Cash_Stream.Exceptions.AlreadyExistsException;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingCategoryRepository;
import com.yuri.freire.Cash_Stream.Incoming.services.factory.IncomingFactory;
import com.yuri.freire.Cash_Stream.Utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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
    private final UserService userService;

    public IncomingCategoryResponse createIncomingCategory(@Valid IncomingCategoryRequest incomingCategoryRequest, String username){
        User user = userService.findUserByUsername(username);
        Optional<IncomingCategory> fetchedIncomingCategory = incomingCategoryRepository.findByCategoryName(incomingCategoryRequest.getCategoryName(), username);
        if(fetchedIncomingCategory.isPresent()){
            throw new AlreadyExistsException("Incoming category with name '"
                    + incomingCategoryRequest.getCategoryName() + "' already exists");
        }
        IncomingCategory incomingCategory = incomingFactory.createIncomingCategory(incomingCategoryRequest, user);
        IncomingCategory incomingCategorySaved = incomingCategoryRepository.save(incomingCategory);
        return incomingFactory.createIncomingCategoryResponse(incomingCategorySaved);
    }

    public Page<IncomingCategoryResponse> findAll(Pageable pageable, String useername) {
        return incomingCategoryRepository.findAllIncomingCategory(pageable, useername);
    }

    public IncomingCategory findByCategoryName(String categoryName, String username){
        return incomingCategoryRepository
                .findByCategoryName(categoryName, username)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryName));
    }

    public String deleteByCategoryId(Integer categoryId, String username){
        IncomingCategory incomingCategory = incomingCategoryRepository.findByCategoryId(categoryId, username)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
        incomingCategoryRepository.deleteById(categoryId);
        return incomingCategory.getCategoryName();
    }
}
