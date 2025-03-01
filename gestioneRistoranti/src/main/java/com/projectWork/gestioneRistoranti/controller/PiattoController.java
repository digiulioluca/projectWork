package com.projectWork.gestioneRistoranti.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
@CrossOrigin(origins = {})
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

	@PostMapping("/{idCategoria}")
	public Object createPiatto(@PathVariable("idCategoria") Long id, @RequestParam("foto_piatto") MultipartFile fotoPiatto, 
			HttpServletRequest request, HttpServletResponse response, Piatto piatto) throws IOException{
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		if(!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Collections.singletonMap("message", "Accesso negato");
		}
		
		Optional<Categoria> findCategoria = categoriaRepository.findById(id);
		if(!findCategoria.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "categoria non trovata");
		}
		
		piatto.setCategoria(findCategoria.get());
		piatto.setPhoto(fotoPiatto.getBytes());
		piatto.setPhotoName(fotoPiatto.getOriginalFilename());
		piattoRepository.save(piatto);
		return Collections.singletonMap("message", "Piatto aggiunto con successo");
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
