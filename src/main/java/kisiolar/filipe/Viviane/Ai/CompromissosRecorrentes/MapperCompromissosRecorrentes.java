package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;


import kisiolar.filipe.Viviane.Ai.Compromissos.MapperCompromissos;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        uses = {MapperCompromissos.class},
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MapperCompromissosRecorrentes {


    public CompromissosRecorrentesModel map(DTOCompromissosRecorrentes dtoCompromissosRecorrentes);

    public DTOCompromissosRecorrentes map(CompromissosRecorrentesModel compromissosRecorrentesModel);


}
