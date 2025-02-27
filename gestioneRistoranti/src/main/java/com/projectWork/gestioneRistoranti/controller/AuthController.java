package com.projectWork.gestioneRistoranti.controller;

import com.projectWork.gestioneRistoranti.auth.*;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {}) // Disabilita richieste CORS da origini esterne
public class AuthController {
	
	@Autowired
	private UtenteRepository utenteRepository;
	
	@Autowired
	private TokenService tokenService;
	
	/* Metodo per effettuare il login
	 * 
	 * @param	body		Mappa contenente i dati per effettuare il login (email, password)
	 * @param	response	oggetto per impostare lo stato in caso di errore
	 * @return	mappa con un messaggio di conferma, il ruolo dell'utente e il token
	 */
	@PostMapping("/login")
	public Map<String, String> login(@RequestBody Map<String, String> body, HttpServletResponse response) {
		
		// estrazione dati login dalla richiesta JSON
		String email = body.get("email");
		String password = body.get("password");
		
		Map<String, String> result = new HashMap<>();
		
		// verifica dei dati forniti
		if (email == null || password == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			result.put("message", "Credenziali non valide");
			return result;
		}
		
		// ricerca dell'utente nel DB tramite email
		Optional<Utente> optionalUtente = utenteRepository.findByEmail(email);
		
		if(!optionalUtente.isPresent() || !optionalUtente.get().getPassword().equals(password)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			result.put("message", "Credenziali non valide");
			return result;
		}
		
		// generiamo un token associato all'utente
		String token = tokenService.generateToken(email);
		
		// Costruiamo la risposta
		result.put("message", "Login effettuato con successo");
		result.put("token", token);
		
		return result;
	}
	
	/* Metodo per effettuare il logout
	 * 
	 * @param	authHeader	->	header contenente 
	 * @return	mappa con un messaggio di conferma del logout
	 */
	@PostMapping("/logout")
	public Map<String, String> logout(@RequestHeader("Authorization") String authHeader) {
		String token = null;
		
		if(authHeader != null && authHeader.startsWith("Bearer "))
			token = authHeader.substring(7);
		else token = authHeader;
		
		tokenService.removeToken(token);
		Map<String, String> result = new HashMap<>();
		result.put("message", "Logout effettuato con successo");
		return result;
	}
	
	
}