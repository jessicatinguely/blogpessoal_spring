package com.generation.blogpessoal.util;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.UsuarioLogin;

public class JwtHelper {

    // Construtor privado (para evitar instanciar a classe)
    private JwtHelper() {
    }

    // Método para obter o token JWT
    public static String obterToken(TestRestTemplate testRestTemplate, String usuario, String senha) {

        // Cria o objeto com usuário e senha
        UsuarioLogin usuarioLogin = TestBuilder.criarUsuarioLogin(usuario, senha);

        // Cria a requisição HTTP (sem cabeçalho)
        HttpEntity<UsuarioLogin> requisicao = new HttpEntity<>(usuarioLogin);

        // Envia a requisição para o endpoint de login
        ResponseEntity<UsuarioLogin> resposta = testRestTemplate
                .exchange("/usuarios/logar", HttpMethod.POST, requisicao, UsuarioLogin.class);

        // Lê o corpo da resposta
        UsuarioLogin corpoResposta = resposta.getBody();

        // Retorna o token se existir
        if (corpoResposta != null && corpoResposta.getToken() != null) {
            return corpoResposta.getToken();
        }

        throw new RuntimeException("Falha ao obter o token de autenticação para o usuário: " + usuario);
    }

    // Método auxiliar para criar requisições autenticadas
    public static <T> HttpEntity<T> criarRequisicaoComToken(T corpo, String token) {
        HttpHeaders cabecalho = new HttpHeaders();
        String tokenlimpo = token.startsWith("Bearer ") ? token.substring(7) : token;
        cabecalho.setBearerAuth(tokenlimpo);
        return new HttpEntity<>(corpo, cabecalho);
    }
    
    public static HttpEntity<Void> criarRequisicaoComToken(String token) {
    	return criarRequisicaoComToken(null, token);
    	
    }
}
