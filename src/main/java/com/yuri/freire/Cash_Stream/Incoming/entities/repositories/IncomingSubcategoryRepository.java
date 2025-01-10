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
    @Query("""
            SELECT isc
            FROM IncomingSubcategory isc
            WHERE isc.incomingSubcategoryId = :incomingSubcategoryId
            AND isc.deletedAt IS NULL
            """)
    Optional<IncomingSubcategory> findBySubcategoryId(@Param("incomingSubcategoryId") Integer incomingSubcategoryId);

    @Query("""
            SELECT isc
            FROM IncomingSubcategory isc
            WHERE isc.subCategoryName = :subCategoryName
            AND isc.deletedAt IS NULL
            """)
    Optional<IncomingSubcategory> findBySubCategoryName(@Param("subCategoryName") String subCategoryName);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse(
            isc.incomingSubcategoryId,
            isc.subCategoryName,
            isc.incomingCategory.categoryName
            )
            FROM IncomingSubcategory isc
            JOIN isc.incomingCategory
            WHERE isc.deletedAt IS NULL
            """)
    Page<IncomingSubcategoryResponse> findAllSubcategory(Pageable pageable);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse(
            isc.incomingSubcategoryId,
            isc.subCategoryName,
            isc.incomingCategory.categoryName
            )
            FROM IncomingSubcategory isc
            JOIN isc.incomingCategory ic
            WHERE ic.categoryName = :categoryName    
            AND isc.deletedAt IS NULL        
            """)
    Page<IncomingSubcategoryResponse> findAllByCategory(@Param("categoryName") String categoryName, Pageable pageable);
}
