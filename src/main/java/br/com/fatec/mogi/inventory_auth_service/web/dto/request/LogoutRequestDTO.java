package br.com.fatec.mogi.inventory_auth_service.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequestDTO {

    private String accessToken;

    private String refreshToken;

}
