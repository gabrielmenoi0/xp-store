package com.xp.store.dto;

import com.xp.store.utils.Validations.BasicInfoLogin;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Valid
public record LoginDTO(
        @Schema(description = "Email do usuário")
        @NotBlank(message = "Email não pode ser nulo ou vazio",groups = BasicInfoLogin.class)
        @Parameter(required = true)
        String email,
        @Schema(description = "Senha do usuário")
        @NotBlank(message = "Senha não pode ser nulo ou vazio.",groups = BasicInfoLogin.class)
        @Parameter(required = true)
        String senha

) {

}