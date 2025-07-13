package br.com.fatec.mogi.inventory_auth_service.web;

import br.com.fatec.mogi.inventory_auth_service.web.dto.request.AutorizarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.CadastrarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.LoginRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.response.AutorizarUsuarioResponseDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.response.LoginResponseDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AutorizacaoControllerTest {

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Deve obter autorização com sucesso")
    void deveObterAutorizacaoComSucesso() {
        String email = UUID.randomUUID().toString().concat("@gmail.com");
        String senha = "Senha123";
        cadastrarUsuario(email, senha);
        var tokens = fazerLogin(email, senha);
        AutorizarUsuarioRequestDTO dto = AutorizarUsuarioRequestDTO.builder()
                .funcionalidade("/excluir-bem")
                .build();
        var responseDto = RestAssured.given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(dto)
                .header("X-ACCESS-TOKEN", tokens.getAccessToken())
                .log()
                .all()
                .when()
                .post("/auth-service/v1/autorizacao")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(AutorizarUsuarioResponseDTO.class);

        assertTrue(responseDto.isAutorizado());
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar obter acesso a funcionalidade não mapeada")
    void deveRetornarErroTentarObterAcessoFuncionalidadeNaoMapeada() {
        String email = UUID.randomUUID().toString().concat("@gmail.com");
        String senha = "Senha123";
        cadastrarUsuario(email, senha);
        var tokens = fazerLogin(email, senha);
        AutorizarUsuarioRequestDTO dto = AutorizarUsuarioRequestDTO.builder()
                .funcionalidade("invalido")
                .build();
        var errorMessage = RestAssured.given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(dto)
                .header("X-ACCESS-TOKEN", tokens.getAccessToken())
                .log()
                .all()
                .when()
                .post("/auth-service/v1/autorizacao")
                .then()
                .statusCode(400)
                .extract()
                .body()
                .asString();

        assertEquals("Funcionalidade não mapeada.", errorMessage);
    }


    private LoginResponseDTO fazerLogin(String email, String senha) {
        LoginRequestDTO dto = LoginRequestDTO.builder()
                .senha(senha)
                .email(email)
                .build();
        return RestAssured.given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(dto)
                .log()
                .all()
                .when()
                .post("/auth-service/v1/autenticacao/login")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(LoginResponseDTO.class);
    }

    private void cadastrarUsuario(String email, String senha) {
        CadastrarUsuarioRequestDTO cadastrarUsuarioRequestDTO = CadastrarUsuarioRequestDTO.builder()
                .nome("Usuario teste")
                .email(email)
                .senha(senha)
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
    }
}
