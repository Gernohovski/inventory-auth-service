package br.com.fatec.mogi.inventory_auth_service.service;

import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("integration")
public class EmailServiceTest {

	@Autowired
	private EmailService emailService;

	@Test
	@DisplayName("Deve enviar e-mail de confirmação com sucesso")
	void deveEnviarEmailConfirmacaoComSucesso() {
		Usuario usuario = new Usuario("Rodrigo", "Senha123", "gernohovskirodrigo@gmail.com");
		var enviado = emailService.enviarEmailConfirmacao(usuario);
		assertTrue(enviado);
	}

}
