package com.eventhub.api.mapper;

import com.eventhub.api.domain.entity.Ingresso;
import com.eventhub.api.dto.ingresso.IngressoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface IngressoMapper {

    @Mapping(source = "evento.nome", target = "nomeEvento")
    @Mapping(source = "participante.nome", target = "nomeParticipante")
    @Mapping(source = "participante.email", target = "emailParticipante")
    IngressoResponse toDTO(Ingresso ingresso);
}