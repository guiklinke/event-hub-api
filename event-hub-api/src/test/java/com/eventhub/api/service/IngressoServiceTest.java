package com.eventhub.api.service;

import com.eventhub.api.domain.entity.Evento;
import com.eventhub.api.domain.entity.Ingresso;
import com.eventhub.api.domain.entity.Participante;
import com.eventhub.api.domain.repository.IngressoRepository;
import com.eventhub.api.domain.repository.ParticipanteRepository;
import com.eventhub.api.dto.ingresso.IngressoRequest;
import com.eventhub.api.dto.ingresso.IngressoResponse;
import com.eventhub.api.exception.NegocioException;
import com.eventhub.api.mapper.IngressoMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngressoServiceTest {
    private final OffsetDateTime dataFixaOffset = OffsetDateTime.parse("2026-02-12T10:00:00-03:00");
    private final Instant dataFixaInstant = dataFixaOffset.toInstant();

    @InjectMocks
    private IngressoService ingressoService;

    @Mock
    private IngressoMapper ingressoMapper;

    @Mock
    private EventoService eventoService;

    @Mock
    private ParticipanteRepository participanteRepository;

    @Mock
    private IngressoRepository ingressoRepository;

    private IngressoRequest ingressoRequest;
    private Evento evento;
    private Participante participante;
    private Ingresso ingresso;
    private IngressoResponse ingressoResponse;

    @BeforeEach
    void setUp() {
        ingressoRequest = new IngressoRequest(1L, 1L);

        evento = Evento.builder()
                .id(1L)
                .nome("Tech Conference")
                .capacidade(9)
                .build();

        participante = Participante.builder()
                .id(1L)
                .nome("João")
                .email("joao@email.com")
                .build();

        ingresso = Ingresso.builder()
                .id(100L)
                .dataCompra(dataFixaInstant)
                .build();

        ingressoResponse = new IngressoResponse(
                100L, "Tech", "João", "joao@email.com",
                dataFixaOffset);
    }

    @Nested
    @DisplayName("realizarCompra")
    class RealizarCompra {

        @Test
        @DisplayName("Deve realizar a compra com sucesso chamando as dependências corretamente")
        void sucesso() {
            when(participanteRepository.findById(1L)).thenReturn(Optional.of(participante));

            when(eventoService.validarEReservarVaga(1L)).thenReturn(evento);

            when(ingressoRepository.save(any(Ingresso.class))).thenReturn(ingresso);
            when(ingressoMapper.toDTO(any(Ingresso.class))).thenReturn(ingressoResponse);

            IngressoResponse response = ingressoService.realizarCompra(ingressoRequest);

            assertNotNull(response);
            assertEquals(ingressoResponse.id(), response.id());

            verify(participanteRepository).findById(1L);
            verify(eventoService).validarEReservarVaga(1L);
            verify(ingressoRepository).save(any(Ingresso.class));
        }

        @Test
        @DisplayName("Deve lançar NegocioException quando a EventoService informar que não há vagas")
        void semCapacidade() {
            when(participanteRepository.findById(1L)).thenReturn(Optional.of(participante));

            doThrow(new NegocioException("Não há vagas disponíveis."))
                    .when(eventoService).validarEReservarVaga(1L);

            NegocioException exception = assertThrows(NegocioException.class, () ->
                    ingressoService.realizarCompra(ingressoRequest));

            assertEquals("Não há vagas disponíveis.", exception.getMessage());

            verify(ingressoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar EntityNotFoundException quando o participante não for encontrado")
        void participanteNaoEncontrado() {
            when(participanteRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () ->
                    ingressoService.realizarCompra(ingressoRequest));

            verify(eventoService, never()).validarEReservarVaga(anyLong());
            verify(ingressoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve repassar EntityNotFoundException se a EventoService não encontrar o evento")
        void eventoNaoEncontrado() {
            when(participanteRepository.findById(1L)).thenReturn(Optional.of(participante));

            doThrow(new EntityNotFoundException("Evento não encontrado"))
                    .when(eventoService).validarEReservarVaga(1L);

            assertThrows(EntityNotFoundException.class, () ->
                    ingressoService.realizarCompra(ingressoRequest));

            verify(ingressoRepository, never()).save(any());
        }
    }
}