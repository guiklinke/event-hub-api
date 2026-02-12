package com.eventhub.api.service;

import com.eventhub.api.domain.entity.Participante;
import com.eventhub.api.domain.repository.ParticipanteRepository;
import com.eventhub.api.dto.pagination.PageResponse;
import com.eventhub.api.dto.participante.ParticipanteRequest;
import com.eventhub.api.dto.participante.ParticipanteResponse;
import com.eventhub.api.exception.NegocioException;
import com.eventhub.api.mapper.ParticipanteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;
    private final ParticipanteMapper participanteMapper;

    @Transactional
    public ParticipanteResponse cadastrar(ParticipanteRequest participanteRequest) {
        if (participanteRepository.existsByEmail(participanteRequest.email())) {
            throw new NegocioException("Este e-mail já está vinculado a um participante.");
        }

        Participante participante = participanteMapper.toEntity(participanteRequest);
        participanteRepository.save(participante);

        return participanteMapper.toDto(participante);
    }

    @Transactional(readOnly = true)
    public PageResponse<ParticipanteResponse> listarTodos(Pageable pageable) {
        Page<Participante> page = participanteRepository.findAll(pageable);
        return PageResponse.build(page.map(participanteMapper::toDto));
    }
}