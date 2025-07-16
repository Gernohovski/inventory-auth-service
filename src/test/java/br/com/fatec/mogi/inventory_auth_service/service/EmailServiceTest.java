package br.com.fatec.mogi.inventory_auth_service.service;

import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("integration")
public class EmailServiceTest {

	@Autowired
	private EmailService emailService;

	@Test
	@DisplayName("Deve enviar e-mail de confirmação com sucesso")
	@Disabled("Desabilitado temporariamente devido a limitação do serviço de envio de e-mail")
	void deveEnviarEmailConfirmacaoComSucesso() {
		Usuario usuario = new Usuario("Rodrigo", "Senha123", "gernohovskirodrigo@gmail.com");
		var enviado = emailService.enviarEmailConfirmacao(usuario);
		assertTrue(enviado);
	}

	@Test
	@DisplayName("Deve retornar falso ao realizar envio endereço inválido")
	void deveRetornarFalseRealizarEnvioEnderecoInvalido() {
		Usuario usuario = new Usuario("Rodrigo", "Senha123", "usuariotodotorto@gmail.com");
		var enviado = emailService.enviarEmailConfirmacao(usuario);
		assertFalse(enviado);
	}

	@Test
	@DisplayName("Deve enviar e-mail de redefinição de senha com sucesso")
	@Disabled("Desabilitado temporariamente devido a limitação do serviço de envio de e-mail")
	void deveEnviarEmailRedefinicaoSenhaComSucesso() {
		Usuario usuario = new Usuario("Rodrigo", "Senha123", "gernohovskirodrigo@gmail.com");
		var enviado = emailService.enviarEmailResetSenha(usuario);
		assertTrue(enviado);
	}

	@Test
	@DisplayName("Deve retornar falso ao realizar envio de redefinição de senha para endereço inválido")
	void deveRetornarFalseRealizarEnvioRedefinicaoSenhaEnderecoInvalido() {
		Usuario usuario = new Usuario("Rodrigo", "Senha123", "usuariotodotorto@gmail.com");
		var enviado = emailService.enviarEmailConfirmacao(usuario);
		assertFalse(enviado);
	}

}
