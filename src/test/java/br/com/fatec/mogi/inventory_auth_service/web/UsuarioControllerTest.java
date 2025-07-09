package br.com.fatec.mogi.inventory_auth_service.web;

import br.com.fatec.mogi.inventory_auth_service.domain.exception.EmailInvalidoException;
import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.CadastrarUsuarioRequestDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
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
        var errorMessage = RestAssured
                .given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .body(dto)
                    .log().all()
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
        var errorMessage = RestAssured
                .given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(dto)
                .log().all()
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
        RestAssured
                .given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(dto)
                .log().all()
                .when()
                .post("/auth-service/v1/usuarios")
                .then()
                .statusCode(201);

        var errorMessage = RestAssured
                .given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(dto)
                .log().all()
                .when()
                .post("/auth-service/v1/usuarios")
                .then()
                .statusCode(400)
                .extract()
                .body()
                .asString();

        assertEquals("E-mail já utilizado.", errorMessage);
    }

}
