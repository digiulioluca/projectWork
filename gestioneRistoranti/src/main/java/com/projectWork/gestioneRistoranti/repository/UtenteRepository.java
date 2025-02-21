package com.projectWork.gestioneRistoranti.repository;

import com.projectWork.gestioneRistoranti.model.Utente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long>{
	
	/*
	 * metodo per ritornare un utente dalla sua email
	 * 
	 * @param email	->	e-mail dell'utente da ricercare
	 * @return Optional	-> contenente, o meno, l'utente ricercato via mail
	 */
	Optional<Utente> findByEmail(String email);
	
	/*
	 * metodo per ritornare un utente dal suo token di autenticazione
	 * 
	 * @param token	->	token dell'utente da ricercare
	 * @return Optional	-> contenente, o meno, l'utente ricercato via token
	 */
	Optional<Utente> findByToken(String token);
}
