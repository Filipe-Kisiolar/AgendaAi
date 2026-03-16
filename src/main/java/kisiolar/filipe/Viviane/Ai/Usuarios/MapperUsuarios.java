package kisiolar.filipe.Viviane.Ai.Usuarios;

import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOCreateUsuario;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOUpdateUsuario;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOUserResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperUsuarios {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "role",ignore = true)
    @Mapping(target = "profileImage",ignore = true)
    @Mapping(target = "imageKey",ignore = true)
    @Mapping(target = "authorities",ignore = true)
    @Mapping(target = "compromissos",ignore = true)
    @Mapping(target = "compromissosRecorrentes",ignore = true)
    UsuariosModel mapToModel(DTOCreateUsuario dtoCreateUsuario);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "role",ignore = true)
    @Mapping(target = "profileImage",ignore = true)
    @Mapping(target = "imageKey",ignore = true)
    @Mapping(target = "senha",ignore = true)
    @Mapping(target = "authorities",ignore = true)
    @Mapping(target = "compromissos",ignore = true)
    @Mapping(target = "compromissosRecorrentes",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizacao(DTOUpdateUsuario dtoUpdate, @MappingTarget UsuariosModel usuarios);

    DTOUserResponse mapToDto(UsuariosModel model);
}
