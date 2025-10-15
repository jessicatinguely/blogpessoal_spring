package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_tema") // CREATE TABLE tb_tema 
public class Tema {
	
	@Id // PRIMARY KEY (id)
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	private Long id;
	
	@Column(length = 100) // DEFINE O TAMANHO DA COLUNA
	@NotBlank(message = "O atributo descrição é obrigatório!") // NOT NULL)
	@Size(min = 5, max = 100, message = "O atributo descrição deve conter no mínimo 05 e no máximo 100 caracteres")
	private String descricao;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE) // UM TEMA PARA MUITAS POSTAGENS, CASCADE REMOVE PARA DELETAR AS POSTAGENS RELACIONADAS AO TEMA
	@JsonIgnoreProperties(value = "tema", allowSetters = true) 
	// IGNORA OS GETS MAS PERMITE OS SETS (POST, PUT), OU SEJA, NÃO RETORNA AS POSTAGENS MAS PERMITE QUE SEJA ATRIBUÍDA UM TEMA A UMA POSTAGEM
	private List<Postagem> postagem;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public List<Postagem> getPostagem() {
		return postagem;
	}
	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}
	


}
