/*	CAMPI ENTITA' ORDINE
 * 	- id
 * 	- numero
 * 	- data
 * 	- status
*/
package com.projectWork.gestioneRistoranti.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

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
	
	// numero ordine che andremo a generare; univoco
	@Column(nullable=false, name="numero_ordine", unique=true)
	@Length(min=10, max=10)
	private Long numeroOrdine;
	
	@Column(nullable=false)
	private Status status;
	
	// data ordine che andremo a gestire col metodo now()
	@Column(nullable=false, name="data_ordine")
	private LocalDate dataOrdine;
	
	// campo totale (numero composto da 8 cifre, di cui 2 dopo la virgola)
	@Column(nullable=false, precision = 8, scale = 2)
	private Double totale;
	
	/* seconda parte relazione utente-ordine
	 * in questo caso abbiamo solo un oggetto Utente (a un ordine è collegato
	 * un solo utente)*/
	@ManyToOne
	@JoinColumn(name="utente_id")
	@JsonBackReference
	private Utente utente;
	
	// relazione uno-a-molti tra 'ordine' e 'ordine_dettagli'
	@OneToMany(mappedBy="ordine")
	@JsonManagedReference
	private List<OrdineDettagli> dettagli;
	
	// costruttore
	public Ordine () {}
	
	// getters e setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumeroOrdine() {
		return numeroOrdine;
	}

	public void setNumeroOrdine(Long numeroOrdine) {
		this.numeroOrdine = numeroOrdine;
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

	public Double getPrezzo() {
		return totale;
	}

	public void setPrezzo(Double prezzo) {
		this.totale = prezzo;
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

}
