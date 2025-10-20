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

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*") // libera o acesso para qualquer origem (ex: Authorization,
													// Content-Type, etc)

public class TemaController {

	@Autowired
	private TemaRepository temaRepository; // Spring Boot faz a injeção de dependência, ou seja, instancia o objeto
											// automaticamente

	@GetMapping
	public ResponseEntity<List<Tema>> getAll() {
		// ResponseEntity = Retorna uma resposta HTTP (código de status, cabeçalhos e
		// corpo)
		return ResponseEntity.ok(temaRepository.findAll());
		// SELECT * FROM tb_temas ,ou seja, traz todos os temas

	}

	@GetMapping("/{id}")
	public ResponseEntity<Tema> getById(@PathVariable Long id) {
		return temaRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				// se encontrar o id, retorna o status OK
				.orElse(ResponseEntity.notFound().build());
		// se não encontrar o id, retorna o status 404 (NOT FOUND)

		// SELECT * FROM tb_postagens WHERE id = {id}
	}

	@GetMapping("/descricao/{descricao}")
	public ResponseEntity<List<Tema>> getAllByDescricao(@PathVariable String descricao) {
		return ResponseEntity.ok(temaRepository.findAllByDescricaoContainingIgnoreCase(descricao));

		// SELECT * FROM tb_postagens WHERE descricao LIKE "%{descricao}%"

	}

	@PostMapping
	public ResponseEntity<Tema> post(@Valid @RequestBody Tema tema) {
		tema.setId(null); // garante que o id seja nulo para criar um novo tema

		return ResponseEntity.status(HttpStatus.CREATED).body(temaRepository.save(tema)); // salva o tema e retorna o
																							// status 201 (CREATED)

	}

	@PutMapping
	public ResponseEntity<Tema> put(@Valid @RequestBody Tema tema) {

		return temaRepository.findById(tema.getId()) // verifica se o id existe no banco de dados
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(temaRepository.save(tema))) // se encontrar o
																										// id, atualiza
																										// o tema e
																										// retorna o
																										// status OK
				.orElse(ResponseEntity.notFound().build()); // se não encontrar o id, retorna o status 404 (NOT FOUND)

	}

	@ResponseStatus(HttpStatus.NO_CONTENT) // retorna o status 204 (NO CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {

		Optional<Tema> tema = temaRepository.findById(id);
		// verifica se o id existe no banco de dados e senão existir, retorna um
		// Optional vazio

		if (tema.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		// se não encontrar o id, retorna o status 404 (NOT FOUND)

		temaRepository.deleteById(id); // se encontrar o id, deleta o tema

	}
}