package com.eventhub.api.dto.participante;

public record ParticipanteResponse(
        Long id,
        String nome,
        String email
) {
}