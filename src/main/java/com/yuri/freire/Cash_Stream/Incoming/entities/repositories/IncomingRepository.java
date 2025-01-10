package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IncomingRepository extends JpaRepository<Incoming, Integer> {

    @Query("""
            SELECT i
            FROM Incoming i
            WHERE i.incomingId = :incomingId
            AND i.deletedAt IS NULL
            """)
    Optional<Incoming> findIncomingById(@Param("incomingId") Integer incomingId);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse(
            i.incomingId,
            i.incomingDescription,
            i.grossIncoming,
            i.netIncoming,
            i.incomingDate,
            i.recurrence.recurrenceFrequency,
            i.incomingCategory.categoryName,
            i.incomingSubcategory.subCategoryName            
            )
            FROM Incoming i
            JOIN i.recurrence
            JOIN i.incomingCategory
            JOIN i.incomingSubcategory
            WHERE i.deletedAt IS NULL
            """)
    Page<IncomingResponse> findAllIncomings(Pageable pageable);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse(
            i.incomingId,
            i.incomingDescription,
            i.grossIncoming,
            i.netIncoming,
            i.incomingDate,
            i.recurrence.recurrenceFrequency,
            i.incomingCategory.categoryName,
            i.incomingSubcategory.subCategoryName            
            )
            FROM Incoming i
            JOIN i.recurrence
            JOIN i.incomingCategory ic
            JOIN i.incomingSubcategory
            WHERE ic.categoryName = :categoryName
            AND i.deletedAt IS NULL
            """)
    Page<IncomingResponse> findAllByCategory(@Param("categoryName") String categoryName, Pageable pageable);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse(
            i.incomingId,
            i.incomingDescription,
            i.grossIncoming,
            i.netIncoming,
            i.incomingDate,
            i.recurrence.recurrenceFrequency,
            i.incomingCategory.categoryName,
            i.incomingSubcategory.subCategoryName            
            )
            FROM Incoming i
            JOIN i.recurrence
            JOIN i.incomingCategory
            JOIN i.incomingSubcategory isc
            WHERE isc.subCategoryName = :subCategoryName
            AND i.deletedAt IS NULL
            """)
    Page<IncomingResponse> findAllBySubcategory(@Param("subCategoryName") String subcategoryName, Pageable pageable);
}