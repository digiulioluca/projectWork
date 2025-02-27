package com.projectWork.gestioneRistoranti.controller;

import com.projectWork.gestioneRistoranti.auth.TokenService;
import com.projectWork.gestioneRistoranti.model.Ordine;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.repository.OrdineRepository;
import com.projectWork.gestioneRistoranti.repository.UtenteRepository;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/ordine")
@CrossOrigin(origins = {})
public class OrdineController {
	
	@Autowired
	private OrdineRepository ordineRepository;
	
	@Autowired
	private UtenteRepository utenteRepository;
	
	@Autowired
	private TokenService tokenService;
	
	/* Metodo per creare un ordine
	 * 
	 * @param	i dati da raccogliere nel body
	 * @param	id dell'utente che sta creando l'ordine
	 * @param	request
	 * @param	response
	 * @return	l'oggetto appena creato
	 */
	@PostMapping
	public Object creaOrdine(@RequestBody Ordine ordine, HttpServletResponse response, HttpServletRequest request) {
		Utente authUtente = getAuthenticatedUtente(request);
		if(authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message","Effettua il login prima di creare un ordine");
		}
		
		if(!"user".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per accaedere");
		}
		
		Optional<Utente> utenteOpt = utenteRepository.findById(authUtente.getId());
		if(!utenteOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Utente non trovato");
		}
		
		Utente utente = utenteOpt.get();
		
		Ordine nuovoOrdine = new Ordine();
		nuovoOrdine.setUtente(utente);
		nuovoOrdine.setDataOrdine(LocalDate.now());
		nuovoOrdine.setStatus(ordine.getStatus());
		nuovoOrdine.setTotale(ordine.getTotale());
		return ordineRepository.save(nuovoOrdine);
	}
	
	/* Metodo riservato al ristoratore per poter aggiornare lo status di un ordine
	 * 
	 */
	@PutMapping("/{id}/status")
	public Object updateStatus(@RequestBody Ordine nuovoStatus, @RequestParam Long id, HttpServletRequest request, HttpServletResponse response){
		Utente authUtente = getAuthenticatedUtente(request);
		if(authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		if(!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per accaedere");
		}
		
		Optional<Ordine> ordineOpt = ordineRepository.findById(id);
		if (!ordineOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Ordine non trovato");
		}
		
		Ordine ordine = ordineOpt.get();
		ordine.setStatus(nuovoStatus.getStatus());
		response.setStatus(HttpServletResponse.SC_OK);
		return Collections.singletonMap("message", "Status ordine aggiornato");
	}
	
	@DeleteMapping("/{id}")
	public Object deleteOrdine(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if(authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		Optional<Ordine> ordineOpt = ordineRepository.findById(id);
		if(!ordineOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Utente non trovato");
		}
		
		Ordine ordine = ordineOpt.get();
		ordineRepository.delete(ordine);
		return Collections.singletonMap("message", "Ordine cancellato con successo");
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
            return tokenService.getAuthUtente(token);
        }
        // Se non c'è header "Authorization", restituisce null
        return null;
    }
}
