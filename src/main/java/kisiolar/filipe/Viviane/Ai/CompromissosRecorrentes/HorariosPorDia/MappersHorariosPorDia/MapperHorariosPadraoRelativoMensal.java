package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal.DTOCreateHorariosPadraoRelativoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal.DTOSaidaHorariosPadraoRelativoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal.DTOUpdateHorariosPadraoRelativoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPadraoRelativoMensal;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperHorariosPadraoRelativoMensal {

    @Mapping(target = "id",ignore = true)
    HorariosPadraoRelativoMensal mapToModel(DTOCreateHorariosPadraoRelativoMensal dtoCreateHorariosPadraoRelativoMensal);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "compromissoRecorrente",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizacao(DTOUpdateHorariosPadraoRelativoMensal dtoUpdateHorariosPadraoRelativoMensal, @MappingTarget HorariosPadraoRelativoMensal entity);

    DTOSaidaHorariosPadraoRelativoMensal mapToDto(HorariosPadraoRelativoMensal entity);
}
