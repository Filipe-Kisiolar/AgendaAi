package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;


import kisiolar.filipe.Viviane.Ai.Compromissos.MapperCompromissos;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {MapperCompromissos.class},
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MapperCompromissosRecorrentes {


    CompromissosRecorrentesModel map(DTOCompromissosRecorrentes dtoCompromissosRecorrentes);

    DTOCompromissosRecorrentes map(CompromissosRecorrentesModel compromissosRecorrentesModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atulializacao(DTOUpdateCompromissosRecorrentes dto, @MappingTarget CompromissosRecorrentesModel entity);
}
