package com.yuri.freire.Cash_Stream.entities.repositories;

import com.yuri.freire.Cash_Stream.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, BigInteger> {
    @Query("""
        select t from Token t inner join User u on t.user.id = u.id
        where u.id = :userId and (t.isExpired = false or t.isRevoked = false)    
    """)
    List<Token> findAllValidTokensByUser(BigInteger userId);

    Optional<Token> findByToken(String token);
}
