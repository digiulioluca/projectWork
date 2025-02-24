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

import com.projectWork.gestioneRistoranti.model.Menu;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.repository.MenuRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/menu")
@CrossOrigin(origins = {})
public class MenuController {

	@Autowired
	private MenuRepository menuRepository;

	// metodo che restituisce la lista completa dei menu.
	@GetMapping()
	public List<Menu> getAllMenu() {

		return menuRepository.findAll();
	}

	/*
	 * Metodo per creare un menu
	 * 
	 * @param oggetto di tipo Menu da aggiungere
	 * 
	 * @return -> messaggio, in caso di successo
	 */
	@PostMapping
	public Object createMenu(Menu nuovoMenu) {
		menuRepository.save(nuovoMenu);
		return Collections.singletonMap("message", "Menu aggiunto con successo!");
	}

	/*
	 * Metodo per restituire i dati di un singolo menu
	 * 
	 * @param id per ricercare il menu una volta ottenute le sue info dal token
	 * 
	 * @param request Oggetto per leggere l'header "Authorization"
	 * 
	 * @param response Oggetto per impostare lo status in caso di errore
	 * 
	 * @return L'oggetto Utente, se i controlli precedenti vengono passati (false)
	 */
	@GetMapping("/{id}")
	public Object getMenu(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}

		// attraverso l'id cerchiamo il menu
		Optional<Menu> menu = menuRepository.findById(id);
		if (!menu.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Menu non trovato");
		}

		// con il metodo get ritorniamo l'oggetto Menu
		return menu.get();
	}

	/*
	 * Metodo per aggiornare i dati del menu
	 * 
	 * @param id -> id del menu di cui andremo a modificare i dati
	 * 
	 * @param menuDetails -> oggetto con i nuovi dati
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return in caso di successo, il 'nuovo' oggetto menu; in caso contrario
	 * errore
	 */
	@PutMapping("/{id}")
	public Object updateMenu(@PathVariable Long id, @RequestBody Menu menuDetails, HttpServletRequest request,
			HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		Optional<Menu> menu = menuRepository.findById(id);
		if (menu.isPresent() && !"RISTORATORE".equals(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Menu non trovato");
		}
		Menu updatedMenu = menu.get();
		updatedMenu.setNome(menuDetails.getNome());
		menuRepository.save(updatedMenu);
		return menuRepository.save(updatedMenu);

	}

	// metodo per prendere autenticazione dell'utente
	private Utente getAuthenticatedUtente(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Metodo per cancellare un menu
	 */
	@DeleteMapping("/{id}")
	public Object deleteMenu(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		Optional<Menu> menu = menuRepository.findById(id);
		if (menu.isPresent() && !"RISTORATORE".equals(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Menu non trovato");
		}
		menuRepository.delete(menu.get());
		return Collections.singletonMap("message", "Menu cancellato con successo");
	}
}
