package com.capricho.ms_catalogo.seeds;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.capricho.ms_catalogo.model.Categoria;
import com.capricho.ms_catalogo.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner{
	
	private final CategoriaRepository categoriaRepository;

	@Override
	public void run(String... args) throws Exception {
		
		if(categoriaRepository.count()==0) {
			
			categoriaRepository.save(new Categoria(null, "Bebidas",true) );
			categoriaRepository.save(new Categoria(null,"Postres",true));
			categoriaRepository.save(new Categoria(null,"Snacks",true));
			
			System.out.println("Datos iniciales cargados correctamente");
		}
	}
	
}
