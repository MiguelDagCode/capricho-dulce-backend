package com.capricho.ms_usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capricho.ms_usuarios.model.Distrito;

@Repository
public interface DistritoRepository extends JpaRepository<Distrito, Integer>{
	Optional<Distrito> findByNombre(String nombre);
}
