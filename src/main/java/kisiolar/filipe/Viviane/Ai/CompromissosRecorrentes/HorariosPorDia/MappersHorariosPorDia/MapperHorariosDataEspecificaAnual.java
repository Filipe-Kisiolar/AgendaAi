package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DataEspecificaAnual.DTOCreateHorariosDataEspecificaAnual;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DataEspecificaAnual.DTOSaidaHorariosDataEspecificaAnual;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DataEspecificaAnual.DTOUpdateHorariosDataEspecificaAnual;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.HorariosDataEspecificaAnual;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperHorariosDataEspecificaAnual {

    @Mapping(target = "id",ignore = true)
    HorariosDataEspecificaAnual mapToModel(DTOCreateHorariosDataEspecificaAnual dtoCreateHorariosDataEspecificaAnual);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "compromissoRecorrente",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizacao(DTOUpdateHorariosDataEspecificaAnual dtoUpdateHorariosDataEspecificaAnual, @MappingTarget HorariosDataEspecificaAnual entity);

    DTOSaidaHorariosDataEspecificaAnual mapToDto(HorariosDataEspecificaAnual entity);
}
