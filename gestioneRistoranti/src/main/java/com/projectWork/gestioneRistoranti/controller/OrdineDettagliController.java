package com.projectWork.gestioneRistoranti.controller;

import com.projectWork.gestioneRistoranti.auth.TokenService;
import com.projectWork.gestioneRistoranti.model.OrdineDettagli;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.model.Ordine;
import com.projectWork.gestioneRistoranti.repository.OrdineDettagliRepository;
import com.projectWork.gestioneRistoranti.repository.OrdineRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dettagli")
@CrossOrigin(origins = {})
public class OrdineDettagliController {
	
	@Autowired
	private OrdineDettagliRepository ordineDettagliRepository;
	
	@Autowired
	private OrdineRepository ordineRepository;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public Object dettaglioNuovoOrdine(@PathVariable Long id, @RequestBody OrdineDettagli dettaglio, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if(authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		Optional<Ordine> ordineOpt = ordineRepository.findById(authUtente.getId());
		if (!ordineOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Ordine non trovato");
		}
		
		dettaglio.setOrdine(ordineOpt.get());
		
		return ordineDettagliRepository.save(dettaglio);
	}
	
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
            return tokenService.getAuthUtente(token);
        }
        // Se non c'è header "Authorization", restituisce null
        return null;
    }
}
