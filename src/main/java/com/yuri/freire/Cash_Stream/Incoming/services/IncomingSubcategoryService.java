package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.services.JwtService;
import com.yuri.freire.Cash_Stream.Authentication.services.UserService;
import com.yuri.freire.Cash_Stream.Exceptions.AlreadyExistsException;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingSubcategoryRepository;
import com.yuri.freire.Cash_Stream.Incoming.services.factory.IncomingFactory;
import com.yuri.freire.Cash_Stream.Utils.CookieUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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

    private final JwtService jwtService;
    private final UserService userService;


    public IncomingSubcategoryResponse createIncomingSubcategory(IncomingSubcategoryRequest incomingSubcategoryRequest, HttpServletRequest request){
        String username = this.extractUsernameFromCookie(request);
        User user = userService.findUserByUsername(username);
        Optional<IncomingSubcategory> fetchedSubcategory = incomingSubcategoryRepository.findBySubCategoryName(username, incomingSubcategoryRequest.getSubcategoryName());
        if(fetchedSubcategory.isPresent()){
            throw new AlreadyExistsException("Incoming subcategory with name '"
                    + incomingSubcategoryRequest.getSubcategoryName() + "' already exists");
        }
        IncomingCategory incomingCategory = incomingCategoryService.findByCategoryName(username, incomingSubcategoryRequest.getIncomingCategoryName());
        IncomingSubcategory incomingSubcategory = incomingFactory.createIncomingSubcategory(incomingSubcategoryRequest, incomingCategory, user);
        IncomingSubcategory savedSubcategory = incomingSubcategoryRepository.save(incomingSubcategory);
        return incomingFactory.createIncomingSubcategoryResponse(savedSubcategory);
    }

    public IncomingSubcategory findBySubcategoryName(String subcategoryName, String username){
        return incomingSubcategoryRepository.findBySubCategoryName(username, subcategoryName)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + subcategoryName));
    }

    public Page<IncomingSubcategoryResponse> findAllSubcategory(Pageable pageable, HttpServletRequest request){
        String username = this.extractUsernameFromCookie(request);
        return incomingSubcategoryRepository.findAllSubcategory(username, pageable);
    }

    public Page<IncomingSubcategoryResponse> findAllByCategoryName(String categoryName, Pageable pageable, HttpServletRequest request){
        String username = this.extractUsernameFromCookie(request);
        IncomingCategory category = incomingCategoryService.findByCategoryName(username, categoryName);
        return incomingSubcategoryRepository.findAllByCategory(username, category.getCategoryName(), pageable);
    }

    public String deleteBySubcategoryId(Integer incomginSubcategoryId, HttpServletRequest request){
        String username = this.extractUsernameFromCookie(request);
        IncomingSubcategory subcategory = incomingSubcategoryRepository.findBySubcategoryId(username, incomginSubcategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found: " + incomginSubcategoryId));
        incomingSubcategoryRepository.deleteById(subcategory.getIncomingSubcategoryId());
        return subcategory.getSubCategoryName();
    }

    public String extractUsernameFromCookie(HttpServletRequest request){
        String jwtFromCookie = CookieUtils.getJwtFromCookie(request);
        String username = jwtService.extractUsername(jwtFromCookie);
        return username;
    }
}
