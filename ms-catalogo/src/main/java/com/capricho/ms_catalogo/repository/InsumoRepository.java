package com.capricho.ms_catalogo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capricho.ms_catalogo.model.Insumo;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {

	Optional<Insumo> findByNombre(String nombre);
}
