package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;
import com.generation.blogpessoal.util.JwtHelper;
import com.generation.blogpessoal.util.TestBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Indica que a classe é um teste de integração Spring Boot
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Define o ciclo de vida da instância do teste
@TestMethodOrder(MethodOrderer.DisplayName.class) // Ordena os métodos de teste pelo nome exibido

public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private static final String BASE_URL = "/usuarios";
	private static final String USUARIO = "root@root.com";
	private static final String SENHA = "rootroot";
	
	@BeforeAll
	void inicio(){
		usuarioRepository.deleteAll();
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Root", USUARIO, SENHA));
	}
	
	@Test
	@DisplayName("01 - Deve Cadastrar um novo usuário com sucesso")
	void deveCadastrarUsuario() {
		// Given: Aqui definimos o estado inicial ou pré-condições para o teste
		Usuario usuario = TestBuilder.criarUsuario(null, "João da Silva", "joao@email.com.br", "12345678");
		
		
		// When: Aqui executamos a ação que queremos testar
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL + "/cadastrar", HttpMethod.POST,requisicao, Usuario.class);
		
		
		// Then: Aqui verificamos os resultados esperados
         assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
         assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("02 - Não Deve Cadastrar usuário duplicado")
	void naoDeveCadastrarUsuarioDuplicado() {
		// Given: Aqui definimos o estado inicial ou pré-condições para o teste
		Usuario usuario = TestBuilder.criarUsuario(null, "Rafaela Lemes", "rafaela@email.com.br", "12345678");
		usuarioService.cadastrarUsuario(usuario);
		
		// When: Aqui executamos a ação que queremos testar
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL + "/cadastrar", HttpMethod.POST,requisicao, Usuario.class);
		
		
		// Then: Aqui verificamos os resultados esperados
         assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
         assertNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("03 - Deve Atualizar os dados do usuário com sucesso")
	void DeveAtualizarUmUsuario() {
		// Given: Aqui definimos o estado inicial ou pré-condições para o teste
		Usuario usuario = TestBuilder.criarUsuario(null, "Nadia", "nadia@email.com.br", "12345678");
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(usuario);
		
		Usuario usuarioUpdate = TestBuilder.criarUsuario(usuarioCadastrado.get().getId() ,"Nadia Caricatto", "nadia@email.com.br", "abc12345");
		
		// When: Aqui executamos a ação que queremos testar
		String token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);
		
		// Criar a requisição com o token JWT no cabeçalho
		HttpEntity<Usuario> requisicao = JwtHelper.criarRequisicaoComToken(usuarioUpdate, token);
		
		// Enviar a requisição para o endpoint de atualização PUT
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL + "/atualizar", HttpMethod.PUT,requisicao, Usuario.class);
		
		
		// Then: Aqui verificamos os resultados esperados
         assertEquals(HttpStatus.OK, resposta.getStatusCode());
         assertNotNull(resposta.getBody());
	
}
	
	@Test
	@DisplayName("04 - Deve Listar todos os usuários com sucesso")
	void DeveListarTodosUsuarios() {
		// Given: Aqui definimos o estado inicial ou pré-condições para o teste
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Ana Marques", "ana@email.com.br", "12345678"));
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Carlos Moura", "carlos@email.com.br", "12345678"));
				
		// When: Aqui executamos a ação que queremos testar
		String token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);
		
		// Criar a requisição com o token JWT no cabeçalho
		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
		
		// Enviar a requisição para o endpoint de atualização PUT
		ResponseEntity<Usuario[]> resposta = testRestTemplate.exchange(
				BASE_URL + "/all", HttpMethod.GET,requisicao, Usuario[].class);
		
		
		// Then: Aqui verificamos os resultados esperados
         assertEquals(HttpStatus.OK, resposta.getStatusCode());
         assertNotNull(resposta.getBody());

	}
	
	@Test
	@DisplayName("05 - Deve Listar os Usuarios por Id com sucesso")
	void DeveListarUsuarioPorId() {
		// Given: Aqui definimos o estado inicial ou pré-condições para o teste
		Usuario usuario = TestBuilder.criarUsuario(null, "Maria João", "maria@email.com.br", "12345678");
	    Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(usuario);

				
		// When: Aqui executamos a ação que queremos testar
		String token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);
		
		// Criar a requisição com o token JWT no cabeçalho
		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
		
		// Enviar a requisição para o endpoint de atualização PUT
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL + "/" + usuarioCadastrado.get().getId(), HttpMethod.GET,requisicao, Usuario.class);
		
		
		// Then: Aqui verificamos os resultados esperados
         assertEquals(HttpStatus.OK, resposta.getStatusCode());
         assertNotNull(resposta.getBody());

	}
	}
