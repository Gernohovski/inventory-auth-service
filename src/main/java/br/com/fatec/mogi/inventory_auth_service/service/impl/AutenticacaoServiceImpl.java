package br.com.fatec.mogi.inventory_auth_service.service.impl;

import br.com.fatec.mogi.inventory_auth_service.domain.enums.TipoCache;
import br.com.fatec.mogi.inventory_auth_service.domain.exception.UsuarioNaoAutenticadoException;
import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import br.com.fatec.mogi.inventory_auth_service.service.AutenticacaoService;
import br.com.fatec.mogi.inventory_auth_service.service.RedisService;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.RefreshTokenRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.response.LoginResponseDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.response.RefreshTokenResponseDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AutenticacaoServiceImpl implements AutenticacaoService {

	@Value("${sistema.inventario.key}")
	private String secret;

	@Value("${jwt.expiration.time}")
	private Long expiration;

	@Value("${jwt.refresh.expiration.time}")
	private Long refreshExpiration;

	private final Algorithm algorithm = Algorithm.HMAC256(secret);

	private final RedisService redisService;

	@Override
	public LoginResponseDTO gerarAutenticacao(Usuario usuario) {
		var accessToken = this.gerarToken(usuario);
		var refreshToken = this.gerarRefreshToken(usuario.getEmail().getEmail());
		redisService.salvar(TipoCache.REFRESH_TOKEN, refreshToken, usuario, refreshExpiration);
		return new LoginResponseDTO(accessToken, refreshToken, expiration);
	}

	@Override
	public RefreshTokenResponseDTO gerarAutenticacao(RefreshTokenRequestDTO dto) {
		var usuario = (Usuario) redisService.buscar(TipoCache.REFRESH_TOKEN, dto.getRefreshToken());
		if (usuario == null) {
			throw new UsuarioNaoAutenticadoException();
		}
		var accessToken = this.gerarToken(usuario);
		var refreshToken = this.gerarRefreshToken(usuario.getEmail().getEmail());
		redisService.salvar(TipoCache.REFRESH_TOKEN, refreshToken, usuario, refreshExpiration);
		return new RefreshTokenResponseDTO(accessToken, refreshToken, expiration);
	}

	private String gerarToken(Usuario usuario) {
		return JWT.create()
			.withSubject(usuario.getEmail().getEmail())
			.withClaim("nome", usuario.getNome())
			.withIssuedAt(new Date())
			.withExpiresAt(Date.from(Instant.now().plusSeconds(expiration)))
			.sign(algorithm);
	}

	public String gerarRefreshToken(String email) {
		return JWT.create().withSubject(email).withIssuedAt(new Date()).sign(algorithm);
	}

}
