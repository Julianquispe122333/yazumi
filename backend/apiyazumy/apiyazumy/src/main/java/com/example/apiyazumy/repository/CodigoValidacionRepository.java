package com.example.apiyazumy.repository;

import com.example.apiyazumy.entity.CodigoValidacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodigoValidacionRepository extends JpaRepository<CodigoValidacion, Integer> {

    Optional<CodigoValidacion> findByCodigo(String codigo);

}