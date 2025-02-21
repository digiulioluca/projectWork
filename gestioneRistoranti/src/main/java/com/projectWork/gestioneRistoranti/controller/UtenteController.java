package com.projectWork.gestioneRistoranti.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springcookie.auth.AuthUser;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.repository.UtenteRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/utente")
@CrossOrigin(origins = {})
public class UtenteController {

	
	@Autowired
	private UtenteRepository utenteRepository;
	
	@Autowired
	private TokenService tokenService;
	
	//metodo per aggiungere un utente
	@PostMapping("/aggiungi")
	public Object aggiungiUtente(@RequestBody Utente nuovoUtente, HttpServletRequest request, HttpServletResponse response) {
    	// Verifica l'autenticazione
    	Utente authUtente = getAuthenticatedUtente(request);
        if (authUtente == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return Collections.singletonMap("message", "Non autorizzato");
        }
        // Verifica che l'utente abbia il ruolo "admin"
        if (!"admin".equals(authUtente.getRuolo())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Collections.singletonMap("message", "Accesso negato: solo admin può aggiungere utenti");
        }
        // Salva il nuovo utente nel database
        nuovoUtente.setRuolo("user");
        nuovoUtente.setEmail(nuovoUtente.getEmail());
        utenteRepository.save(nuovoUtente);
        return Collections.singletonMap("message", "Utente aggiunto con successo");
    }
	//metodo per leggere dettagli di un singolo utente
	@GetMapping("/{id}")
    public Object getUtenteDetails(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
    	 // Ottiene l'utente autenticato dal token
    	Utente authUtente = getAuthenticatedUtente(request);
        if (authUtente == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return Collections.singletonMap("message", "Non autorizzato");
        }

        // Cerca l'utente per ID
        Optional<Utente> utenteOpt = utenteRepository.findById(id);
        if (!utenteOpt.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return Collections.singletonMap("message", "Utente non trovato");
        }
        return utenteOpt.get();
    }
	/**
     * Metodo di utilità per estrarre il token di autenticazione dall'header "Authorization".
     * Il token viene inviato nel formato "Bearer <token>".
     *
     * @param request Oggetto HttpServletRequest contenente gli header della richiesta
     * @return L'oggetto AuthUser associato al token, oppure null se il token non è presente o non valido
     */
    private Utente getAuthenticatedUtente(HttpServletRequest request) {
        // Legge l'header "Authorization"
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isEmpty()) {
            String token;
            // Se il token è inviato come "Bearer <token>", lo estrae
            if (authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            } else {
                token = authHeader;
            }
            // Usa il TokenService per ottenere l'utente associato al token
            return tokenService.getAuthUser(token);
        }
        // Se non c'è header "Authorization", restituisce null
        return null;
    }
	
}
