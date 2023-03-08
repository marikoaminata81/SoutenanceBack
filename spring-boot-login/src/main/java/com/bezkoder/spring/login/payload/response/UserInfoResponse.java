package com.bezkoder.spring.login.payload.response;

import java.util.List;

public class UserInfoResponse {
	private Long id;
	private String username;
	private String numero;
	private String nom;
	private String prenom;
	private String password;

	private List<String> roles;

	public UserInfoResponse(Long id, String username, String numero, String nom, String prenom, String password, List<String> roles) {
		this.id = id;
		this.username = username;
		this.nom = nom;
		this.prenom = prenom;
		this.numero = numero;
		this.password=password;
		this.roles = roles;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public  String getNom(){
		return  nom;
	}
  public  String getPrenom(){
		return  prenom;
  }
	public  String getPassword(){
		return  password;
	}
	public  String setPassword(){
		return  password;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}



	public List<String> getRoles() {
		return roles;
	}
}
