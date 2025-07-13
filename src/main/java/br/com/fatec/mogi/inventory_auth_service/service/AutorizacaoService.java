package br.com.fatec.mogi.inventory_auth_service.service;

import br.com.fatec.mogi.inventory_auth_service.web.dto.request.AutorizarUsuarioRequestDTO;

public interface AutorizacaoService {

	boolean autorizar(AutorizarUsuarioRequestDTO dto, String accessToken);

}
