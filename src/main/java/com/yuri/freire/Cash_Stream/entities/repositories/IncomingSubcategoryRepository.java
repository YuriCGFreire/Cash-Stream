package com.yuri.freire.Cash_Stream.entities.repositories;

import com.yuri.freire.Cash_Stream.entities.IncomingSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomingSubcategoryRepository extends JpaRepository<IncomingSubcategory, Integer> {
    IncomingSubcategory findBySubCategoryName(String incomingSubcategoryName);
}
