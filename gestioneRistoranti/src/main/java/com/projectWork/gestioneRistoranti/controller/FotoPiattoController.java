package com.projectWork.gestioneRistoranti.controller;

import com.projectWork.gestioneRistoranti.auth.TokenService;
import com.projectWork.gestioneRistoranti.model.FotoPiatto;
import com.projectWork.gestioneRistoranti.model.Utente;
import com.projectWork.gestioneRistoranti.model.Piatto;
import com.projectWork.gestioneRistoranti.repository.FotoPiattoRepository;
import com.projectWork.gestioneRistoranti.repository.PiattoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/fotoPiatti")
@CrossOrigin(origins = {})
public class FotoPiattoController {
	
	@Autowired
	private FotoPiattoRepository fotoPiattoRepository;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private PiattoRepository piattoRepository;
	
	/* Metodo per aggiungere la foto di un piatto
	 * 
	 * @param	multipart file da acquisire (form-data)
	 * @param	id del piatto
	 * @param	request
	 * @param	response
	 * 
	 * @return	messaggio (in entrambi i casi)
	 */
	@PostMapping("/{id}/nuovo")
	public Object addFotoPiatto(@RequestParam("file") MultipartFile file, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Utente authUtente =getAuthenticatedUtente(request);
		if(authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		if(!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare questa operazione");
		}
		
		Optional<Piatto> piattoOpt = piattoRepository.findById(id);
		if (!piattoOpt.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message","Piatto non trovato");
		}
		
		Piatto p = piattoOpt.get();
		FotoPiatto fp = new FotoPiatto();
		fp.setPiatto(p);
		fp.setFoto(file.getBytes());
		fp.setNome(file.getOriginalFilename());
		fotoPiattoRepository.save(fp);
		return Collections.singletonMap("message", "foto piatto "+p.getNome()+" aggiunta con successo");
 	}
	
	/*
	 * Metodo per cancellare la foto di un piatto
	 */
	@DeleteMapping("/{id}")
	public Object deleteFotoPiatto(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Utente authUtente = getAuthenticatedUtente(request);
		if (authUtente == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non autorizzato");
		}
		
		if (!"ristoratore".equalsIgnoreCase(authUtente.getRuolo().toString())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return Collections.singletonMap("message", "Non hai i permessi per effettuare quest'operazione");
		}
		
		Optional<FotoPiatto> fp = fotoPiattoRepository.findById(id);
		if (!fp.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.singletonMap("message", "Foto piatto non trovata");
		}
		fotoPiattoRepository.delete(fp.get());
		return Collections.singletonMap("message", "Foto piatto cancellata con successo");
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
