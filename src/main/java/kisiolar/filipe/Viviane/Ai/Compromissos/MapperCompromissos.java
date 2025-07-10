package kisiolar.filipe.Viviane.Ai.Compromissos;


import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOCreateCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOSaidaCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOUpdateCompromissos;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperCompromissos {
     @Mapping(target = "id",ignore = true)
     @Mapping(target = "compromissoRecorrente", ignore = true)
     CompromissosModel map(DTOCreateCompromissos dto);

     DTOSaidaCompromissos map(CompromissosModel compromissosModel);

     @Mapping(target = "compromissoRecorrente", ignore = true)
     CompromissosModel map(DTOSaidaCompromissos dto);

     @Mapping(target = "id", ignore = true)
     @Mapping(target = "compromissoRecorrente", ignore = true)
     @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
     void atualizacao(DTOUpdateCompromissos dto, @MappingTarget CompromissosModel entity);

     List<DTOSaidaCompromissos> mapToDtoList(List<CompromissosModel> entityList);
}