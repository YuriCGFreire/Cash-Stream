package com.yuri.freire.Cash_Stream.Recurrence.services;

import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import com.yuri.freire.Cash_Stream.Recurrence.entities.repositories.RecurrenceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecurrenceService {
    private final RecurrenceRepository recurrenceRepository;

    public Recurrence findByRecurrenFrequency(RecurrenceType recurrence){
        return recurrenceRepository
                .findByrecurrenceFrequency(recurrence)
                .orElseThrow(() -> new EntityNotFoundException("Recorrencia não encontrada: " + recurrence));
    }
}
