package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;

import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomingRepository extends JpaRepository<Incoming, Integer> {
    List<Incoming> findAll();
}