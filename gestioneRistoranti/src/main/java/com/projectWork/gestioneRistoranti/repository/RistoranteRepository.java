package com.projectWork.gestioneRistoranti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.projectWork.gestioneRistoranti.model.Ristorante;
import java.util.List;

@Repository
public interface RistoranteRepository extends JpaRepository<Ristorante, Long> {
	
	List<Ristorante> findByDescrizioneContainingIgnoreCase(String descrizione);

}
