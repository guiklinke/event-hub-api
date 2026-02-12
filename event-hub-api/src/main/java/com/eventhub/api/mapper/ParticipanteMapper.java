package com.eventhub.api.mapper;

import com.eventhub.api.domain.entity.Participante;
import com.eventhub.api.dto.participante.ParticipanteRequest;
import com.eventhub.api.dto.participante.ParticipanteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipanteMapper {

    @Mapping(target = "id", ignore = true)
    Participante toEntity(ParticipanteRequest dto);

    ParticipanteResponse toDto(Participante entity);
}