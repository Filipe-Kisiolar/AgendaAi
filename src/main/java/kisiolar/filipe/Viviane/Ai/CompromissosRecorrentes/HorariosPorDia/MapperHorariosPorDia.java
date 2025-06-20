package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDia;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOSaidaHorariosPorDia;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOUpdateHorariosPorDia;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperHorariosPorDia {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "compromissoRecorrente",ignore = true)
    HorariosPorDiaModel mapToModel(DTOCreateHorariosPorDia dtoCreateHorariosPorDia);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "compromissoRecorrente",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizacao(DTOUpdateHorariosPorDia dtoUpdateHorariosPorDia, @MappingTarget HorariosPorDiaModel entity);

    DTOSaidaHorariosPorDia mapToDto(HorariosPorDiaModel entity);
}