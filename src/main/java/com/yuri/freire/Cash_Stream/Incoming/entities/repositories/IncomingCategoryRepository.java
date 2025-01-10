package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IncomingCategoryRepository extends JpaRepository<IncomingCategory, Integer> {


    @Query("""
            SELECT ic 
            FROM IncomingCategory ic
            WHERE ic.incomingCategoryId = :incomingCategoryId
            AND ic.deletedAt IS NULL
            """)
    Optional<IncomingCategory> findByCategoryId(@Param("incomingCategoryId") Integer incomingCategoryId);

    @Query("""
            SELECT ic 
            FROM IncomingCategory ic
            WHERE ic.categoryName = :categoryName
            AND ic.deletedAt IS NULL
            """)
    Optional<IncomingCategory> findByCategoryName(@Param("categoryName") String incomingCategory);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse(
            ic.incomingCategoryId,
            ic.categoryName
            )
            FROM IncomingCategory ic
            WHERE ic.deletedAt IS NULL
            """)
    Page<IncomingCategoryResponse> findAllIncomingCategory(Pageable pageable);
}
