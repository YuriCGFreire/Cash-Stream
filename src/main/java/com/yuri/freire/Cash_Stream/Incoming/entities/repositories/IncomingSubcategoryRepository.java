package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;

import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomingSubcategoryRepository extends JpaRepository<IncomingSubcategory, Integer> {
    IncomingSubcategory findBySubCategoryName(String incomingSubcategoryName);
}
