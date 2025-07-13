package br.com.fatec.mogi.inventory_auth_service.service;

import br.com.fatec.mogi.inventory_auth_service.domain.exception.*;
import br.com.fatec.mogi.inventory_auth_service.repository.FuncaoRepository;
import br.com.fatec.mogi.inventory_auth_service.repository.UsuarioFuncaoRepository;
import br.com.fatec.mogi.inventory_auth_service.repository.UsuarioRepository;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.CadastrarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.ConfirmarCadastroUsuarioRequestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private FuncaoRepository funcaoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioFuncaoRepository usuarioFuncaoRepository;

	@AfterEach
	void cleanUp() {
		usuarioRepository.deleteAll();
	}

	@Test
	@DisplayName("Deve cadastrar um usuário com sucesso")
	void deveCadastrarUmUsuarioComSucesso() {
		var funcao = funcaoRepository.findAll();
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("email@gmail.com")
			.senha("Senha123")
			.funcaoId(funcao.getFirst().getId())
			.build();
		var usuario = usuarioService.cadastrarUsuario(dto);
		assertNotNull(usuario);
		assertEquals("email@gmail.com", usuario.getEmail().getEmail());
		assertTrue(BCrypt.checkpw("Senha123", usuario.getSenha().getSenha()));
		var usuarioFuncao = usuarioFuncaoRepository.findByUsuarioId(usuario.getId());
		assertEquals(funcao.getFirst().getId(), usuarioFuncao.getFirst().getFuncao().getId());
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "teste", "teste@", "@dominio.com" })
	@DisplayName("Não deve cadastrar usuário com e-mail inválido")
	void naoDeveCadastrarUsuarioComEmailInvalido(String email) {
		var funcao = funcaoRepository.findAll();
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email(email)
			.senha("Senha123")
			.funcaoId(funcao.getFirst().getId())
			.build();
		assertThrows(EmailInvalidoException.class, () -> {
			usuarioService.cadastrarUsuario(dto);
		});
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "senha123", "SENHA123", "asdasdasd" })
	@DisplayName("Não deve cadastrar usuário com senha inválida")
	void naoDeveCadastrarUsuarioSenhaInvalida(String senha) {
		var funcao = funcaoRepository.findAll();
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("email@gmail.com")
			.senha(senha)
			.funcaoId(funcao.getFirst().getId())
			.build();
		assertThrows(SenhaInvalidaException.class, () -> {
			usuarioService.cadastrarUsuario(dto);
		});
	}

	@Test
	@DisplayName("Não deve cadastrar usuário com e-mail já cadastrado")
	void naoDeveCadastrarUsuarioComEmailJaCadastrado() {
		var funcao = funcaoRepository.findAll();
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("email@gmail.com")
			.senha("Senha123")
			.funcaoId(funcao.getFirst().getId())
			.build();
		var usuario = usuarioService.cadastrarUsuario(dto);
		assertNotNull(usuario);
		assertThrows(EmailJaUtilizadoException.class, () -> {
			usuarioService.cadastrarUsuario(dto);
		});
	}

	@Test
	@DisplayName("Não deve cadastrar usuário para função inválida")
	void naoDeveCadastrarUsuarioParaFuncaoInvalida() {
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("email@gmail.com")
			.senha("Senha123")
			.funcaoId(20L)
			.build();
		assertThrows(FuncaoNaoEncontrada.class, () -> {
			usuarioService.cadastrarUsuario(dto);
		});
	}

	@Test
	@DisplayName("Deve confirmar o cadastro de um usuário com sucesso")
	void deveConfirmarCadastroUsuarioComSucesso() {
		var funcao = funcaoRepository.findAll();
		CadastrarUsuarioRequestDTO cadastrarUsuarioRequestDTO = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("email@gmail.com")
			.senha("Senha123")
			.funcaoId(funcao.getFirst().getId())
			.build();
		var usuario = usuarioService.cadastrarUsuario(cadastrarUsuarioRequestDTO);
		ConfirmarCadastroUsuarioRequestDTO confirmarCadastroUsuarioRequestDTO = ConfirmarCadastroUsuarioRequestDTO
			.builder()
			.email(usuario.getEmail().getEmail())
			.build();
		var confirmado = usuarioService.confirmarCadastro(confirmarCadastroUsuarioRequestDTO);
		assertTrue(confirmado);
		var usuarioSalvo = usuarioRepository.findByEmail(usuario.getEmail())
			.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
		assertTrue(usuarioSalvo.isAtivo());
	}

	@Test
	@DisplayName("Deve criar usuário ativo caso não conseguir enviar e-mail de confirmação")
	void deveCriarUsuarioAtivoCasoNaoConseguirEnviarEmailConfirmacao() {
		var funcao = funcaoRepository.findAll();
		CadastrarUsuarioRequestDTO cadastrarUsuarioRequestDTO = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("emailinvalido@gmail.com")
			.senha("Senha123")
			.funcaoId(funcao.getFirst().getId())
			.build();
		var usuario = usuarioService.cadastrarUsuario(cadastrarUsuarioRequestDTO);
		assertTrue(usuario.isAtivo());
	}

}
