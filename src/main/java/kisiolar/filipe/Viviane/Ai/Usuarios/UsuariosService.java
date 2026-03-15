package kisiolar.filipe.Viviane.Ai.Usuarios;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import kisiolar.filipe.Viviane.Ai.Messaging.Producer.RabbitSender;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOCreateUsuario;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOUpdateUsuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    private final RabbitSender rabbitSender;

    public UsuariosService(UsuariosRepository usuariosRepository, MapperUsuarios mapperUsuarios, PasswordEncoder passwordEncoder, S3Client s3Client, RabbitSender rabbitSender) {
        this.usuariosRepository = usuariosRepository;
        this.mapperUsuarios = mapperUsuarios;
        this.passwordEncoder = passwordEncoder;
        this.s3Client = s3Client;
        this.rabbitSender = rabbitSender;
    }

    public UsuariosModel findUsuarioById(long usuarioId){
       return usuariosRepository.findById(usuarioId)
               .orElseThrow(() -> new ResourceNotFindException("Usuário Não Encontrado"));
    }

    public UserDetails loadUserByEmail(String userEmail) throws UsernameNotFoundException{
        return usuariosRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("usuário ou senha inválido"));
    }

    public UsuariosModel findUserByEmail(String userEmail) throws UsernameNotFoundException{
        return usuariosRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("usuário ou senha inválido"));
    }

    public DTOUserResponse findUserInformations(long userId){
        UsuariosModel user = usuariosRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFindException("Usuário Não Encontrado"));

        return mapperUsuarios.mapToDto(user);
    }

    @Transactional
    public void criarUsuario(DTOCreateUsuario dtoCreate) {
        List<String> errosIdentificados = verificarInformacoesCriacao(dtoCreate);

        if (!errosIdentificados.isEmpty()){
            throw new BadRequestException("erros na criação de usuario:\n" + errosIdentificados);
        }

        UsuariosModel usuario = mapperUsuarios.mapToModel(dtoCreate);

        String senhaEncriptada = passwordEncoder.encode(usuario.getSenha());

        String phoneNumberE164 = phoneParsing(usuario,usuario.getPhoneNumber());

        usuario.setSenha(senhaEncriptada);

        usuario.setPhoneNumber(phoneNumberE164);

        usuario.setRole(RoleTypeEnum.ROLE_USUARIO);

        UsuariosModel user = usuariosRepository.save(usuario);

        rabbitSender.sendAccountCreatedMessage(user);
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

    @Transactional
    public void alterarUsuario(long id,DTOUpdateUsuario dtoUpdate){

        UsuariosModel usuarioParaAtualizar = usuariosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFindException("usuario não encontrado"));

        List<String> errosIdentificados = verificarInformacoesUpdate(dtoUpdate,id);

        if (!errosIdentificados.isEmpty()){
            throw new BadRequestException("erros na alteracao de usuario:\n" + errosIdentificados);
        }

        String phoneNumberE164 = phoneParsing(usuarioParaAtualizar,dtoUpdate.getPhoneNumber());

        mapperUsuarios.atualizacao(dtoUpdate,usuarioParaAtualizar);

        if(phoneNumberE164 != null && !phoneNumberE164.isBlank()){
            usuarioParaAtualizar.setPhoneNumber(phoneNumberE164);
        }

        usuariosRepository.save(usuarioParaAtualizar);
    }

    @Transactional
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

    private String phoneParsing(UsuariosModel user,String phoneNumber){
        //the user can choose if deliver his number
        if (phoneNumber == null || phoneNumber.isBlank()){
            return null;
        }

        try {
            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber formatedPhone = pnu.parse(phoneNumber,"zz");

            if (!pnu.isValidNumber(formatedPhone)){
                throw new BadRequestException("Número de celular errado ou mal formatado");
            }

            String phoneNumberE164 = pnu.format(formatedPhone, PhoneNumberUtil.PhoneNumberFormat.E164);

            boolean sameNumber = usuariosRepository
                    .findByPhoneNumber(phoneNumberE164)
                    .map(u -> !u.getId().equals(user.getId()))
                    .orElse(false);

            if(sameNumber){
                throw new BadRequestException("Já existe usuário com esse número de celular");
            }

            return phoneNumberE164;
        }catch (Exception e){
            throw new BadRequestException("Número de celular inválido");
        }
    }
}
