package com.example.apiyazumy.repository;

import com.example.apiyazumy.entity.CodigoValidacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionRepository extends JpaRepository<CodigoValidacion, Integer> {
    Optional<CodigoValidacion> findByCodigoValidacion(String codigoValidacion);
}
