/*	CAMPI ENTITA' PIATTO
 * 	- id
 * 	- nome
 * 	- costo
 * 	- descrizione
 */
package com.projectWork.gestioneRistoranti.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Piatto {

	// identificativo
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// campo nome piatto
	@Column(nullable = false)
	private String nome;

	// campo costo (numero composto da 5 cifre totali, di cui 2 dopo la virgola)
	@Column(nullable = false, scale = 2)
	private Double costo;

	// decrizione piatto. Il campo può contenere max 200 caratteri
	@Column(length = 200)
	private String descrizione;

	// nome foto
	@Column(name = "nome_foto", nullable = true)
	private String nomeFoto;

	// annotation per la gestione dei large object (foto nel nostro caso)
	@Lob
	@Column(length = 100000000)
	private byte[] foto;

	@OneToMany(mappedBy = "piatto")
	@JsonManagedReference("ordinePiatto")
	private List<OrdineDettagli> ordinato;

	@ManyToOne
	@JoinColumn(name = "categoria_id")
	@JsonBackReference("categoriaPiatto")
	private Categoria categoria;

	// costruttore
	public Piatto() {
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

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<OrdineDettagli> getOrdinato() {
		return ordinato;
	}

	public void setOrdinato(List<OrdineDettagli> ordinato) {
		this.ordinato = ordinato;
	}

	public String getNomeFoto() {
		return nomeFoto;
	}

	public void setNomeFoto(String nomeFoto) {
		this.nomeFoto = nomeFoto;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}
