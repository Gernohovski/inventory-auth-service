package br.com.fatec.mogi.inventory_auth_service.service;

import br.com.fatec.mogi.inventory_auth_service.domain.enums.TipoCache;

public interface RedisService {

	void salvar(TipoCache tipoCache, String chave, Object valor, Long ttl);

	Object buscar(TipoCache tipoCache, String chave);

	void deletar(TipoCache tipoCache, String chave);

}
