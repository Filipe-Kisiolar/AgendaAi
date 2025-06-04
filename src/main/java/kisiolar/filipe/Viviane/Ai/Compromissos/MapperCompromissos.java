package kisiolar.filipe.Viviane.Ai.Compromissos;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperCompromissos {

    public CompromissosModel map(DTOCompromissos dtoCompromissos);

    public DTOCompromissos map(CompromissosModel compromissosModel);
}
