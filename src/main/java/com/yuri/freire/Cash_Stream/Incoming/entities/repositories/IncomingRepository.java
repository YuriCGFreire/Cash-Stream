package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IncomingRepository extends JpaRepository<Incoming, Integer> {

    @Query("""
            SELECT i FROM Incoming i
            JOIN FETCH i.incomingCategory
            JOIN FETCH i.incomingSubcategory
            JOIN FETCH i.recurrence
            """)
    Page<Incoming> findAllIncomings(Pageable pageable);

    @Query("""
            SELECT i from Incoming i 
            JOIN FETCH i.incomingCategory
            JOIN FETCH i.incomingSubcategory
            JOIN FETCH i.recurrence
            WHERE i.incomingCategory.categoryName = :categoryName
            """)
    Page<Incoming> findAllByCategory(@Param("categoryName") String categoryName, Pageable pageable);

    @Query("""
            SELECT i from Incoming i 
            JOIN FETCH i.incomingCategory
            JOIN FETCH i.incomingSubcategory
            JOIN FETCH i.recurrence
            WHERE i.incomingSubcategory.subCategoryName = :subcategoryName
            """)
    Page<Incoming> findAllBySubcategory(@Param("subcategoryName") String subcategoryName, Pageable pageable);
}