package com.projectWork.gestioneRistoranti.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.projectWork.gestioneRistoranti.auth.TokenService;
import com.projectWork.gestioneRistoranti.model.Categoria;
import com.projectWork.gestioneRistoranti.model.Piatto;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.repository.CategoriaRepository;
import com.projectWork.gestioneRistoranti.repository.PiattoRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/piatto")
public class PiattoController {

	@Autowired
	private PiattoRepository piattoRepository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private CategoriaRepository categoriaRepository;

	// metodo che restituisce la lista completa dei piatti.
	@GetMapping
	public List<Piatto> getAllPiatti() {
		return piattoRepository.findAll();
	}

	/*
	 * Metodo per creare un piatto
	 * 
	 * @param 	oggetto di tipo Piatto da aggiungere
	 * @param	id della categoria di riferimento
	 * @param	response
	 * @param	request
	 * 
	 * @return -> messaggio, in caso di successo
	 */
	@PostMapping("/{id}/nuovo")
	public Object createPiatto(Piatto nuovoPiatto, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) throws IOException {
		// authentication
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}

		if (!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare quest'operazione");
		}
		
		Optional<Categoria> findCat = categoriaRepository.findById(id);
		if(!findCat.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("messsage", "Piatto non trovato");
		}
		
		nuovoPiatto.setNomeFoto(file.getOriginalFilename());
		nuovoPiatto.setFoto(file.getBytes());
		nuovoPiatto.setCategoria(findCat.get());
		piattoRepository.save(nuovoPiatto);
		return Collections.singletonMap("message", "Piatto aggiunto con successo!");
	}

	/* Metodo per restituire i dati di un singolo piatto
	 * 
	 * @param 	id del piatto selezionato
	 * @param	response
	 * 
	 * @return	oggetto 
	 * 
	*/
	@GetMapping("/{id}")
	public Object leggiPiattoById(@PathVariable Long id, HttpServletResponse response) {
		Optional<Piatto> ricerca = piattoRepository.findById(id);
		if (!ricerca.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Piatto non trovato");
		}

		// con il metodo get ritorniamo l'oggetto Categoria
		return ricerca.get();
	}

	/*
	 * Metodo per aggiornare i dati del piatto
	 * 
	 * @param id -> id del piatto di cui andremo a modificare i dati
	 * @param piattoDetails -> oggetto con i nuovi dati
	 * @param request
	 * @param response
	 * 
	 * @return in caso di successo, il 'nuovo' oggetto piatto; in caso contrario
	 * errore
	 */
	@PutMapping("/{id}")
	public Object updatePiatto(@PathVariable Long id, Piatto piattoDetails, HttpServletResponse response,
			HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		if (!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare quest'operazione");
		}
		
		Optional<Piatto> piatto = piattoRepository.findById(id);
		if (!piatto.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Piatto non trovato");
		}
		
		Piatto p = piatto.get();
		p.setFoto(file.getBytes());
		p.setNomeFoto(file.getOriginalFilename());
		p.setNome(piattoDetails.getNome());
		p.setCosto(piattoDetails.getCosto());
		p.setDescrizione(piattoDetails.getDescrizione());
		p.setCosto(piattoDetails.getCosto());
		p.setCategoria(piattoDetails.getCategoria());

		return piattoRepository.save(p);
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
		
		if (!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare quest'operazione");
		}
		
		Optional<Piatto> p = piattoRepository.findById(id);
		if (!p.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Piatto non trovato");
		}
		piattoRepository.delete(p.get());
		return Collections.singletonMap("message", "Piatto cancellato con successo");
	}

	/**
	 * Metodo di utilità per estrarre il token di autenticazione dall'header
	 * "Authorization". Il token viene inviato nel formato "Bearer <token>".
	 *
	 * @param request Oggetto HttpServletRequest contenente gli header della
	 *                richiesta
	 * @return L'oggetto AuthUser associato al token, oppure null se il token non è
	 *         presente o non valido
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
