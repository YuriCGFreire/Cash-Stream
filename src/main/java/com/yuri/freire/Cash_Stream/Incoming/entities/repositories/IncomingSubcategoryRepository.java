package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IncomingSubcategoryRepository extends JpaRepository<IncomingSubcategory, Integer> {
    Optional<IncomingSubcategory> findBySubCategoryName(String incomingSubcategoryName);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse(
            isc.incomingSubcategoryId,
            isc.subCategoryName,
            isc.incomingCategory.categoryName
            )
            FROM IncomiingSucategory isc
            JOIN isc.incomingCategory
            """)
    Page<IncomingSubcategoryResponse> findAllSubcategory(Pageable pageable);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse(
            isc.incomingSubcategoryId,
            isc.subCategoryName,
            isc.incomingCategory.categoryName
            )
            FROM IncomiingSucategory isc
            JOIN isc.incomingCategory
            WHERE isc.categoryName = :categoryName            
            """)
    Page<IncomingSubcategoryResponse> findAllByCategory(@Param("categoryName") String categoryName, Pageable pageable);
}
