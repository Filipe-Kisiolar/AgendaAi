package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;


import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOCreateCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.MapperCompromissos;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.DTOCreateCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.DTOSaidaCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.DTOUpdateCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.HorariosPorDiaModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia.HelperMapperHorariosPorDia;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        uses = {HelperMapperHorariosPorDia.class, MapperCompromissos.class},
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperCompromissosRecorrentes {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "horariosPorDia", ignore = true)
    @Mapping(target = "compromissosGerados",ignore = true)
    @Mapping(target = "usuario",ignore = true)
    CompromissosRecorrentesModel mapToModel(DTOCreateCompromissosRecorrentes dto);

    @Mapping(source = "horariosPorDia", target = "horariosPorDia")
    @Mapping(source = "compromissosGerados", target = "compromissosGerados")
    DTOSaidaCompromissosRecorrentes  mapToDto(CompromissosRecorrentesModel entity);

    //mapToModel para gerar um compromisso a partir de um compromisso recorrente
    @Mapping(target = "inicio", source = "inicioGerado")
    @Mapping(target = "fim", source = "fimGerado")
    @Mapping(target = "compromissoRecorrenteId", source = "compromissosRecorrentesModel.id")
    DTOCreateCompromissos mapGerarCompromisso(CompromissosRecorrentesModel compromissosRecorrentesModel, LocalDateTime inicioGerado
                                                ,LocalDateTime fimGerado);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "modoDeRecorrencia",ignore = true)
    @Mapping(target = "horariosPorDia",ignore = true)
    @Mapping(target = "compromissosGerados",ignore = true)
    @Mapping(target = "usuario",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizacao(DTOUpdateCompromissosRecorrentes dto, @MappingTarget CompromissosRecorrentesModel entity);

}