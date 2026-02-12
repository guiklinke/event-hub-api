package com.eventhub.api.service;

import com.eventhub.api.domain.entity.Evento;
import com.eventhub.api.domain.repository.EventoRepository;
import com.eventhub.api.domain.repository.IngressoRepository;
import com.eventhub.api.dto.evento.EventoRequest;
import com.eventhub.api.dto.evento.EventoResponse;
import com.eventhub.api.dto.pagination.PageResponse;
import com.eventhub.api.exception.NegocioException;
import com.eventhub.api.mapper.EventoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final IngressoRepository ingressoRepository;
    private final EventoMapper eventoMapper;

    @Transactional(readOnly = true)
    public PageResponse<EventoResponse> listarTodos(Pageable pageable) {
        Page<Evento> page = eventoRepository.findAll(pageable);
        return PageResponse.build(page.map(eventoMapper::toDTO));
    }

    @Transactional(readOnly = true)
    public EventoResponse buscarPorId(Long id) {
        Evento evento = buscarEventoPorId(id);
        return eventoMapper.toDTO(evento);
    }

    @Transactional
    public EventoResponse criar(EventoRequest dados) {
        Evento evento = eventoMapper.toEntity(dados);
        eventoRepository.save(evento);
        return eventoMapper.toDTO(evento);
    }

    @Transactional
    public EventoResponse atualizar(Long id, EventoRequest dados) {
        Evento evento = buscarEventoPorId(id);

        eventoMapper.updateEntityFromDTO(dados, evento);

        eventoRepository.save(evento);
        return eventoMapper.toDTO(evento);
    }

    @Transactional
    public void deletar(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new EntityNotFoundException("Evento não encontrado com ID: " + id);
        }

        if (ingressoRepository.existsByEventoId(id)) {
            throw new NegocioException("Não é possível deletar um evento que possui ingressos vendidos.");
        }

        eventoRepository.deleteById(id);
    }

    private Evento buscarEventoPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado com ID: " + id));
    }

    @Transactional
    public Evento validarEReservarVaga(Long eventoId) {
        Evento evento = buscarEventoPorId(eventoId);

        if (evento.getCapacidade() <= 0) {
            log.warn("Tentativa de reserva negada. Evento lotado. ID: {}, Nome: '{}'",
                    evento.getId(), evento.getNome());
            throw new NegocioException("Não há vagas disponíveis.");
        }

        int capacidadeAnterior = evento.getCapacidade();
        evento.setCapacidade(capacidadeAnterior - 1);

        Evento eventoSalvo = eventoRepository.save(evento);
        log.info("Vaga reservada. Evento ID: {}. Capacidade: {} -> {}",
                evento.getId(), capacidadeAnterior, evento.getCapacidade());
        return eventoSalvo;
    }
}