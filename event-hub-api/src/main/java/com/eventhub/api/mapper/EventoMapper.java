package com.eventhub.api.mapper;

import com.eventhub.api.domain.entity.Evento;
import com.eventhub.api.dto.evento.EventoRequest;
import com.eventhub.api.dto.evento.EventoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface EventoMapper {

    EventoResponse toDTO(Evento evento);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    Evento toEntity(EventoRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromDTO(EventoRequest dto, @MappingTarget Evento entity);
}