/*  CAMPI DA INSERIRE PER L'ENTITà CATEGORIA :
 * - id
 * - nome
 */
package com.projectWork.gestioneRistoranti.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
public class Categoria {
	
	//identificativo
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//annotaion per impostare NOT NULL il campo
	@Column(nullable = false)
	private String nome;
	
	//relazione nolti a uno con tabella Menu.
	@ManyToOne
	@JoinColumn(name="menu_id")
	@JsonBackReference
	private Menu menu;
	
	//relazione uno a molti con tabella Piatto.
	@OneToMany(mappedBy="categoria")
	@JsonManagedReference
	private List<Piatto> piatti;
	
	//costruttori
	public Categoria() {}
	
	public Categoria(Long id,String nome) {
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

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public List<Piatto> getPiatti() {
		return piatti;
	}

	public void setPiatti(List<Piatto> piatti) {
		this.piatti = piatti;
	}
}
