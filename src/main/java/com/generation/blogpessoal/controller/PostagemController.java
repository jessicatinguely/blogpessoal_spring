package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

import jakarta.validation.Valid;

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
	
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id) {
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta)) 
				// se encontrar o id, retorna o status OK													
				.orElse(ResponseEntity.notFound().build());
		        // se não encontrar o id, retorna o status 404 (NOT FOUND)
		
		// SELECT * FROM tb_postagens WHERE id = {id}
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getAllByTitulo(@PathVariable String titulo) { 
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo)); 
		
		// SELECT * FROM tb_postagens WHERE titulo LIKE "%{titulo}%"

	}
	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {
		postagem.setId(null); // garante que o id seja nulo para criar uma nova postagem
		
		return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem)); // salva a postagem e retorna o status 201 (CREATED)
		
	}
	
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {
		
		return postagemRepository.findById(postagem.getId()) // verifica se o id existe no banco de dados
				.map(resposta -> ResponseEntity.status(HttpStatus.OK)
						.body(postagemRepository.save(postagem))) // se encontrar o id, atualiza a postagem e retorna o status OK
				.orElse(ResponseEntity.notFound().build()); // se não encontrar o id, retorna o status 404 (NOT FOUND)
	
	
}
	
@ResponseStatus(HttpStatus.NO_CONTENT) // retorna o status 204 (NO CONTENT)
@DeleteMapping("/{id}")
public void delete (@PathVariable Long id) {
    
    Optional<Postagem> postagem = postagemRepository.findById(id); 
    // verifica se o id existe no banco de dados e senão existir, retorna um Optional vazio
    
	if (postagem.isEmpty())
		throw new ResponseStatusException(HttpStatus.NOT_FOUND); 
	// se não encontrar o id, retorna o status 404 (NOT FOUND)
	
	postagemRepository.deleteById(id); // se encontrar o id, deleta a postagem
																	
	}
}