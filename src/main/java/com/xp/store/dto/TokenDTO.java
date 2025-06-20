package com.xp.store.dto;

import com.xp.store.utils.Validations.BasicInfoToken;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
@Valid
public record TokenDTO(
        @Schema(description = "Token de acesso")
        @NotBlank(message = "Token não pode ser nulo ou vazio",groups = BasicInfoToken.class)
        @Parameter(required = true)
        String token
) {

}
