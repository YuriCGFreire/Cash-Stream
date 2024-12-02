package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;

import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IncomingSubcategoryRepository extends JpaRepository<IncomingSubcategory, Integer> {
    Optional<IncomingSubcategory> findBySubCategoryName(String incomingSubcategoryName);

    @Query("""
            SELECT isc FROM IncomingSubcategory isc
            JOIN FETCH isc.incomingCategory
            """)
    List<IncomingSubcategory> findAllSubcategory();
}
