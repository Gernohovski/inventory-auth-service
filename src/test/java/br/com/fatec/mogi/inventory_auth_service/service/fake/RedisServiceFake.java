package br.com.fatec.mogi.inventory_auth_service.service.fake;

import br.com.fatec.mogi.inventory_auth_service.domain.enums.TipoCache;
import br.com.fatec.mogi.inventory_auth_service.service.RedisService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile({ "test", "integration" })
public class RedisServiceFake implements RedisService {

	private static class ValorExpiravel {

		Object valor;

		Instant expiraEm;

		ValorExpiravel(Object valor, Long ttlSegundos) {
			this.valor = valor;
			this.expiraEm = Instant.now().plusSeconds(ttlSegundos);
		}

		boolean estaExpirado() {
			return Instant.now().isAfter(expiraEm);
		}

	}

	private final Map<String, ValorExpiravel> storage = new ConcurrentHashMap<>();

	@Override
	public void salvar(TipoCache tipoCache, String chave, Object valor, Long ttl) {
		String chaveFinal = tipoCache.getNome() + ":" + chave;
		storage.put(chaveFinal, new ValorExpiravel(valor, ttl));
	}

	@Override
	public Object buscar(TipoCache tipoCache, String chave) {
		String chaveFinal = tipoCache.getNome() + ":" + chave;
		ValorExpiravel entry = storage.get(chaveFinal);

		if (entry == null || entry.estaExpirado()) {
			storage.remove(chaveFinal);
			return null;
		}

		return entry.valor;
	}

	@Override
	public void deletar(TipoCache tipoCache, String chave) {
		String chaveFinal = tipoCache.getNome() + ":" + chave;
		storage.remove(chaveFinal);
	}

}
