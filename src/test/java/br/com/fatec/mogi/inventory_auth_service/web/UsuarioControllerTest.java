package br.com.fatec.mogi.inventory_auth_service.web;

import br.com.fatec.mogi.inventory_auth_service.web.dto.request.CadastrarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.ConfirmarCadastroUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.response.ConfirmarCadastroUsuarioResponseDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
public class UsuarioControllerTest {

	@LocalServerPort
	private int port;

	@Test
	@DisplayName("Não deve cadastrar usuário com e-mail inválido")
	void naoDeveCadastrarUsuarioComEmailInvalido() {
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("")
			.senha("Senha123")
			.funcaoId(1L)
			.build();
		var errorMessage = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(dto)
			.log()
			.all()
			.when()
			.post("/auth-service/v1/usuarios")
			.then()
			.statusCode(400)
			.extract()
			.body()
			.asString();

		assertEquals("O e-mail não pode ser nulo ou vazio.", errorMessage);
	}

	@Test
	@DisplayName("Não deve cadastrar usuário com senha inválida")
	void naoDeveCadastrarUsuarioComSenhaInvalida() {
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("email@gmail.com")
			.senha("")
			.funcaoId(1L)
			.build();
		var errorMessage = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(dto)
			.log()
			.all()
			.when()
			.post("/auth-service/v1/usuarios")
			.then()
			.statusCode(400)
			.extract()
			.body()
			.asString();

		assertEquals("A senha deve ter pelo menos 8 caracteres.", errorMessage);
	}

	@Test
	@DisplayName("Não deve cadastrar usuário com e-mail já cadastrado")
	void naoDeveCadastrarUsuarioComEmailJaCadastrado() {
		CadastrarUsuarioRequestDTO dto = CadastrarUsuarioRequestDTO.builder()
			.nome("Usuario teste")
			.email("email@gmail.com")
			.senha("Senha123")
			.funcaoId(1L)
			.build();
		RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(dto)
			.log()
			.all()
			.when()
			.post("/auth-service/v1/usuarios")
			.then()
			.statusCode(201);

		var errorMessage = RestAssured.given()
			.port(port)
			.contentType(ContentType.JSON)
			.body(dto)
			.log()
			.all()
			.when()
			.post("/auth-service/v1/usuarios")
			.then()
			.statusCode(400)
			.extract()
			.body()
			.asString();

		assertEquals("E-mail já utilizado.", errorMessage);
	}

	@Test
	@DisplayName("Deve confirmar o cadastro de um usuário com sucesso")
	void deveConfirmarCadastroUsuarioComSucesso() {
		CadastrarUsuarioRequestDTO cadastrarUsuarioRequestDTO = CadastrarUsuarioRequestDTO.builder()
				.nome("Usuario teste")
				.email("email123@gmail.com")
				.senha("Senha123")
				.funcaoId(1L)
				.build();
		RestAssured.given()
				.port(port)
				.contentType(ContentType.JSON)
				.body(cadastrarUsuarioRequestDTO)
				.log()
				.all()
				.when()
				.post("/auth-service/v1/usuarios")
				.then()
				.statusCode(201);
		ConfirmarCadastroUsuarioRequestDTO confirmarCadastroUsuarioRequestDTO = ConfirmarCadastroUsuarioRequestDTO.builder()
				.email("email123@gmail.com")
				.build();
		var confirmarCadastroUsuarioResponseDto = RestAssured.given()
				.port(port)
				.contentType(ContentType.JSON)
				.body(confirmarCadastroUsuarioRequestDTO)
				.log()
				.all()
				.when()
				.put("/auth-service/v1/usuarios/confirmar-cadastro")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(ConfirmarCadastroUsuarioResponseDTO.class);

		assertTrue(confirmarCadastroUsuarioResponseDto.isConfirmado());
	}

	@Test
	@DisplayName("Não deve confirmar o cadastro de um usuário inválido")
	void naoDeveConfirmarCadastroUsuarioInvalido() {
		ConfirmarCadastroUsuarioRequestDTO confirmarCadastroUsuarioRequestDTO = ConfirmarCadastroUsuarioRequestDTO.builder()
				.email("email1234@gmail.com")
				.build();
		var errorMessage = RestAssured.given()
				.port(port)
				.contentType(ContentType.JSON)
				.body(confirmarCadastroUsuarioRequestDTO)
				.log()
				.all()
				.when()
				.put("/auth-service/v1/usuarios/confirmar-cadastro")
				.then()
				.statusCode(400)
				.extract()
				.body()
				.asString();

		assertEquals("Usuário não encontrado.", errorMessage);
	}

}
