package kisiolar.filipe.Viviane.Ai.Compromissos;


import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.MapperCompromissosRecorrentes;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {MapperCompromissosRecorrentes.class},
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperCompromissos {

     @Mapping(target = "compromissoRecorrente", ignore = true)
     CompromissosModel map(DTOCompromissos dtoCompromissos);

     @Mapping(target = "compromissosRecorrente", ignore = true)
     DTOCompromissos map(CompromissosModel compromissosModel);

     @Mapping(target = "id", ignore = true)
     @Mapping(target = "compromissoRecorrente", ignore = true)
     @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
     void atualizacao(DTOUpdateCompromissos dto, @MappingTarget CompromissosModel entity);
}
