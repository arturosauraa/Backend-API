package com.network.crud.repository;

import com.network.crud.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    @Query("select v from VerificationToken v where v.token = ?1")
    VerificationToken findByToken(final String token);

    @Transactional
    @Modifying
    @Query("delete from VerificationToken v where v.token = ?1")
    void removeByToken(String token);


}
