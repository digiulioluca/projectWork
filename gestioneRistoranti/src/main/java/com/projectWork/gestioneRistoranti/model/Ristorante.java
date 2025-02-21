/*  CAMPI DA INSERIRE PER L'ENTITà RISTORANTE:
 *  - id
 *  - nome
 *  - indirizzo
 *  - numero telefono
 *  - partita Iva
 *  - città
 *  - provincia
 *  - email
 */ 
 
package com.projectWork.gestioneRistoranti.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Ristorante {
	
	enum Tipo{
		TRATTORIA,
		RISTORANTE_PIZZERIA,
		STELLATO
	}
	
	//identificativo
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	//annotaion per impostare NOT NULL il campo
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false, unique = true)
	private String indirizzo;
	
	@Column(nullable = false, unique = true)
	@Size(min = 10, max=10)
	private int numeroTelefono;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String partitaIva;
	
	@Column(nullable = false)
	private String citta;
	
	@Column(nullable = false)
	private String provincia;
	
	@Column(length=200, nullable = false)
	private String descrizione;
	
	@Column(nullable = false)
	private Tipo tipo;
	
	//relazione molti a uno con tabella Utente
	@ManyToOne
	@JoinColumn(name="utente_id")
	@JsonBackReference
	private Utente utente;
	
	//relazione uno a molti con tabella Menu
	@OneToMany
	@JsonManagedReference
	private List<Menu> menu;
	
	//relazione uno a molti con tabella Ordine
	@OneToMany
	@JsonManagedReference
	private List<Ordine> ordini;
	
	//costruttori
	public Ristorante() {}
	
	public Ristorante(Long id, String nome, String indirizzo, String email,
			int numeroTelefono, String partitaIva, String citta, String provincia, Tipo tipo,
			String descrizione) {
		this.id=id;
		this.nome=nome;
		this.indirizzo=indirizzo;
		this.email=email;
		this.numeroTelefono=numeroTelefono;
		this.partitaIva=partitaIva;
		this.citta=citta;
		this.provincia=provincia;
		this.tipo=tipo;
		this.descrizione=descrizione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public int getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(int numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public List<Menu> getMenu() {
		return menu;
	}

	public void setMenu(List<Menu> menu) {
		this.menu = menu;
	}

	public List<Ordine> getOrdini() {
		return ordini;
	}

	public void setOrdini(List<Ordine> ordini) {
		this.ordini = ordini;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	
}
