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
            AND i.user.username = :username
            """)
    Optional<Incoming> findIncomingById(@Param("username") String username, @Param("incomingId") Integer incomingId);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse(
            i.incomingId,
            i.incomingDescription,
            i.grossIncoming,
            i.netIncoming,
            i.incomingDate,
            i.recurrence.recurrenceFrequency,
            i.incomingCategory.categoryName,
            i.incomingSubcategory.subCategoryName,
            i.user.username        
            )
            FROM Incoming i
            JOIN i.recurrence
            JOIN i.incomingCategory
            JOIN i.incomingSubcategory
            JOIN i.user
            WHERE i.deletedAt IS NULL
            AND i.user.username = :username
            """)
    Page<IncomingResponse> findAllIncomings(@Param("username") String username ,Pageable pageable);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse(
            i.incomingId,
            i.incomingDescription,
            i.grossIncoming,
            i.netIncoming,
            i.incomingDate,
            i.recurrence.recurrenceFrequency,
            i.incomingCategory.categoryName,
            i.incomingSubcategory.subCategoryName,
            i.user.username            
            )
            FROM Incoming i
            JOIN i.recurrence
            JOIN i.incomingCategory ic
            JOIN i.incomingSubcategory
            JOIN i.user
            WHERE ic.categoryName = :categoryName
            AND i.deletedAt IS NULL
            AND i.user.username = :username
            """)
    Page<IncomingResponse> findAllByCategory(@Param("username") String username, @Param("categoryName") String categoryName, Pageable pageable);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse(
            i.incomingId,
            i.incomingDescription,
            i.grossIncoming,
            i.netIncoming,
            i.incomingDate,
            i.recurrence.recurrenceFrequency,
            i.incomingCategory.categoryName,
            i.incomingSubcategory.subCategoryName,
            i.user.username           
            )
            FROM Incoming i
            JOIN i.recurrence
            JOIN i.incomingCategory
            JOIN i.incomingSubcategory isc
            JOIN i.user
            WHERE isc.subCategoryName = :subCategoryName
            AND i.deletedAt IS NULL
            AND i.user.username = :username
            """)
    Page<IncomingResponse> findAllBySubcategory(@Param("username") String username, @Param("subCategoryName") String subcategoryName, Pageable pageable);
}