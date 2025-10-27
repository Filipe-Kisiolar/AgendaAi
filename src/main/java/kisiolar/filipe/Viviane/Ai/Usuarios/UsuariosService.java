package kisiolar.filipe.Viviane.Ai.Usuarios;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOCreateUsuario;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOUpdateUsuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsuariosService {

    @Value("${storage.bucket}")
    private String bucketName;

    private final UsuariosRepository usuariosRepository;

    private final MapperUsuarios mapperUsuarios;

    private final PasswordEncoder passwordEncoder;

    private final S3Client s3Client;

    public UsuariosService(UsuariosRepository usuariosRepository, MapperUsuarios mapperUsuarios, PasswordEncoder passwordEncoder, S3Client s3Client) {
        this.usuariosRepository = usuariosRepository;
        this.mapperUsuarios = mapperUsuarios;
        this.passwordEncoder = passwordEncoder;
        this.s3Client = s3Client;
    }

    public UsuariosModel findUsuarioById(long usuarioId){
       return usuariosRepository.findById(usuarioId)
               .orElseThrow(() -> new ResourceNotFindException("Usuário Não Encontrado"));
    }

    public void criarUsuario(DTOCreateUsuario dtoCreate){
        List<String> errosIdentificados = verificarInformacoesCriacao(dtoCreate);

        if (!errosIdentificados.isEmpty()){
            throw new BadRequestException("erros na criação de usuario:\n" + errosIdentificados);
        }

        UsuariosModel usuario = mapperUsuarios.mapToModel(dtoCreate);

        String senhaEncriptada = passwordEncoder.encode(usuario.getSenha());

        usuario.setSenha(senhaEncriptada);

        usuario.setRole(RoleTypeEnum.ROLE_USUARIO);

        usuariosRepository.save(usuario);
    }

    @Transactional
    public void updateProfileImg(Long userId,MultipartFile imgFile){
        UsuariosModel user = usuariosRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFindException("Usuário Não Encontrado"));

        String fileName = UUID.randomUUID() + "-" + imgFile.getOriginalFilename();

        try{
            //if it returns null no problem
            String oldImageKey = user.getImageKey();

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.putObject(objectRequest,
                    RequestBody.fromByteBuffer(ByteBuffer.wrap(imgFile.getBytes())));

            GetUrlRequest request = GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            String imgUrl = s3Client.utilities().getUrl(request).toString();

            user.setProfileImage(imgUrl);
            user.setImageKey(fileName);

            if(oldImageKey != null && !oldImageKey.isBlank()){
                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(oldImageKey)
                        .build();

                s3Client.deleteObject(deleteRequest);
            }
        }
        catch (Exception e){
            throw new RuntimeException("Imagem não foi criada",e);
        }
    }

    @Transactional
    public void deleteProfileImage(Long userId){
        UsuariosModel user = usuariosRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFindException("Usuário Não Encontrado"));

        String userImg = user.getProfileImage();

        try{
            if(user.getProfileImage() != null && !user.getProfileImage().isBlank()){

                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(user.getImageKey())
                        .build();

                s3Client.deleteObject(deleteRequest);
            }
        }catch (Exception e){
            throw new RuntimeException("Não foi possivel deletar a imagem",e);
        }

        user.setProfileImage(null);
        user.setImageKey(null);
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
