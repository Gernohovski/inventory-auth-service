package br.com.fatec.mogi.inventory_auth_service.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoCache {

	REFRESH_TOKEN("cache-refresh-token"),SESSAO_USUARIO("cache-sessao-usuario");

	private final String nome;

}
