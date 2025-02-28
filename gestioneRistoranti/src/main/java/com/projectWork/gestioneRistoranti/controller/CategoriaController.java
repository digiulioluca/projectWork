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
import com.projectWork.gestioneRistoranti.model.Categoria;
import com.projectWork.gestioneRistoranti.model.Menu;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.repository.CategoriaRepository;
import com.projectWork.gestioneRistoranti.repository.MenuRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/categoria")
@CrossOrigin(origins = {}) // Disabilita richieste CORS da origini esterne
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private MenuRepository menuRepository;

	/*
	 * Metodo per creare una Categoria (simile al createMenu presente nel MenuController)
	 * 
	 * @param 	oggetto di tipo Categoria da aggiungere
	 * @param	id del menù di riferimento
	 * @param	response
	 * @param	request
	 * 
	 * @return	messaggio con esito
	 */
	@PostMapping("/{id}/nuovo")
	public Object createCategoria(@RequestBody Categoria nuovaCategoria, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		//authentication
		Utente authUtente = getAuthenticatedUtente(request);
		if(authUtente==null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		if(!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare quest'operazione");
		}
		
		Optional<Menu> findMenu = menuRepository.findById(id);
		if(!findMenu.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Menù non trovato");
		}
		
		nuovaCategoria.setMenu(findMenu.get());
		categoriaRepository.save(nuovaCategoria);
		return Collections.singletonMap("message", "Categoria aggiunto con successo!");
	}

	/*
	 * Metodo per restituire i dati di una singola categoria
	 * 
	 * @param id per ricercare la categoria una volta ottenute le sue info dal token
	 * @param request Oggetto per leggere l'header "Authorization"
	 * @param response Oggetto per impostare lo status in caso di errore
	 * 
	 * @return L'oggetto Categoria, se i controlli precedenti vengono passati
	 * (false)
	 */
	@GetMapping("/{id}")
	public Object getCategoriaById(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
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

	/* Metodo che restituisce la lista completa delle categorie (per ora non necessario).
	 *	
	 * @return	Lista con tutte le categorie
	 */
	@GetMapping
	public List<Categoria> getAllCategoria() {
		return categoriaRepository.findAll();
	}

	/*
	 * Metodo per aggiornare i dati della categoria
	 * 
	 * @param id -> id della categoria di cui andremo a modificare i dati
	 * @param categoriaDetails -> oggetto con i nuovi dati
	 * @param request
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
		
		if(!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare quest'operazione");
		}
		
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		if (!categoria.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Categoria non trovata");
		}
		
		Categoria updatedCategoria = categoria.get();
		updatedCategoria.setNome(categoriaDetails.getNome());

		return categoriaRepository.save(updatedCategoria);

	}

    
	/*
	 * Metodo per cancellare una categoria
	 * 
	 * @param	id della categoria da cancellare
	 * @param	request
	 * @param	response
	 * 
	 * @return	messaggio di conferma in caso di successo
	 */
	@DeleteMapping("/{id}")
	public Object deleteCategoria(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		if(!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare quest'operazione");
		}
		
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		if (!categoria.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Menu non trovato");
		}
		categoriaRepository.delete(categoria.get());
		return Collections.singletonMap("message", "Menu cancellato con successo");
	}
	
	/* Metodo che ritorna la lista di tutte le categorie di un singolo menù
	 * 
	 * @param	id del menù
	 * @response
	 * 
	 * @return	La lista di categorie (o messaggio di errore)
	*/
	@GetMapping("/{id}/piatti")
	public Object getAllPiattiByCategoria (@PathVariable Long id, HttpServletResponse response) {
		Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
		if(!categoriaOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Menù non trovato");
		}
		
		Categoria c = categoriaOpt.get();
		return c.getPiatti();
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
