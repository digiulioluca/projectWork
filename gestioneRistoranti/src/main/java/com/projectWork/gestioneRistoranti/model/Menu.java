/* CAMPI DA INSERIRE PER L'ENTITà MENU:
 * - id
 * - nome
 */
package com.projectWork.gestioneRistoranti.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
public class Menu {
	
	//identificativo
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//annotation per impostare NOT NULL il campo
	@Column(nullable = false)
	private String nome;
	
	//relazioni molti a uno con tabella Ristorante
	@ManyToOne
	@JoinColumn(name="ristorante_id")
	@JsonBackReference
	private Ristorante ristorante;
	
	// relazione uno a molti con tabella Categoria 
	@OneToMany(mappedBy="menu")
	@JsonManagedReference
	private List<Categoria> categorie;
	
	//costruttori
	public Menu() {}
	
	public Menu(Long id,String nome) {
		this.id=id;
		this.nome=nome;
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

	public Ristorante getRistorante() {
		return ristorante;
	}

	public void setRistorante(Ristorante ristorante) {
		this.ristorante = ristorante;
	}

	public List<Categoria> getCategorie() {
		return categorie;
	}

	public void setCategorie(List<Categoria> categorie) {
		this.categorie = categorie;
	}
}
