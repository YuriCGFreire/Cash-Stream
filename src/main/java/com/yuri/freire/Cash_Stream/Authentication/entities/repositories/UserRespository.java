package com.yuri.freire.Cash_Stream.Authentication.entities.repositories;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRespository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
