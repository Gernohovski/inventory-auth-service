package br.com.fatec.mogi.inventory_auth_service.service.impl;

import br.com.fatec.mogi.inventory_auth_service.domain.enums.TipoCache;
import br.com.fatec.mogi.inventory_auth_service.domain.exception.FuncionalidadeNaoMapeadaException;
import br.com.fatec.mogi.inventory_auth_service.domain.exception.UsuarioNaoAutenticadoException;
import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import br.com.fatec.mogi.inventory_auth_service.repository.FuncionalidadeRepository;
import br.com.fatec.mogi.inventory_auth_service.repository.UsuarioRepository;
import br.com.fatec.mogi.inventory_auth_service.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_auth_service.service.RedisService;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.AutorizarUsuarioRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutorizacaoServiceImpl implements AutorizacaoService {

	private final FuncionalidadeRepository funcionalidadeRepository;

	private final UsuarioRepository usuarioRepository;

	private final RedisService redisService;

	@Override
	public boolean autorizar(AutorizarUsuarioRequestDTO dto, String accessToken) {
		funcionalidadeRepository.findByFuncionalidade(dto.getFuncionalidade())
			.orElseThrow(FuncionalidadeNaoMapeadaException::new);
		var usuario = (Usuario) redisService.buscar(TipoCache.SESSAO_USUARIO, accessToken);
		if (usuario == null) {
			throw new UsuarioNaoAutenticadoException();
		}
		return usuarioRepository.possuiFuncionalidade(usuario.getId(), dto.getFuncionalidade());
	}

}
