package kisiolar.filipe.Viviane.Ai.Seguranca;

import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import kisiolar.filipe.Viviane.Ai.Exceptions.UsernameOrPasswordInvalidException;
import kisiolar.filipe.Viviane.Ai.Messaging.Producer.RabbitSender;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOUserResponse;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosModel;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public AuthService(UsuariosService usuariosService, AuthenticationManager authenticationManager, TokenService tokenService, RabbitSender rabbitSender, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.usuariosService = usuariosService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.rabbitSender = rabbitSender;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
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

    public void sendPasswordResetEmail(String userEmail){
        try{
        UsuariosModel user = usuariosService.findUserByEmail(userEmail);
        String rawToken = UUID.randomUUID().toString();

        LocalDateTime createdAt = LocalDateTime.now();

        PasswordResetTokenModel passwordResetToken = new PasswordResetTokenModel();

        passwordResetToken.setUserId(user.getId());
        passwordResetToken.setTokenHash(hashToken(rawToken));
        passwordResetToken.setCreatedAt(createdAt);
        passwordResetToken.setExpiresAt(createdAt.plusMinutes(30));
        passwordResetToken.setUsed(false);

        passwordResetTokenRepository.save(passwordResetToken);

        rabbitSender.sendNewPasswordRequest(rawToken,userEmail);

        }catch (Exception e ){
            throw new ResourceNotFindException("erro ao tentar enviar o email");
        }
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Erro ao gerar hash do token", e);
        }
    }
}
