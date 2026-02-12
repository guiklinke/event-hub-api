package com.eventhub.api.service;

import com.eventhub.api.domain.entity.Evento;
import com.eventhub.api.domain.entity.Ingresso;
import com.eventhub.api.domain.entity.Participante;
import com.eventhub.api.domain.repository.IngressoRepository;
import com.eventhub.api.domain.repository.ParticipanteRepository;
import com.eventhub.api.dto.ingresso.IngressoRequest;
import com.eventhub.api.dto.ingresso.IngressoResponse;
import com.eventhub.api.dto.pagination.PageResponse;
import com.eventhub.api.mapper.IngressoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngressoService {

    private final IngressoRepository ingressoRepository;
    private final ParticipanteRepository participanteRepository;
    private final EventoService eventoService;
    private final IngressoMapper ingressoMapper;

    @Transactional
    public IngressoResponse realizarCompra(IngressoRequest ingressoRequest) {
        log.info("Iniciando compra. EventoID: {}, ParticipanteID: {}", ingressoRequest.eventoId(), ingressoRequest.participanteId());
        Participante participante = participanteRepository.findById(ingressoRequest.participanteId())
                .orElseThrow(() -> new EntityNotFoundException("Participante não encontrado"));

        Evento evento = eventoService.validarEReservarVaga(ingressoRequest.eventoId());

        Ingresso ingresso = Ingresso.builder()
                .evento(evento)
                .participante(participante)
                .build();

        log.info("Compra finalizada com sucesso. Novo IngressoID: {}, EventoID: {}, ParticipanteID: {}",
                ingresso.getId(),
                ingressoRequest.eventoId(),
                ingressoRequest.participanteId());

        return ingressoMapper.toDTO(ingressoRepository.save(ingresso));
    }

    @Transactional(readOnly = true)
    public PageResponse<IngressoResponse> listarPorParticipante(Long participanteId, Pageable pageable) {
        if (!participanteRepository.existsById(participanteId)) {
            throw new EntityNotFoundException("Participante não encontrado");
        }

        Page<Ingresso> page = ingressoRepository.findByParticipanteId(participanteId, pageable);

        return PageResponse.build(page.map(ingressoMapper::toDTO));
    }

}