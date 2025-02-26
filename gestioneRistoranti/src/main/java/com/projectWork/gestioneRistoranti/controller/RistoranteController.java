package com.projectWork.gestioneRistoranti.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectWork.gestioneRistoranti.auth.TokenService;
import com.projectWork.gestioneRistoranti.model.Ristorante;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.repository.RistoranteRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/ristorante")
@CrossOrigin(origins = {}) // Disabilita richieste CORS da origini esterne
public class RistoranteController {

	@Autowired
	private RistoranteRepository ristoranteRepository;
	
	@Autowired
	private TokenService tokenService;

	/*
	 * Metodo per creare un ristorante
	 * 
	 * @param oggetto di tipo Ristorante da aggiungere
	 * 
	 * @return -> messaggio, in caso di successo
	 */
	@PostMapping
	public Object createRistorante(@RequestBody Ristorante nuovoRistorante, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		if(!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare questa operazione, cambiare il tipo di account");
		}
		
		nuovoRistorante.setUtente(authUtente);
		ristoranteRepository.save(nuovoRistorante);
		return Collections.singletonMap("message", "Ristorante aggiunto con successo!");
	}

	/* Metodo che restituisce la lista completa dei ristoranti.
	 * 
	 * @return -> la lista completa dei ristoranti
	*/
	@GetMapping
	public List<Ristorante> getAllRistoranti() {
		return ristoranteRepository.findAll();	
	}
	
	/* Metodo che ritorna i dettagli di un ristorante dal suo id
	 * 
	 * 
	 */
	@GetMapping("/{id}")
	public Object getRistoranteDetailsById(@PathVariable Long id, HttpServletResponse response) {
		Optional<Ristorante> restOpt = ristoranteRepository.findById(id);
		if(!restOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Nessun ristorante trovato");
		}
		
		/* a differenza di altri metodi, se la ricerca è andata a buon fine non occorrerà
		fare questa operazione su un nuovo oggetto 'Ristorante'*/
		return restOpt.get();
	}
	
	/* Metodo che restituisce tutti i menù per ristorante
	 * 
	 * @param 	id del ristorante che ricaveremo dall'endpoint
	 * @param	response
	 * 
	 * @return	oggetto, in caso di successo, con le informazioni su tutti i menù
	 */
	@GetMapping("/{id}/menu")
	public Object getAllMenuByRistoranteId(@PathVariable Long id, HttpServletResponse response) {
		Optional<Ristorante> restOpt = ristoranteRepository.findById(id);
		if(!restOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Nessun menù trovato per il seguente ristorante");
		}
		
		Ristorante r = restOpt.get();
		
		return r.getMenu();
	}

	/**
	 * endpoint che modifica i dettagli di un singolo ristorante. Richiede il token
	 * di autenticazione nell'header "Authorization".
	 *
	 * @param id       L'ID del ristorante da modificare
	 * @param request  Oggetto HttpServletRequest per leggere l'header
	 *                 "Authorization"
	 * @param response Oggetto HttpServletResponse per impostare lo status in caso
	 *                 di errore
	 * @return L'oggetto Ristorante se trovato, altrimenti un messaggio di errore
	 */

	@PutMapping("/{id}")
	public Object updateRistorante(@PathVariable Long id, @RequestBody Ristorante ristoranteDetails,
			HttpServletRequest request, HttpServletResponse response) {

		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		if(!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare questa operazione, cambiare il tipo di account");
		}
		
		Optional<Ristorante> ristorante = ristoranteRepository.findById(id);
		if (!ristorante.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Ristorante non trovato");
		}
		Ristorante updatedRistorante = ristorante.get();
		updatedRistorante.setUtente(authUtente);
		updatedRistorante.setNome(ristoranteDetails.getNome());
		updatedRistorante.setEmail(ristoranteDetails.getEmail());
		updatedRistorante.setDescrizione(ristoranteDetails.getDescrizione());
		updatedRistorante.setPartitaIva(ristoranteDetails.getPartitaIva());
		updatedRistorante.setIndirizzo(ristoranteDetails.getIndirizzo());
		updatedRistorante.setCitta(ristoranteDetails.getCitta());
		updatedRistorante.setNumeroTelefono(ristoranteDetails.getNumeroTelefono());
		return ristoranteRepository.save(updatedRistorante);

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

	/*
	 * Metodo per cancellare un ristorante
	 */
	@DeleteMapping("/{id}")
	public Object deleteRistorante(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		if(!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare questa operazione, cambiare il tipo di account");
		}
		
		Optional<Ristorante> ristorante = ristoranteRepository.findById(id);
		if (!ristorante.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Ristorante non trovato");
		}
		ristoranteRepository.delete(ristorante.get());
		return Collections.singletonMap("message", "Ristorante cancellato con successo");

	}

}