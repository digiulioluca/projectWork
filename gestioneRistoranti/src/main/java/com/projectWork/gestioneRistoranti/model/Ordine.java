/*	CAMPI ENTITA' ORDINE
 * 	- id
 * 	- numero
 * 	- data
 * 	- status
*/
package com.projectWork.gestioneRistoranti.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Ordine {
	
	// enum per la gestione dei valori dello status
	enum Status{
		IN_ATTESA,
		IN_CORSO,
		COMPLETATO
	}
	
	// identificativo
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	// campo per definire
	@Column(nullable=false)
	private Status status;
	
	// data ordine che andremo a gestire col metodo now()
	@Column(nullable=false, name="data_ordine")
	private LocalDate dataOrdine;
	
	// campo totale (numero composto da 8 cifre, di cui 2 dopo la virgola)
	@Column(nullable=false, scale = 2)
	private Double totale;
	
	/* seconda parte relazione utente-ordine
	 * in questo caso abbiamo solo un oggetto Utente (a un ordine è collegato
	 * un solo utente)*/
	@ManyToOne
	@JoinColumn(name="utente_id")
	@JsonBackReference("ordineUtente")
	private Utente utente;
	
	// relazione uno-a-molti tra 'ordine' e 'ordine_dettagli'
	@OneToMany(mappedBy="ordine")
	@JsonManagedReference("dettagliOrdine")
	private List<OrdineDettagli> dettagli;
	
	@ManyToOne
	@JoinColumn(name="ristorante_id")
	@JsonBackReference("ristoranteOrdine")
	private Ristorante ristorante;
	
	// costruttore
	public Ordine () {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDate getDataOrdine() {
		return dataOrdine;
	}

	public void setDataOrdine(LocalDate dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	public Double getTotale() {
		return totale;
	}

	public void setTotale(Double totale) {
		this.totale = totale;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public List<OrdineDettagli> getDettagli() {
		return dettagli;
	}

	public void setDettagli(List<OrdineDettagli> dettagli) {
		this.dettagli = dettagli;
	}

	public Ristorante getRistorante() {
		return ristorante;
	}

	public void setRistorante(Ristorante ristorante) {
		this.ristorante = ristorante;
	}

	
	
}
