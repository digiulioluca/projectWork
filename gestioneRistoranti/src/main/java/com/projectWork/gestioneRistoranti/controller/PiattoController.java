package com.projectWork.gestioneRistoranti.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectWork.gestioneRistoranti.model.Piatto;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.repository.PiattoRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/piatti")
public class PiattoController {

	@Autowired
	private PiattoRepository piattoRepository;

	// metodo che restituisce la lista completa dei piatti.
	@GetMapping
	public List<Piatto> LeggiPiatti() {
		return piattoRepository.findAll();
	}

	/*
	 * Metodo per creare un piatto
	 * 
	 * @param oggetto di tipo Piatto da aggiungere
	 * 
	 * @return -> messaggio, in caso di successo
	 */
	@PostMapping
	public Object createPiatto(Piatto nuovoPiatto) {
		piattoRepository.save(nuovoPiatto);
		return Collections.singletonMap("message", "Piatto aggiunto con successo!");
	}

	// metodo per restituire i dati di un singolo piatto.
	@GetMapping("/{id}")
	public Optional<Piatto> leggiPiattoById(@PathVariable Long id) {
		return piattoRepository.findById(id);
	}

	/*
	 * Metodo per aggiornare i dati del piatto
	 * 
	 * @param id -> id del piatto di cui andremo a modificare i dati
	 * 
	 * @param piattoDetails -> oggetto con i nuovi dati
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return in caso di successo, il 'nuovo' oggetto piatto; in caso contrario
	 * errore
	 */
	@PutMapping("/{id}")
	public Object updatePiatto(@PathVariable Long id, @RequestBody Piatto piattoDetails, HttpServletResponse response,
			HttpServletRequest request) {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		Optional<Piatto> piatto = piattoRepository.findById(id);
		if (!piatto.isPresent() && !"RISTORATORE".equals(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Piatto non trovato");
		}
		Piatto p = piatto.get();
		p.setNome(piattoDetails.getNome());
		p.setCosto(piattoDetails.getCosto());
		p.setDescrizione(piattoDetails.getDescrizione());
		p.setCosto(piattoDetails.getCosto());
		p.setCategoria(piattoDetails.getCategoria());

		return piattoRepository.save(p);
	}

	// metodo per prendere autenticazione dell'utente
	private Utente getAuthenticatedUtente(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Metodo per cancellare un piatto
	 */
	@DeleteMapping("/{id}")
	public Object deletePiatto(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		Optional<Piatto> p = piattoRepository.findById(id);
		if (p.isPresent() && !"RISTORATORE".equals(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Piatto non trovato");
		}
		piattoRepository.delete(p.get());
		return Collections.singletonMap("message", "Piatto cancellato con successo");
	}
}
