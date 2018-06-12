package br.com.divino.model;

import java.io.Serializable;

public class Loja implements Serializable {
	
	private Long id;
	private String nome;
	private String foto;
	
	public Loja() {
		// TODO Auto-generated constructor stub
	}
	
	public Loja(Long id, String nome, String foto) {
		this.id = id;
		this.nome = nome;
		this.foto = foto;
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
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	
}
