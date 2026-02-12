package com.eventhub.api.dto.evento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record EventoRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotNull
        @Future(message = "A data deve ser no futuro")
        @Schema(
                type = "string",
                format = "date-time",
                example = "2026-12-25T14:00:00-03:00"
        )
        OffsetDateTime dataEvento,

        @NotBlank(message = "O local é obrigatório")
        String local,

        @NotNull(message = "A capacidade é obrigatória")
        @Min(value = 1, message = "A capacidade deve ser no mínimo 1")
        int capacidade
) {
}