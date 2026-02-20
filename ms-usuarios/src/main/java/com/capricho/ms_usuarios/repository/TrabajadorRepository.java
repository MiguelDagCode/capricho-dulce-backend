package com.capricho.ms_usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capricho.ms_usuarios.model.Trabajador;

@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Integer> {
	boolean existsByDocumento(String documento);
}
