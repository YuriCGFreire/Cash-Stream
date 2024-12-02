package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingCategoryRepository;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingRepository;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingSubcategoryRepository;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import com.yuri.freire.Cash_Stream.Recurrence.entities.repositories.RecurrenceRepository;
import com.yuri.freire.Cash_Stream.Recurrence.services.RecurrenceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomingService {

    private final IncomingSubcategoryService incomingSubcategoryService;

    private final IncomingCategoryService incomingCategoryService;

    private final RecurrenceService recurrenceService;

    private final IncomingRepository incomingRepository;

    public IncomingResponse createIncoming(IncomingRequest incomingRequest){
        IncomingCategory incomingCategory = incomingCategoryService.findByCategoryName(incomingRequest.getIncomingCategory());

        IncomingSubcategory incomingSubcategory = incomingSubcategoryService.findBySubcategoryName(incomingRequest.getIncomingSubcategory());

        Recurrence recurrence = recurrenceService.findByRecurrenFrequency(incomingRequest.getRecurrence());


        Incoming incoming = Incoming.builder()
                .incomingDescription(incomingRequest.getIncomingDescription())
                .grossIncoming(incomingRequest.getGrossIncoming())
                .netIncoming(incomingRequest.getNetIncoming())
                .incomingCategory(incomingCategory)
                .incomingSubcategory(incomingSubcategory)
                .recurrence(recurrence)
                .build();

        incomingRepository.save(incoming);

        return IncomingResponse.builder()
                .incomingId(incoming.getIncomingId())
                .incomingDescription(incoming.getIncomingDescription())
                .grossIncoming(incoming.getGrossIncoming())
                .netIncoming(incoming.getNetIncoming())
                .recurrence(incoming.getRecurrence().getRecurrenceFrequency().name())
                .incomingSubcategory(incoming.getIncomingCategory().getCategoryName())
                .incomingCategory(incoming.getIncomingCategory().getCategoryName())
                .build();
    }


    public Page<IncomingResponse> findAllIncomings(Pageable pageable){
        return incomingRepository.findAllIncomings(pageable)
                .map(incoming -> IncomingResponse.builder()
                        .incomingId(incoming.getIncomingId())
                        .incomingDescription(incoming.getIncomingDescription())
                        .grossIncoming(incoming.getGrossIncoming())
                        .netIncoming(incoming.getNetIncoming())
                        .incomingCategory(incoming.getIncomingCategory().getCategoryName())
                        .incomingSubcategory(incoming.getIncomingSubcategory().getSubCategoryName())
                        .recurrence(incoming.getRecurrence().getRecurrenceFrequency().name())
                        .build());
    }

    public Page<IncomingResponse> findAllIncomingsByCategory(String categoryName, Pageable pageable){
        return incomingRepository.findAllByCategory(categoryName, pageable)
                .map(incoming -> IncomingResponse.builder()
                        .incomingId(incoming.getIncomingId())
                        .incomingDescription(incoming.getIncomingDescription())
                        .grossIncoming(incoming.getGrossIncoming())
                        .netIncoming(incoming.getNetIncoming())
                        .incomingCategory(incoming.getIncomingCategory().getCategoryName())
                        .incomingSubcategory(incoming.getIncomingSubcategory().getSubCategoryName())
                        .recurrence(incoming.getRecurrence().getRecurrenceFrequency().name())
                        .build());
    }

    public Page<IncomingResponse> findAllIncomingsBySubcategory(String subcategoryName, Pageable pageable){
        return incomingRepository.findAllBySubcategory(subcategoryName, pageable)
                .map(incoming -> IncomingResponse.builder()
                        .incomingId(incoming.getIncomingId())
                        .incomingDescription(incoming.getIncomingDescription())
                        .grossIncoming(incoming.getGrossIncoming())
                        .netIncoming(incoming.getNetIncoming())
                        .incomingCategory(incoming.getIncomingCategory().getCategoryName())
                        .incomingSubcategory(incoming.getIncomingSubcategory().getSubCategoryName())
                        .recurrence(incoming.getRecurrence().getRecurrenceFrequency().name())
                        .build());
    }

    public String deleteByIncomingId(Integer incomingId){
        Incoming incoming = incomingRepository.findById(incomingId)
                .orElseThrow(() -> new EntityNotFoundException("Incoming not found: " + incomingId));
        incomingRepository.deleteById(incomingId);
        return incoming.getIncomingDescription();
    }
}
