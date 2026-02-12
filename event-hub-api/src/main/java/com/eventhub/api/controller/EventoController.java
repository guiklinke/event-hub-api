package com.eventhub.api.controller;

import com.eventhub.api.dto.evento.EventoRequest;
import com.eventhub.api.dto.evento.EventoResponse;
import com.eventhub.api.dto.pagination.PageResponse;
import com.eventhub.api.service.EventoService;
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
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    public ResponseEntity<EventoResponse> criar(@RequestBody @Valid EventoRequest body) {
        EventoResponse novoEvento = eventoService.criar(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEvento);
    }

    @GetMapping
    public ResponseEntity<PageResponse<EventoResponse>> listar(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(eventoService.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid EventoRequest body) {
        return ResponseEntity.ok(eventoService.atualizar(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        eventoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}