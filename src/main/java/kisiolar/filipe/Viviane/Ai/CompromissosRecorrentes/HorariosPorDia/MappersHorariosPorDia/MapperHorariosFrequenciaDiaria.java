package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaDiaria.DTOCreateHorariosFrequenciaDiaria;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaDiaria.DTOSaidaHorariosFrequenciaDiaria;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaDiaria.DTOUpdateHorariosFrequenciaDiaria;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.HorariosFrequenciaDiaria;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperHorariosFrequenciaDiaria {
    @Mapping(target = "id",ignore = true)
    HorariosFrequenciaDiaria mapToModel(DTOCreateHorariosFrequenciaDiaria dtoCreateHorariosFrequenciaDiaria);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "compromissoRecorrente",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizacao(DTOUpdateHorariosFrequenciaDiaria dtoUpdateHorariosFrequenciaDiaria, @MappingTarget HorariosFrequenciaDiaria entity);

    DTOSaidaHorariosFrequenciaDiaria mapToDto(HorariosFrequenciaDiaria entity);
}
