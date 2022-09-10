package com.network.crud.repository;

import com.network.crud.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepo extends JpaRepository<AppUser, Integer> {
}
