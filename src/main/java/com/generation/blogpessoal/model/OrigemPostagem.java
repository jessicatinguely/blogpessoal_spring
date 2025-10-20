package com.generation.blogpessoal.model;

public enum OrigemPostagem {
	C("Computador"), S("Smartphone"), T("Tablet");

	private String categoria;

	OrigemPostagem(String categoria) {
		this.categoria = categoria;
	}

	public String getCategoria() {
		return categoria;
	}
}
