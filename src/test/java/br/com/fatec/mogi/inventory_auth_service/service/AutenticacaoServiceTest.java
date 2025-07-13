package br.com.fatec.mogi.inventory_auth_service.service;

import br.com.fatec.mogi.inventory_auth_service.domain.enums.TipoCache;
import br.com.fatec.mogi.inventory_auth_service.domain.exception.UsuarioNaoAutenticadoException;
import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.RefreshTokenRequestDTO;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AutenticacaoServiceTest {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private RedisService redisService;

    @Test
    @DisplayName("Deve gerar tokens para usuário com sucesso e salvar refreshToken no cache")
    void deveGerarTokensParaUsuarioComSucessoSalvarRefreshTokenCache() {
        String nome = "Usuario teste";
        String senha = "Senha123";
        String email = "email@gmail.com";
        var usuario = new Usuario(nome, senha, email);
        var tokens = autenticacaoService.gerarAutenticacao(usuario);
        var decodedAccessToken = autenticacaoService.decodeJwt(tokens.getAccessToken());
        assertEquals(usuario.getEmail().getEmail(), decodedAccessToken.getSubject());
        assertEquals(usuario.getNome(), decodedAccessToken.getClaim("nome").asString());
        var decodedRefreshToken = autenticacaoService.decodeJwt(tokens.getRefreshToken());
        assertEquals(usuario.getEmail().getEmail(), decodedRefreshToken.getSubject());
        assertEquals(900, tokens.getExpiresIn());
        var usuarioRefreshToken = redisService.buscar(TipoCache.REFRESH_TOKEN, tokens.getRefreshToken());
        assertNotNull(usuarioRefreshToken);
    }

    @Test
    @DisplayName("Não deve decodificar JWT inválido")
    void naoDeveDecodificarJwtInvalido() {
        assertThrows(JWTVerificationException.class, () -> {
            autenticacaoService.decodeJwt("invalido");
        });
    }

    @Test
    @DisplayName("Deve gerar novo JWT com base no refreshToken do usuario")
    void deveGerarNovoJwtComBaseRefreshTokenUsuario() {
        String nome = "Usuario teste";
        String senha = "Senha123";
        String email = "email@gmail.com";
        var usuario = new Usuario(nome, senha, email);
        var tokens = autenticacaoService.gerarAutenticacao(usuario);
        var newTokens = autenticacaoService.gerarAutenticacao(new RefreshTokenRequestDTO(tokens.getRefreshToken()));
        assertNotEquals(tokens.getRefreshToken(), newTokens.getRefreshToken());
        assertNotEquals(tokens.getAccessToken(), newTokens.getAccessToken());
    }

    @Test
    @DisplayName("Deve lançar exceção ao gerar novo JWT para usuário não autenticado")
    void deveLancarExecaoGerarNovoJwtUsuarioNaoAutenticado() {
        assertThrows(UsuarioNaoAutenticadoException.class, () -> {
            autenticacaoService.gerarAutenticacao(new RefreshTokenRequestDTO(UUID.randomUUID().toString()));
        });
    }

}
