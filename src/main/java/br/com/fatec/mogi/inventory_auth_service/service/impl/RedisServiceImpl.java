package br.com.fatec.mogi.inventory_auth_service.service.impl;

import br.com.fatec.mogi.inventory_auth_service.domain.enums.TipoCache;
import br.com.fatec.mogi.inventory_auth_service.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Profile("!test & !integration")
public class RedisServiceImpl implements RedisService {

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public void salvar(TipoCache tipoCache, String chave, Object valor, Long ttl) {
		String chaveFinal = tipoCache.getNome() + ":" + chave;
		redisTemplate.delete(chaveFinal);
		redisTemplate.opsForValue().set(chaveFinal, valor, Duration.ofSeconds(ttl));
	}

	@Override
	public Object buscar(TipoCache tipoCache, String chave) {
		String chaveFinal = tipoCache.getNome() + ":" + chave;
		return redisTemplate.opsForValue().get(chaveFinal);
	}

}
