package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingRepository;
import com.yuri.freire.Cash_Stream.Incoming.services.facade.IncomingFacade;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncomingService {
    private final IncomingRepository incomingRepository;

    private final IncomingFacade incomingFacade;

    public IncomingResponse createIncoming(IncomingRequest incomingRequest, HttpServletRequest request){
        Incoming incoming = incomingFacade.createIncoming(incomingRequest, request);
        Incoming savedIncoming = incomingRepository.save(incoming);
        return incomingFacade.createIncomingResponse(savedIncoming);
    }


    public Page<IncomingResponse> findAllIncomings(Pageable pageable,HttpServletRequest request){
        String username = incomingFacade.extractUsernameFromCookie(request);
        return incomingRepository.findAllIncomings(username, pageable);
    }

    public Page<IncomingResponse> findAllIncomingsByCategory(String categoryName, Pageable pageable, HttpServletRequest request){
        String username = incomingFacade.extractUsernameFromCookie(request);
        String incomingCategoryName = incomingFacade.findIncomingCategoryByName(username, categoryName).getCategoryName();
        return incomingRepository.findAllByCategory(username, incomingCategoryName, pageable);
    }

    public Page<IncomingResponse> findAllIncomingsBySubcategory(String subcategoryName, Pageable pageable, HttpServletRequest request){
        String username = incomingFacade.extractUsernameFromCookie(request);
        String incomingSubcategoryName = incomingFacade.findIncomingSubcategoryByName(subcategoryName, username).getSubCategoryName();
        return incomingRepository.findAllBySubcategory(username, incomingSubcategoryName, pageable);
    }

    public String deleteByIncomingId(Integer incomingId, HttpServletRequest request){
        String username = incomingFacade.extractUsernameFromCookie(request);
        Incoming incoming = incomingRepository.findIncomingById(username, incomingId)
                .orElseThrow(() -> new EntityNotFoundException("Incoming not found: " + incomingId));
        incomingRepository.deleteById(incomingId);
        return incoming.getIncomingDescription();
    }
}
