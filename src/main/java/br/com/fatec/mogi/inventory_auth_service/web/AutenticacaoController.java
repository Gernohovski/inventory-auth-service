package br.com.fatec.mogi.inventory_auth_service.web;

import br.com.fatec.mogi.inventory_auth_service.service.UsuarioService;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.LoginRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.response.LoginResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth-service/v1/autenticacao")
public record AutenticacaoController(UsuarioService usuarioService) {

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        var tokensDto = usuarioService.login(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokensDto);
    }

}
