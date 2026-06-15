package com.example.apiyazumy.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.apiyazumy.entities.RecuperacionPassword;

@Repository
public interface RecuperacionPasswordRepository
        extends JpaRepository<RecuperacionPassword, Integer> {

    Optional<RecuperacionPassword> findByToken(String token);

}