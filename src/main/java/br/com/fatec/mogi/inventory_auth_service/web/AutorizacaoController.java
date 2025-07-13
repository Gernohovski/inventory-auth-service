package br.com.fatec.mogi.inventory_auth_service.web;

import br.com.fatec.mogi.inventory_auth_service.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.AutorizarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.response.AutorizarUsuarioResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth-service/v1/autorizacao")
public record AutorizacaoController(AutorizacaoService autorizacaoService) {

	@PostMapping
	public ResponseEntity<AutorizarUsuarioResponseDTO> autorizar(@RequestBody AutorizarUsuarioRequestDTO dto,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var autorizado = autorizacaoService.autorizar(dto, accessToken);
		return ResponseEntity.status(HttpStatus.OK).body(new AutorizarUsuarioResponseDTO(autorizado));
	}

}
