package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IncomingRepository extends JpaRepository<Incoming, Integer> {

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse(
            i.incomingId,
            i.incomingDescription,
            i.incomingSource,
            i.grossIncoming,
            i.netIncoming,
            i.recurrence.recurrenceFrequency,
            i.incomingCategory.categoryName,
            i.incomingSubcategory.subCategoryName            
            )
            FROM Incoming i
            JOIN i.recurrence
            JOIN i.incomingCategory
            JOIN i.incominSubcategory
            """)
    Page<IncomingResponse> findAllIncomings(Pageable pageable);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse(
            i.incomingId,
            i.incomingDescription,
            i.incomingSource,
            i.grossIncoming,
            i.netIncoming,
            i.recurrence.recurrenceFrequency,
            i.incomingCategory.categoryName,
            i.incomingSubcategory.subCategoryName            
            )
            FROM Incoming i
            JOIN i.recurrence
            JOIN i.incomingCategory ic
            JOIN i.incominSubcategory
            WHERE ic.categoryName = :categoryName
            """)
    Page<IncomingResponse> findAllByCategory(@Param("categoryName") String categoryName, Pageable pageable);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse(
            i.incomingId,
            i.incomingDescription,
            i.incomingSource,
            i.grossIncoming,
            i.netIncoming,
            i.recurrence.recurrenceFrequency,
            i.incomingCategory.categoryName,
            i.incomingSubcategory.subCategoryName            
            )
            FROM Incoming i
            JOIN i.recurrence
            JOIN i.incomingCategory
            JOIN i.incominSubcategory isc
            WHERE isc.subCategoryName = :subCategoryName
            """)
    Page<IncomingResponse> findAllBySubcategory(@Param("subCategoryName") String subcategoryName, Pageable pageable);
}