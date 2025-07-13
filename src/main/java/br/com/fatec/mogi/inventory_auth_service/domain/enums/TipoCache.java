package br.com.fatec.mogi.inventory_auth_service.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoCache {

	SESSAO("cache-sessao-usuario"), REFRESH_TOKEN("cache-refresh-token");

	private final String nome;

}
