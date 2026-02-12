package com.eventhub.api.controller;

import com.eventhub.api.dto.pagination.PageResponse;
import com.eventhub.api.dto.participante.ParticipanteRequest;
import com.eventhub.api.dto.participante.ParticipanteResponse;
import com.eventhub.api.service.ParticipanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/participants")
@RequiredArgsConstructor
public class ParticipanteController {

    private final ParticipanteService participanteService;

    @PostMapping
    public ResponseEntity<ParticipanteResponse> cadastrar(
            @RequestBody @Valid ParticipanteRequest dto) {
        ParticipanteResponse response = participanteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ParticipanteResponse>> listar(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(participanteService.listarTodos(pageable));
    }
}