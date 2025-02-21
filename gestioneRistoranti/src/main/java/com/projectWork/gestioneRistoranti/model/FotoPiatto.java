/*	CAMPI ENTITA' 'FOTO PIATTO'
 * 	- id
 * 	- nome foto
 * 	- foto
 */
package com.projectWork.gestioneRistoranti.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="foto_piatto")
public class FotoPiatto {
	
	// identificativo
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// campo nome foto
	@Column(nullable = false)
	private String nome;
	
	// annotation per la gestione dei large object (foto nel nostro caso)
	@Lob
	@Column(length=10000000)
	private byte[] foto;
	
	// relazione con tabella 'piatto'
	@ManyToOne
	@JoinColumn(name="piatto_id")
	@JsonBackReference
	private Piatto piatto;
	
	// costruttore
	public FotoPiatto() {}

	// getters e setters
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

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public Piatto getPiatto() {
		return piatto;
	}

	public void setPiatto(Piatto piatto) {
		this.piatto = piatto;
	}
	
}
