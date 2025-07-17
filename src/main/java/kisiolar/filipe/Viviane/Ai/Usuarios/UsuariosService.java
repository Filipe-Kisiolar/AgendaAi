package kisiolar.filipe.Viviane.Ai.Usuarios;

import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOCreateUsuario;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOUpdateUsuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;

    private final MapperUsuarios mapperUsuarios;

    public UsuariosService(UsuariosRepository usuariosRepository, MapperUsuarios mapperUsuarios) {
        this.usuariosRepository = usuariosRepository;
        this.mapperUsuarios = mapperUsuarios;
    }

    public void criarUsuario(DTOCreateUsuario dtoCreate){
        UsuariosModel usuario = mapperUsuarios.mapToModel(dtoCreate);

        List<String> errosIdentificados = verificarInformacoes(usuario);

        if (!errosIdentificados.isEmpty()){
            throw new BadRequestException("erros na criação de usuario:\n" + errosIdentificados);
        }

        usuariosRepository.save(usuario);

    }

    public void alterarUsuario(long id,DTOUpdateUsuario dtoUpdate){

        UsuariosModel usuarioParaAtualizar = usuariosRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFindException("usuario não encontrado"));

        mapperUsuarios.atualizacao(dtoUpdate,usuarioParaAtualizar);

        List<String> errosIdentificados = verificarInformacoes(usuarioParaAtualizar);

        if (!errosIdentificados.isEmpty()){
            throw new BadRequestException("erros na alteracao de usuario:\n" + errosIdentificados);
        }

        usuariosRepository.save(usuarioParaAtualizar);
    }

    //todo:fazer delecao de usuarios e encriptar as senhas

    private List<String> verificarInformacoes(UsuariosModel usuario){
        List<String> errosIdentificados = new ArrayList<>();

        if (usuariosRepository.existsByEmail(usuario.getEmail())){
            errosIdentificados.add("Ja existe outra conta com esse email cadastrado" + usuario.getEmail());
        }

        String senha = usuario.getSenha();

        if (senha.isBlank() || senha.length() < 6) {
            errosIdentificados.add("A senha deve ter no mínimo 6 caracteres.");
        }

        if (!senha.matches(".*[A-Z].*")) {
            errosIdentificados.add("A senha deve conter pelo menos uma letra maiúscula.");
        }

        if (!senha.matches(".*\\d.*")) {
            errosIdentificados.add("A senha deve conter pelo menos um número.");
        }

        if (!senha.matches(".*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>.,?/\\\\|~`].*")) {
            errosIdentificados.add("A senha deve conter pelo menos um caractere especial.");
        }

        if (senha.matches(".*\\s.*")) {
            errosIdentificados.add("A senha não pode conter espaços ou quebras de linha.");
        }

        return errosIdentificados;
    }
}
