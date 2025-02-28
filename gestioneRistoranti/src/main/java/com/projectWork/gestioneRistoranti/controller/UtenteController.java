package com.projectWork.gestioneRistoranti.controller;

import com.projectWork.gestioneRistoranti.auth.TokenService;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/utente")
@CrossOrigin(origins = {}) // Disabilita richieste CORS da origini esterne
public class UtenteController {
	
	@Autowired
	private UtenteRepository utenteRepository;
	
	@Autowired
	private TokenService tokenService;
	
	// metodo per aggiungere un utente
	 @PostMapping("/aggiungi")
	 public Object addUtente(@RequestBody Utente nuovoUtente) {
		 if (nuovoUtente.getNumeroCarta() == null || nuovoUtente.getNumeroCarta().isEmpty()) {
			 return Collections.singletonMap("message", "il numero della carta è obbligatorio");
			 }
		 if(nuovoUtente.getNumeroCarta().length() !=16 || !nuovoUtente.getNumeroCarta().matches("[0-9]+")) {
			 return Collections.singletonMap("message", "Il numero della carta deve avere esattamente 16 cifre e contenere solo numeri.");
			 }
		 utenteRepository.save(nuovoUtente);
		 return Collections.singletonMap("message", "Utente aggiunto con successo!");
		 }
		 
	 
	/*	Metodo per restituire i dati di un singolo utente
	 *  
	 *  @param id		per ricercare l'utente una volta ottenute le sue info dal token
	 * 	@param request	Oggetto per leggere l'header "Authorization"
	 *	@param response	Oggetto per impostare lo status in caso di errore
	 *	@return	L'oggetto Utente, se i controlli precedenti vengono passati (false)	 
	 */
	@GetMapping("/{id}")
	public Object getUtenteDetails(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		// otteniamo l'utente autenticato dal token
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return Collections.singletonMap("message", "Non autorizzato");
		}
		
		// attraverso l'id cerchiamo l'utente
		Optional<Utente> utenteOpt = utenteRepository.findById(id);
		if (!utenteOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Utente non trovato");
		}
		
		// con il metodo get ritorniamo l'oggetto Utente
		return utenteOpt.get();
	}
	
	/* Metodo per aggiornare i dati dell'utente
	 * 
	 * @param	id	->	id dell'utente di cui andremo a modificare i dati
	 * @param	utenteMod	->	oggetto con i nuovi dati
	 * @param	request
	 * @param	response
	 * @return	in caso di successo, il 'nuovo' oggetto utente; in caso contrario errore
	 */
	@PutMapping("/{id}")
	public Object updateUtente(@PathVariable Long id, @RequestBody Utente utenteMod, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if(authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		Optional<Utente> utenteOpt = utenteRepository.findById(id);
		if (!utenteOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Utente non trovato");
		}
		
		Utente utente = utenteOpt.get();
		utente.setNome(utenteMod.getNome());
		utente.setCognome(utenteMod.getCognome());
		utente.setEmail(utenteMod.getEmail());
		utente.setPassword(utenteMod.getPassword());
		utente.setNumeroCarta(utenteMod.getNumeroCarta());
		
		return utenteRepository.save(utente);
	
	}
	
	/*
	 * Metodo per ottenere tramite l'id di un utente (ristorante) la lista delle sue attività
	 * 
	 * @param	id	->	id dell'utente
	 * @param	request
	 * @param	response
	 * @return	lista completa dei ristoranti del ristoratore (se presenti)
	 */
	@GetMapping("/{id}/ristoranti")
	public Object getAllRistorantiByUtente(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if(authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		// dopo aver verificato l'autenticazione, controlliamo che l'utente abbia il ruolo 'ristoratore'
		/*if(!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per accaedere");
		}*/
		if (authUtente.getRuolo() != Utente.Ruolo.RISTORATORE) {
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    return Collections.singletonMap("message", "Non hai i permessi per accedere");
		}

		Optional<Utente> utenteOpt = utenteRepository.findById(id);
		if (!utenteOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Utente non trovato");
		}
		
		Utente u = utenteOpt.get();
		
		return u.getRistoranti();
		
	}
	
	/*
	 * Metodo per ottenere tramite l'id di un utente la lista completa dei suoi ordini
	 * 
	 * @param	id	->	id dell'utente
	 * @param	request
	 * @param	response
	 * @return	lista completa degli ordini effettuati dall'utente
	 */
	@GetMapping("/{id}/ordini")
	public Object getAllOrdiniByUtente(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if(authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		Optional<Utente> utenteOpt = utenteRepository.findById(id);
		if (!utenteOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Utente non trovato");
		}
		
		Utente u = utenteOpt.get();
		
		return u.getOrdini();
		
	}
	
	/* 
	 * Metodo per cancellare l'utente
	 */
	@DeleteMapping("/{id}")
	public Object deleteUtente(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if(authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		Optional<Utente> utenteOpt = utenteRepository.findById(id);
		if(!utenteOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Utente non trovato");
		}
		
		Utente utente = utenteOpt.get();
		utenteRepository.delete(utente);
		return Collections.singletonMap("message", "Utente cancellato con successo");
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
