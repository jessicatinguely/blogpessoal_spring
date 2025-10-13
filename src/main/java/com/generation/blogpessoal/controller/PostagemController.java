package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*") // libera o acesso para qualquer origem (ex: Authorization, Content-Type, etc)

public class PostagemController {
	
	@Autowired
	private PostagemRepository postagemRepository; // Spring Boot faz a injeção de dependência, ou seja, instancia o objeto automaticamente

	@GetMapping
	public ResponseEntity<List<Postagem>> getAll() { 
		// ResponseEntity = Retorna uma resposta HTTP (código de status, cabeçalhos e corpo)
		return ResponseEntity.ok(postagemRepository.findAll()); 
		// SELECT * FROM tb_postagens ,ou seja, traz todas as postagens
		
	}
}
