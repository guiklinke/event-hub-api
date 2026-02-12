package com.eventhub.api.dto.ingresso;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

public record IngressoResponse(
        Long id,
        String nomeEvento,
        String nomeParticipante,
        String emailParticipante,
        @Schema(
                type = "string",
                format = "date-time",
                example = "2026-02-12T13:31:00-03:00"
        )
        OffsetDateTime dataCompra
) {
}
