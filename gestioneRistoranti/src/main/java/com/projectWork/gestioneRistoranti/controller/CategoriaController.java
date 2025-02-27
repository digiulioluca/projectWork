package com.projectWork.gestioneRistoranti.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.projectWork.gestioneRistoranti.model.Categoria;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.repository.CategoriaRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;

	/*
	 * Metodo per creare una Categoria
	 * 
	 * @param oggetto di tipo Categoria da aggiungere
	 * 
	 * @return -> messaggio, in caso di successo
	 */
	@PostMapping
	public Object createCategoria(@RequestBody Categoria nuovaCategoria) {
		categoriaRepository.save(nuovaCategoria);
		return Collections.singletonMap("message", "Categoria aggiunto con successo!");
	}

	/*
	 * Metodo per restituire i dati di una singola categoria
	 * 
	 * @param id per ricercare la categoria una volta ottenute le sue info dal token
	 * 
	 * @param request Oggetto per leggere l'header "Authorization"
	 * 
	 * @param response Oggetto per impostare lo status in caso di errore
	 * 
	 * @return L'oggetto Categoria, se i controlli precedenti vengono passati
	 * (false)
	 */
	@GetMapping("/{id}")
	public Object getCategoria(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}

		// attraverso l'id cerchiamo la categoria
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		if (!categoria.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Categoria non trovata");
		}

		// con il metodo get ritorniamo l'oggetto Categoria
		return categoria.get();
	}

	// metodo che restituisce la lista completa dei menu.
	@GetMapping
	public List<Categoria> getAllCategoria() {
		return categoriaRepository.findAll();
	}

	/*
	 * Metodo per aggiornare i dati della categoria
	 * 
	 * @param id -> id della categoria di cui andremo a modificare i dati
	 * 
	 * @param categoriaDetails -> oggetto con i nuovi dati
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return in caso di successo, il 'nuovo' oggetto categoria; in caso contrario
	 * errore
	 */
	@PutMapping("/{id}")
	public Object updateCategoria(@PathVariable Long id, @RequestBody Categoria categoriaDetails,
			HttpServletRequest request, HttpServletResponse response) {

		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		if (categoria.isPresent() && !"RISTORATORE".equals(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Categoria non trovata");
		}
		Categoria updatedCategoria = categoria.get();
		updatedCategoria.setNome(categoriaDetails.getNome());

		return categoriaRepository.save(updatedCategoria);

	}

	// metodo per prendere autenticazione dell'utente
	private Utente getAuthenticatedUtente(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Metodo per cancellare una categoria
	 */
	@DeleteMapping("/{id}")
	public Object deleteCategoria(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		if (categoria.isPresent() && !"RISTORATORE".equals(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Categoria non trovata");
		}
		categoriaRepository.delete(categoria.get());
		return Collections.singletonMap("message", "Categoria cancellata con successo");
	}
}
