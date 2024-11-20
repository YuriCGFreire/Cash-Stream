package com.yuri.freire.Cash_Stream.entities.repositories;

import com.yuri.freire.Cash_Stream.entities.Recurrence;
import com.yuri.freire.Cash_Stream.entities.entity_enum.RecurrenceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurrenceRepository extends JpaRepository<Recurrence, Integer> {
    Recurrence findByrecurrenceFrequency(RecurrenceType recurrenceType);
}
