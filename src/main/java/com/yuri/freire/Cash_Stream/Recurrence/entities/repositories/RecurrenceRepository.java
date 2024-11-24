package com.yuri.freire.Cash_Stream.Recurrence.entities.repositories;

import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurrenceRepository extends JpaRepository<Recurrence, Integer> {
    Recurrence findByrecurrenceFrequency(RecurrenceType recurrenceType);
}
