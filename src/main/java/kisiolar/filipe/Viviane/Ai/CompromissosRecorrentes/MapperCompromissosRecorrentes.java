package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;


import kisiolar.filipe.Viviane.Ai.Compromissos.MapperCompromissos;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = MapperCompromissos.class,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperCompromissosRecorrentes {

    CompromissosRecorrentesModel map(DTOCompromissosRecorrentes dtoCompromissosRecorrentes);

    DTOCompromissosRecorrentes map(CompromissosRecorrentesModel compromissosRecorrentesModel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "compromissosGerados", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizacao(DTOUpdateCompromissosRecorrentes dto, @MappingTarget CompromissosRecorrentesModel entity);
}

