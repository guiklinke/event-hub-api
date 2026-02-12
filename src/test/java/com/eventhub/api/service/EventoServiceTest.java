package com.eventhub.api.service;

import com.eventhub.api.domain.entity.Evento;
import com.eventhub.api.domain.repository.EventoRepository;
import com.eventhub.api.domain.repository.IngressoRepository;
import com.eventhub.api.exception.NegocioException;
import com.eventhub.api.mapper.EventoMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @InjectMocks
    private EventoService eventoService;

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private IngressoRepository ingressoRepository;

    @Mock
    private EventoMapper eventoMapper;

    @Captor
    private ArgumentCaptor<Evento> eventoCaptor;

    private Evento evento;

    @BeforeEach
    void setUp() {
        evento = Evento.builder()
                .id(1L)
                .nome("Workshop Spring Boot")
                .capacidade(10)
                .build();
    }

    @Nested
    @DisplayName("validarEReservarVaga")
    class ValidarEReservarVaga {

        @Test
        @DisplayName("Deve decrementar a capacidade e salvar quando houver vagas")
        void sucesso() {
            when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
            when(eventoRepository.save(any(Evento.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Evento eventoAtualizado = eventoService.validarEReservarVaga(1L);

            assertEquals(9, eventoAtualizado.getCapacidade());

            verify(eventoRepository).save(eventoCaptor.capture());
            Evento eventoSalvo = eventoCaptor.getValue();
            assertEquals(9, eventoSalvo.getCapacidade());
        }

        @Test
        @DisplayName("Deve lançar NegocioException quando capacidade for zero")
        void semCapacidade() {
            evento.setCapacidade(0);
            when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

            NegocioException ex = assertThrows(NegocioException.class, () ->
                    eventoService.validarEReservarVaga(1L));

            assertEquals("Não há vagas disponíveis.", ex.getMessage());

            verify(eventoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar EntityNotFoundException quando evento não existir")
        void eventoNaoEncontrado() {
            when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () ->
                    eventoService.validarEReservarVaga(99L));

            verify(eventoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("deletar")
    class Deletar {

        @Test
        @DisplayName("Deve impedir deleção se houver ingressos vendidos")
        void impedirDelecaoComIngressos() {
            when(eventoRepository.existsById(1L)).thenReturn(true);
            when(ingressoRepository.existsByEventoId(1L)).thenReturn(true);

            assertThrows(NegocioException.class, () -> eventoService.deletar(1L));

            verify(eventoRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Deve deletar com sucesso se não houver ingressos")
        void sucesso() {
            when(eventoRepository.existsById(1L)).thenReturn(true);
            when(ingressoRepository.existsByEventoId(1L)).thenReturn(false);

            eventoService.deletar(1L);

            verify(eventoRepository, times(1)).deleteById(1L);
        }
    }
}