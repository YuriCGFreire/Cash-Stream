package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IncomingCategoryRepository extends JpaRepository<IncomingCategory, Integer> {

    Optional<IncomingCategory> findByCategoryName(String incomingCategory);

    @Query("""
            SELECT new com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse(
            ic.incomingCategoryId,
            in.categoryName
            )
            FROM IncomingCategory ic
            """)
    Page<IncomingCategoryResponse> findAllIncomingCategory(Pageable pageable);
}
