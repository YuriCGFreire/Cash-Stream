package com.yuri.freire.Cash_Stream.entities.repositories;

import com.yuri.freire.Cash_Stream.entities.Incoming;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomingRepository extends JpaRepository<Incoming, Integer> {
    List<Incoming> findAllIncomings();
}