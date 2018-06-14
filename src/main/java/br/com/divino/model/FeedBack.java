package br.com.divino.model;

import java.io.Serializable;

public class FeedBack implements Serializable {
	
	private Long id;
	private Item item;
	private Integer design;
	private Integer durabilidade;
	private Integer material;
	private Integer preco;
	private String observacao;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Integer getDesign() {
		return design;
	}
	public void setDesign(Integer design) {
		this.design = design;
	}
	public Integer getDurabilidade() {
		return durabilidade;
	}
	public void setDurabilidade(Integer durabilidade) {
		this.durabilidade = durabilidade;
	}
	public Integer getMaterial() {
		return material;
	}
	public void setMaterial(Integer material) {
		this.material = material;
	}
	public Integer getPreco() {
		return preco;
	}
	public void setPreco(Integer preco) {
		this.preco = preco;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	
}
