package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncomingRepository extends JpaRepository<Incoming, Integer> {

    @Query("""
            SELECT i FROM Incoming i
            JOIN FETCH i.incomingCategory
            JOIN FETCH i.incomingSubcategory
            JOIN FETCH i.recurrence
            """)
    List<Incoming> findAllIncomings();

    @Query("""
            SELECT i from Incoming i 
            JOIN FETCH i.incomingCategory
            JOIN FETCH i.incomingSubcategory
            JOIN FETCH i.recurrence
            WHERE i.incomingCategory.categoryName = :categoryName
            """)
    List<Incoming> findAllByCategory(@Param("categoryName") String categoryName);

    @Query("""
            SELECT i from Incoming i 
            JOIN FETCH i.incomingCategory
            JOIN FETCH i.incomingSubcategory
            JOIN FETCH i.recurrence
            WHERE i.incomingSubcategory.subCategoryName = :subcategoryName
            """)
    List<Incoming> findAllBySubcategory(@Param("subcategoryName") String subcategoryName);
}