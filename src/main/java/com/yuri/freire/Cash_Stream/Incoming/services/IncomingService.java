package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingRepository;
import com.yuri.freire.Cash_Stream.Incoming.services.facade.IncomingFacade;
import com.yuri.freire.Cash_Stream.Utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncomingService {
    private final IncomingRepository incomingRepository;

    private final IncomingFacade incomingFacade;

    public IncomingResponse createIncoming(IncomingRequest incomingRequest){
        Incoming incoming = incomingFacade.createIncoming(incomingRequest);
        Incoming savedIncoming = incomingRepository.save(incoming);
        return incomingFacade.createIncomingResponse(savedIncoming);
    }


    public Page<IncomingResponse> findAllIncomings(Pageable pageable){
        String currentUsername = SecurityUtils.getCurrentUsername();
        return incomingRepository.findAllIncomings(pageable, currentUsername);
    }

    public Page<IncomingResponse> findAllIncomingsByCategory(String categoryName, Pageable pageable){
        String currentUsername = SecurityUtils.getCurrentUsername();
        return incomingRepository.findAllByCategory(categoryName, pageable, currentUsername);
    }

    public Page<IncomingResponse> findAllIncomingsBySubcategory(String subcategoryName, Pageable pageable){
        String currentUsername = SecurityUtils.getCurrentUsername();
        return incomingRepository.findAllBySubcategory(subcategoryName, pageable, currentUsername);
    }

    public String deleteByIncomingId(Integer incomingId){
        String currentUsername = SecurityUtils.getCurrentUsername();
        Incoming incoming = incomingRepository.findIncomingById(incomingId, currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("Incoming not found: " + incomingId));
        incomingRepository.deleteById(incomingId);
        return incoming.getIncomingDescription();
    }
}
