package br.com.fatec.mogi.inventory_auth_service.web;

import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import br.com.fatec.mogi.inventory_auth_service.service.UsuarioService;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.CriarUsuarioRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth-service/v1/usuarios")
public record UsuarioController(UsuarioService usuarioService) {

	@PostMapping
	public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody CriarUsuarioRequestDTO dto) {
		var usuario = usuarioService.cadastrarUsuario(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
	}

}
