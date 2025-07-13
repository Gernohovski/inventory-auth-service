package br.com.fatec.mogi.inventory_auth_service.service;

import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.RefreshTokenRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.response.LoginResponseDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.response.RefreshTokenResponseDTO;

public interface AutenticacaoService {

	LoginResponseDTO gerarAutenticacao(Usuario usuario);

	RefreshTokenResponseDTO gerarAutenticacao(RefreshTokenRequestDTO dto);

}
