package com.projectWork.gestioneRistoranti.repository;

import com.projectWork.gestioneRistoranti.model.Piatto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PiattoRepository extends JpaRepository<Piatto, Long>{
	
	/*
	 * metodo per ritornare un piatto dal suo nome
	 * 
	 * @param nome	->	nome del piatto da ricercare
	 * @return Optional	-> contenente, o meno, il piatto ricercato via nome
	 */
	Optional<Piatto> findByNome(String nome);
}
