/*	CAMPI ENTITA' 'UTENTE RUOLO'
 * 	- id
 * 	- ruolo
 */
package com.projectWork.gestioneRistoranti.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class UtenteRuolo {
	
	// opzioni possibili per la enum
	enum Ruolo {
		RISTORATORE,
		USER
	}
	
	// identificativo
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	// campo ruolo
	@Column(nullable = false)
	private Ruolo ruolo;
	
	// relazione con 'utente'
	@OneToOne(mappedBy="ruolo")
	@JsonBackReference
	private Utente utente;
	
	// costruttore
	public UtenteRuolo() {}

	// getters e setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	@Override
	public String toString() {
		return ""+ruolo;
	}
	
}
