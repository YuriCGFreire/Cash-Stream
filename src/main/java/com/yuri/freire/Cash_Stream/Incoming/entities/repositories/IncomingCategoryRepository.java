package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;

import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomingCategoryRepository extends JpaRepository<IncomingCategory, Integer> {
    IncomingCategory findByCategoryName(String incomingCategory);
}
