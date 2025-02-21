/*	CAMPI DA INSERIRE PER L'ENTITA' UTENTE:
 * - id
 * - nome
 * - cognome
 * - email		
 * - password
 * - numero_carta
 * - role
 * - token
 */
package com.projectWork.gestioneRistoranti.model;

// import di tutte le librerie necessarie (annotation e liste)
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Utente {
	
	// identificativo
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// annotation per impostare NOT NULL il campo
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String cognome;
		
	/* oltre al not null, con 'unique' evitiamo che nel DB vadano a finire due mail uguali
	Email sarà uno dei due campi NECESSARI per il login*/
	@Column(nullable = false, unique = true)
	private String email;
	
	/* attraverso @Size andiamo a impostare un minimo di 6 caratteri
	per la password*/
	@Column(nullable = false)
	@Size(min = 6)
	private String password;
	
	@Column(name="numero_carta", nullable = false)
	@Size(min=16, max=16)
	private Long numeroCarta;
	
	/* relazione uno a molti con tabella ordini. Attraverso
	 * la seconda annotation evitiamo la ricorsività da entrambi i lati.
	 * In questo caso (managed) andremo a visualizzare solo lato utente gli ordini effettuati*/
	@OneToMany(mappedBy="utente")
	@JsonManagedReference
	private List<Ordine> ordini;
	
	// attributo 'token' per il logi-in
	private String token;
	
	// nome foto
	@Column(nullable = false, name = "nome_foto")
	private String nomeFoto;
		
	// annotation per la gestione dei large object (foto nel nostro caso)
	@Lob
	@Column(length=1000000)
	private byte[] foto;
	
	@OneToOne
	@JoinColumn(name="ruolo_id", nullable = false)
	@JsonManagedReference
	private UtenteRuolo ruolo;
	
	// costruttori
	public Utente() {}

	public Utente(Long id, String nome, String cognome, String email, String password,
			Long numeroCarta, String token, String nomeFoto, byte[] foto) {
		this.id=id;
		this.nome=nome;
		this.cognome=cognome;
		this.email=email;
		this.password=password;
		this.numeroCarta=numeroCarta;
		this.token=token;
		this.nomeFoto=nomeFoto;
		this.foto=foto;
	}
	
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

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getNumeroCarta() {
		return numeroCarta;
	}

	public void setNumeroCarta(Long numeroCarta) {
		this.numeroCarta = numeroCarta;
	}

	public List<Ordine> getOrdini() {
		return ordini;
	}

	public void setOrdini(List<Ordine> ordini) {
		this.ordini = ordini;
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
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
