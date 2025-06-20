package com.xp.store.utils.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResponseErrorMessageDto(
        @Schema(description = "Mensagem de resposta")
        String message,
        @Schema(description = "Código de status da resposta")
        int code,
        @Schema(description = "Objeto do erro")
        Object error

        ) {
}