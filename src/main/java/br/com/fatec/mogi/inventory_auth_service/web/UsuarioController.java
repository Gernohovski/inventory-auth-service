package br.com.fatec.mogi.inventory_auth_service.web;

import br.com.fatec.mogi.inventory_auth_service.service.UsuarioService;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.CadastrarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.ConfirmarCadastroUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.response.CadastrarUsuarioResponseDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.response.ConfirmarCadastroUsuarioResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth-service/v1/usuarios")
public record UsuarioController(UsuarioService usuarioService) {

	@PostMapping
	public ResponseEntity<CadastrarUsuarioResponseDTO> cadastrarUsuario(@RequestBody CadastrarUsuarioRequestDTO dto) {
		var usuario = usuarioService.cadastrarUsuario(dto);
		CadastrarUsuarioResponseDTO responseDTO = CadastrarUsuarioResponseDTO.builder()
			.email(usuario.getEmail().getEmail())
			.nome(usuario.getNome())
			.build();
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}

	@PutMapping("/confirmar-cadastro")
	public ResponseEntity<ConfirmarCadastroUsuarioResponseDTO> confirmarCadastro(
			@RequestBody ConfirmarCadastroUsuarioRequestDTO dto) {
		var confirmado = usuarioService.confirmarCadastro(dto);
		return ResponseEntity.status(HttpStatus.OK).body(new ConfirmarCadastroUsuarioResponseDTO(confirmado));
	}

}
