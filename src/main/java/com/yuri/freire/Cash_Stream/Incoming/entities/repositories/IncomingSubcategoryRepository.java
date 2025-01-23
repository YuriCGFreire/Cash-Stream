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
            AND isc.user.username = :username
            """)
    Optional<IncomingSubcategory> findBySubcategoryId(@Param("incomingSubcategoryId") Integer incomingSubcategoryId, @Param("username") String username );

    @Query("""
            SELECT isc
            FROM IncomingSubcategory isc
            WHERE isc.subCategoryName = :subCategoryName
            AND isc.deletedAt IS NULL
            AND isc.user.username = :username
            """)
    Optional<IncomingSubcategory> findBySubCategoryName(@Param("subCategoryName") String subCategoryName, @Param("username") String username );

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse(
            isc.incomingSubcategoryId,
            isc.subCategoryName,
            isc.incomingCategory.categoryName,
            isc.user.username
            )
            FROM IncomingSubcategory isc
            JOIN isc.incomingCategory
            JOIN isc.user
            WHERE isc.deletedAt IS NULL
            AND isc.user.username = :username
            """)
    Page<IncomingSubcategoryResponse> findAllSubcategory(Pageable pageable, @Param("username") String username );

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse(
            isc.incomingSubcategoryId,
            isc.subCategoryName,
            isc.incomingCategory.categoryName,
            isc.user.username
            )
            FROM IncomingSubcategory isc
            JOIN isc.incomingCategory ic
            WHERE ic.categoryName = :categoryName    
            AND isc.deletedAt IS NULL    
            AND isc.user.username = :username
            """)
    Page<IncomingSubcategoryResponse> findAllByCategory(@Param("categoryName") String categoryName, Pageable pageable, @Param("username") String username );
}