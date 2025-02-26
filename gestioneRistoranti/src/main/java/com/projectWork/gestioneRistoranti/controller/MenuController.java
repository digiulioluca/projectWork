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
import com.projectWork.gestioneRistoranti.model.Menu;
import com.projectWork.gestioneRistoranti.model.Ristorante;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.repository.MenuRepository;
import com.projectWork.gestioneRistoranti.repository.RistoranteRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = {})
public class MenuController {

	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private RistoranteRepository ristoranteRepository;

	// metodo che restituisce la lista completa dei menu.
	@GetMapping
	public List<Menu> getAllMenu() {
		return menuRepository.findAll();
	}

	/*
	 * Metodo per creare un menu
	 * 
	 * @param 	oggetto di tipo Menu da aggiungere
	 * @param	id del ristorante che conterrà il nuovo menù
	 * @param	response
	 * 
	 * @return -> messaggio, in caso di successo
	 */
	@PostMapping("/{id}/nuovo")
	public Object createMenu(@RequestBody Menu nuovoMenu, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		// authentication
		Utente authUtente = getAuthenticatedUtente(request);
		if(authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Effettua il login per compiere quest'operazione");
		}
		
		if(!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare quest'operazione");
		}
		
		//tramite l'id fornito nell'URL cerchiamo il ristorante tramite un optional
		Optional<Ristorante> findRes = ristoranteRepository.findById(id);
		if(!findRes.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message","Ristorante non trovato");
		}
		
		
		// nel caso in cui non dovessimo riscontrare errori, aggiorniamo il valore di setRistorante
		nuovoMenu.setRistorante(findRes.get());
		// salviamo il nuovo menù
		menuRepository.save(nuovoMenu);
		return Collections.singletonMap("message", "Menu aggiunto con successo!");
	}

	/*
	 * Metodo per restituire i dati di un singolo menu
	 * 
	 * @param id per ricercare il menu una volta ottenute le sue info dal token
	 * @param response Oggetto per impostare lo status in caso di errore
	 * 
	 * @return L'oggetto Utente, se i controlli precedenti vengono passati (false)
	 */
	@GetMapping("/{id}")
	public Object getMenu(@PathVariable Long id, HttpServletResponse response) {
		// attraverso l'id cerchiamo il menu
		Optional<Menu> menu = menuRepository.findById(id);
		if (!menu.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Menu non trovato");
		}

		// con il metodo get ritorniamo l'oggetto Menu
		return menu.get();
	}
	
	/* Metodo che ritorna la lista di tutte le categorie di un singolo menù
	 * 
	 * @param	id del menù
	 * @response
	 * 
	 * @return	La lista di categorie (o messaggio di errore)
	*/
	@GetMapping("/{id}/categorie")
	public Object getAllCategorieByMenu (@PathVariable Long id, HttpServletResponse response) {
		Optional<Menu> menuOpt = menuRepository.findById(id);
		if(!menuOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Menù non trovato");
		}
		
		Menu m = menuOpt.get();
		return m.getCategorie();
	}

	/*
	 * Metodo per aggiornare i dati del menu
	 * 
	 * @param id -> id del menu di cui andremo a modificare i dati
	 * @param menuDetails -> oggetto con i nuovi dati
	 * @param request
	 * @param response
	 * 
	 * @return in caso di successo, il 'nuovo' oggetto menu; in caso contrario errore
	 */
	@PutMapping("/{id}")
	public Object updateMenu(@PathVariable Long id, @RequestBody Menu menuDetails, HttpServletRequest request,
			HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		if(!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare quest'operazione");
		}
		
		Optional<Menu> menu = menuRepository.findById(id);
		if (menu.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Menu non trovato");
		}
		
		Menu updatedMenu = menu.get();
		updatedMenu.setNome(menuDetails.getNome());
		menuRepository.save(updatedMenu);
		return Collections.singletonMap("message", "Menu aggiornato con successo");

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
	 * Metodo per cancellare un menu
	 * 
	 * @param	id del menù da cancellare
	 * @param	request
	 * @param	response
	 * 
	 * @return	messaggio di conferma in caso di successo
	 */
	@DeleteMapping("/{id}")
	public Object deleteMenu(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		if(!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare quest'operazione");
		}
		
		Optional<Menu> menu = menuRepository.findById(id);
		if (!menu.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Menu non trovato");
		}
		menuRepository.delete(menu.get());
		return Collections.singletonMap("message", "Menu cancellato con successo");
	}
	
	
}
