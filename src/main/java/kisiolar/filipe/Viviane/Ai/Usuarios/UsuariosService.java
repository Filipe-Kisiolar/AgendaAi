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
        List<String> errosIdentificados = verificarInformacoesCriacao(dtoCreate);

        if (!errosIdentificados.isEmpty()){
            throw new BadRequestException("erros na criação de usuario:\n" + errosIdentificados);
        }

        UsuariosModel usuario = mapperUsuarios.mapToModel(dtoCreate);
        usuario.setRole(RoleTypeEnum.USUARIO);

        usuariosRepository.save(usuario);

    }

    public void alterarUsuario(long id,DTOUpdateUsuario dtoUpdate){

        UsuariosModel usuarioParaAtualizar = usuariosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFindException("usuario não encontrado"));

        List<String> errosIdentificados = verificarInformacoesUpdate(dtoUpdate,id);

        if (!errosIdentificados.isEmpty()){
            throw new BadRequestException("erros na alteracao de usuario:\n" + errosIdentificados);
        }

        mapperUsuarios.atualizacao(dtoUpdate,usuarioParaAtualizar);


        usuariosRepository.save(usuarioParaAtualizar);
    }

    public void deletarUsuario(long id){
        if (!usuariosRepository.existsById(id)){
            throw new ResourceNotFindException("usuario nao encontrado");
        }

        usuariosRepository.deleteById(id);
    }

    //todo:encriptar as senhas

    private List<String> verificarInformacoesCriacao(DTOCreateUsuario usuario){
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

    private List<String> verificarInformacoesUpdate(
            DTOUpdateUsuario dtoUpdateUsuario,long id
    ) {
        List<String> errosIdentificados = new ArrayList<>();

        String emailUpdate = dtoUpdateUsuario.getEmail();

        if (emailUpdate != null){
            if (usuariosRepository.usuarioDiferenteTemEmail(emailUpdate, id)){
                errosIdentificados.add("Ja existe outra conta com esse email cadastrado" + emailUpdate);
            }
        }

        String senhaAtualizada = dtoUpdateUsuario.getSenha();

        if (senhaAtualizada != null){
            if (senhaAtualizada.isBlank() || senhaAtualizada.length() < 6) {
                errosIdentificados.add("A senhaAtualizada deve ter no mínimo 6 caracteres.");
            }

            if (!senhaAtualizada.matches(".*[A-Z].*")) {
                errosIdentificados.add("A senhaAtualizada deve conter pelo menos uma letra maiúscula.");
            }

            if (!senhaAtualizada.matches(".*\\d.*")) {
                errosIdentificados.add("A senhaAtualizada deve conter pelo menos um número.");
            }

            if (!senhaAtualizada.matches(".*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>.,?/\\\\|~`].*")) {
                errosIdentificados.add("A senhaAtualizada deve conter pelo menos um caractere especial.");
            }

            if (senhaAtualizada.matches(".*\\s.*")) {
                errosIdentificados.add("A senhaAtualizada não pode conter espaços ou quebras de linha.");
            }
        }

        return errosIdentificados;
    }
}
