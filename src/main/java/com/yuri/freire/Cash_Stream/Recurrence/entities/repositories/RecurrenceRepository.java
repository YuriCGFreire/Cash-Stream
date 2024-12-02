package com.yuri.freire.Cash_Stream.Recurrence.entities.repositories;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecurrenceRepository extends JpaRepository<Recurrence, Integer> {
    Optional<Recurrence> findByrecurrenceFrequency(RecurrenceType recurrenceType);
}
