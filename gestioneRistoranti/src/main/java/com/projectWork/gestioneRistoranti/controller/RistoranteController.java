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

	// metodo per cercare un ristorante dalla descrizione
	@GetMapping("/descrizione")
	public Object getRistoranteByDescription(@RequestBody String descrizione) {
		List<Ristorante> ristorantino = ristoranteRepository.findAll();
		List<Ristorante> ristorantino2 = null;
		for (Ristorante r : ristorantino) {
			if (r.getDescrizione().toLowerCase().contains(descrizione)) {
				ristorantino2.add(r);
			}
		}
		if (ristorantino2.isEmpty()) {
			return null;
		}
		return ristorantino2;
	}

	/*
	 * Metodo per creare un ristorante
	 * 
	 * @param oggetto di tipo Ristorante da aggiungere
	 * 
	 * @return -> messaggio, in caso di successo
	 */
	@PostMapping
	public Object createRistorante(@RequestBody Ristorante nuovoRistorante) {
		ristoranteRepository.save(nuovoRistorante);
		return Collections.singletonMap("message", "Ristorante aggiunto con successo!");
	}

	// metodo che restituisce la lista completa dei ristoranti.
	@GetMapping
	public List<Ristorante> getAllRistorante() {

		return ristoranteRepository.findAll();
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
		Optional<Ristorante> ristorante = ristoranteRepository.findById(id);
		if (ristorante.isPresent() && !"RISTORATORE".equals(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Ristorante non trovato");
		}
		Ristorante updatedRistorante = ristorante.get();
		updatedRistorante.setNome(ristoranteDetails.getNome());
		updatedRistorante.setEmail(ristoranteDetails.getEmail());
		updatedRistorante.setDescrizione(ristoranteDetails.getDescrizione());
		updatedRistorante.setPartitaIva(ristoranteDetails.getPartitaIva());
		updatedRistorante.setIndirizzo(ristoranteDetails.getIndirizzo());
		updatedRistorante.setCitta(ristoranteDetails.getCitta());
		updatedRistorante.setNumeroTelefono(ristoranteDetails.getNumeroTelefono());
		return ristoranteRepository.save(updatedRistorante);

	}

	// metodo per prendere autenticazione dell'utente
	private Utente getAuthenticatedUtente(HttpServletRequest request) {
		// TODO Auto-generated method stub
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
		Optional<Ristorante> ristorante = ristoranteRepository.findById(id);
		if (ristorante.isPresent() && !"RISTORATORE".equals(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Ristorante non trovato");
		}
		ristoranteRepository.delete(ristorante.get());
		return Collections.singletonMap("message", "Ristorante cancellato con successo");

	}

}