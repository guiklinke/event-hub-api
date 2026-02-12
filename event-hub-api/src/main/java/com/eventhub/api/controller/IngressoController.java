package com.eventhub.api.controller;

import com.eventhub.api.dto.ingresso.IngressoRequest;
import com.eventhub.api.dto.ingresso.IngressoResponse;
import com.eventhub.api.dto.pagination.PageResponse;
import com.eventhub.api.service.IngressoService;
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
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class IngressoController {

    private final IngressoService ingressoService;

    @PostMapping
    public ResponseEntity<IngressoResponse> comprar(@RequestBody @Valid IngressoRequest dto) {
        IngressoResponse ingresso = ingressoService.realizarCompra(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingresso);
    }

    @GetMapping("/participants/{participanteId}")
    public ResponseEntity<PageResponse<IngressoResponse>> listarHistorico(
            @PathVariable Long participanteId,
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "dataCompra", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ingressoService.listarPorParticipante(participanteId, pageable));
    }
}