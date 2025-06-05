package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;


import kisiolar.filipe.Viviane.Ai.Compromissos.DTOCreateCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOUpdateCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.MapperCompromissos;
import org.mapstruct.*;

import java.time.LocalDate;

@Mapper(componentModel = "spring",
        uses = MapperCompromissos.class,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperCompromissosRecorrentes {

    CompromissosRecorrentesModel map(DTOCompromissosRecorrentes dtoCompromissosRecorrentes);

    DTOCompromissosRecorrentes map(CompromissosRecorrentesModel compromissosRecorrentesModel);

    //map para gerar um compromisso a partir de um compromisso recorrente
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "dia",source = "dataGerada")
    @Mapping(target = "compromissoRecorrenteId", source = "compromissosRecorrentesModel.id")
    DTOCreateCompromissos mapGerarCompromisso(CompromissosRecorrentesModel compromissosRecorrentesModel, LocalDate dataGerada);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "compromissosGerados", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizacao(DTOUpdateCompromissosRecorrentes dto, @MappingTarget CompromissosRecorrentesModel entity);

    //map para alterar um compromisso a partir de um compromisso recorrente
    @Mapping(target = "diasDaSemana", ignore = true)
    @Mapping(target = "dataInicioRecorrencia", ignore = true)
    @Mapping(target = "dataFimRecorrencia", ignore = true)
    DTOUpdateCompromissos autualicaoDosCompromissosGerados(DTOUpdateCompromissosRecorrentes updateCompromissosRecorrentes);

}

