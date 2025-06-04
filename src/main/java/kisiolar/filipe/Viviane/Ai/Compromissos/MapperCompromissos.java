package kisiolar.filipe.Viviane.Ai.Compromissos;


import org.mapstruct.*;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperCompromissos {

     CompromissosModel map(DTOCompromissos dtoCompromissos);

     DTOCompromissos map(CompromissosModel compromissosModel);

     @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
     void atualizacao(DTOUpdateCompromissos dto, @MappingTarget CompromissosModel entity);
}
