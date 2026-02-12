package com.eventhub.api.dto.ingresso;

import jakarta.validation.constraints.NotNull;

public record IngressoRequest(
        @NotNull(message = "O ID do evento é obrigatório")
        Long eventoId,

        @NotNull(message = "O ID do participante é obrigatório")
        Long participanteId
) {
}