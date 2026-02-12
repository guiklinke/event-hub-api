package com.eventhub.api.dto.evento;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

public record EventoResponse(
        Long id,
        String nome,
        @Schema(
                type = "string",
                format = "date-time",
                example = "2026-12-25T14:00:00-03:00"
        )
        OffsetDateTime dataEvento,
        String local,
        int capacidade
) {
}