package com.eventhub.api.domain.repository;

import com.eventhub.api.domain.entity.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    boolean existsByEmail(String email);
}