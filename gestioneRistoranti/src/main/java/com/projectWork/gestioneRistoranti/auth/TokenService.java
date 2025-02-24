package com.projectWork.gestioneRistoranti.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.repository.UtenteRepository;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
	
	@Autowired
	private UtenteRepository utenteRepository;
	
	/*
	 * Metodo per generare un token casuale da associare all'utente
	 * 
	 * @param email	->	email dell'utente
	 * 
	 * @return il token generato
	*/
	public String generateToken(String email) {
		// generiamo il token attraverso UUID
		String token = UUID.randomUUID().toString();
		
		// lo stampiamo in console
		System.out.println("Token generato: "+token);
		
		// attraverso l'optional ricerchiamo l'utente via mail
		Optional<Utente> u = utenteRepository.findByEmail(email);
		
		// creiamo un oggetto per raccogliere il risultato di optional
		Utente utente = u.get();
		
		// tramite 'utente' modifichiamo il valore del token
		utente.setToken(token);
		
		// salviamo utente
		utenteRepository.save(utente);
		
		return token;
	}
	
	/*
	 * Metodo che restitusce l'oggetto utente associato al token
	 * 
	 * @param token utente
	 * @return	l'oggetto Utente associato al token
	 */
	public Utente getAuthUtente(String token) {
		Optional<Utente> u = utenteRepository.findByToken(token);
		Utente utente = u.get();
		return utente;
	}
	
	/*
	 * Metodo per rimuovere il token, in operazioni quali il logout
	 * 
	 * @param token	->	il token da rimuovere
	 */
	public void removeToken(String token) { 
		Optional<Utente> u = utenteRepository.findByToken(token);
		Utente utente = u.get();
		utente.setToken(null);
		utenteRepository.save(utente);
	}
}
