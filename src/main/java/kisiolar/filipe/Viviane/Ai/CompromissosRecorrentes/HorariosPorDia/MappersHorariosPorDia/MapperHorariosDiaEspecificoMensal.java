package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal.DTOCreateHorariosDiaEspecificoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal.DTOSaidaHorariosDiaEspecificoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal.DTOUpdateHorariosDiaEspecificoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.HorariosDiaEspecificoMensal;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperHorariosDiaEspecificoMensal {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "compromissoRecorrente",ignore = true)
    HorariosDiaEspecificoMensal mapToModel(DTOCreateHorariosDiaEspecificoMensal dtoCreateHorariosDiaEspecificoMensal);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "compromissoRecorrente",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizacao(DTOUpdateHorariosDiaEspecificoMensal dtoUpdateHorariosDiaEspecificoMensal, @MappingTarget HorariosDiaEspecificoMensal entity);

    DTOSaidaHorariosDiaEspecificoMensal mapToDto(HorariosDiaEspecificoMensal entity);
}
