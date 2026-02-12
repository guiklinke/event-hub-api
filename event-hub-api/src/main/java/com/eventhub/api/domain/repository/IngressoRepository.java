package com.eventhub.api.domain.repository;

import com.eventhub.api.domain.entity.Ingresso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {

    boolean existsByEventoId(Long eventoId);

    @EntityGraph(attributePaths = {"evento", "participante"})
    Page<Ingresso> findByParticipanteId(Long participanteId, Pageable pageable);
}