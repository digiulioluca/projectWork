/*	CAMPI ENTITA' 'ORDINE DETTAGLI'
 * 	- id
 * 	- prezzo
 * 	- quantità
 */
package com.projectWork.gestioneRistoranti.model;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="ordine_dettagli")
public class OrdineDettagli {
	
	// identificativo
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// prezzo del singolo prodotto (da moltiplicare per la quantità)
	@Column(nullable = false, precision = 6, scale = 2)
	private Double prezzo;
	
	// quantità singolo prodotto
	@Column(nullable = false)
	@Length(max = 50)
	private int quantita;
	
	// seconda parte relazione 'ordine' e 'ordine_dettagli"
	@ManyToOne
	@JoinColumn(name="ordine_id")
	@JsonBackReference
	private Ordine ordine;
	
	// seconda parte relazione 'piatto' e 'ordine_dettagli"
	@ManyToOne
	@JoinColumn(name="piatto_id")
	@JsonManagedReference
	private Piatto piatto;
	
	// costruttore
	public OrdineDettagli () {}

	// getters e setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Double prezzo) {
		this.prezzo = prezzo;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public Ordine getOrdine() {
		return ordine;
	}

	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
	}

	public Piatto getPiatto() {
		return piatto;
	}

	public void setPiatto(Piatto piatto) {
		this.piatto = piatto;
	}
	
}
