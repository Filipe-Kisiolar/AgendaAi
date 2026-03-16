package kisiolar.filipe.Viviane.Ai.Seguranca;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import kisiolar.filipe.Viviane.Ai.Exceptions.UsernameOrPasswordInvalidException;
import kisiolar.filipe.Viviane.Ai.Messaging.Producer.RabbitSender;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosModel;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class AuthService {

    private final UsuariosService usuariosService;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final RabbitSender rabbitSender;

    public AuthService(UsuariosService usuariosService, AuthenticationManager authenticationManager, TokenService tokenService, RabbitSender rabbitSender) {
        this.usuariosService = usuariosService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.rabbitSender = rabbitSender;
    }

    public String Autenticacao(String email, String senha){
        try {
            UsernamePasswordAuthenticationToken userAndPass =
                    new UsernamePasswordAuthenticationToken(email,senha);

            Authentication authenticate = authenticationManager.authenticate(userAndPass);

            UsuariosModel usuario = (UsuariosModel) authenticate.getPrincipal();

            return tokenService.generateToken(usuario);
        }catch (BadCredentialsException exception){
            throw new UsernameOrPasswordInvalidException("usuário ou senha inválido");
        }

    }

    @Transactional
    public void sendPasswordResetEmail(String userEmail){
        try{
        UsuariosModel user = usuariosService.findUserByEmail(userEmail);

        String rawToken = tokenService.createPasswordResetToken(user.getId());

        String passwordResetpath = "/auth/novasenha";

        rabbitSender.sendNewPasswordRequest(rawToken,userEmail, passwordResetpath);

        }catch (Exception e ){
            throw new ResourceNotFindException("erro ao tentar enviar o email");
        }
    }

    @Transactional
    public void resetPassword(PasswordDto passwordDto,String token){
        Long userId = tokenService.validatePasswordResetToken(token);

        usuariosService.resetPassword(userId,passwordDto.password());
    }
}
