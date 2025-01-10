package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingRepository;
import com.yuri.freire.Cash_Stream.Incoming.services.facade.IncomingFacade;
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
        return incomingRepository.findAllIncomings(pageable);
    }

    public Page<IncomingResponse> findAllIncomingsByCategory(String categoryName, Pageable pageable){
        return incomingRepository.findAllByCategory(categoryName, pageable);
    }

    public Page<IncomingResponse> findAllIncomingsBySubcategory(String subcategoryName, Pageable pageable){
        return incomingRepository.findAllBySubcategory(subcategoryName, pageable);
    }

    public String deleteByIncomingId(Integer incomingId){
        Incoming incoming = incomingRepository.findIncomingById(incomingId)
                .orElseThrow(() -> new EntityNotFoundException("Incoming not found: " + incomingId));
        incomingRepository.deleteById(incomingId);
        return incoming.getIncomingDescription();
    }
}
