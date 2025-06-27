package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOCreateHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOSaidaHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOUpdateHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosFrequenciaSemanal;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperHorariosFrequenciaSemanal {

    @Mapping(target = "id",ignore = true)
    HorariosFrequenciaSemanal mapToModel(DTOCreateHorariosFrequenciaSemanal dtoCreateHorariosFrequenciaSemanal);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "compromissoRecorrente",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizacao(DTOUpdateHorariosFrequenciaSemanal dtoUpdateHorariosFrequenciaSemanal, @MappingTarget HorariosFrequenciaSemanal entity);

    DTOSaidaHorariosFrequenciaSemanal mapToDto(HorariosFrequenciaSemanal entity);
}
