package kisiolar.filipe.Viviane.Ai.Seguranca;

import kisiolar.filipe.Viviane.Ai.Exceptions.UsernameOrPasswordInvalidException;
import kisiolar.filipe.Viviane.Ai.Messaging.DTOs.DTONewPasswordRequest;
import kisiolar.filipe.Viviane.Ai.Messaging.Producer.RabbitSender;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosModel;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UsuariosRepository usuariosRepository;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final RabbitSender rabbitSender;

    public AuthService(UsuariosRepository usuariosRepository, AuthenticationManager authenticationManager, TokenService tokenService, RabbitSender rabbitSender) {
        this.usuariosRepository = usuariosRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.rabbitSender = rabbitSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuariosRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("usuário ou senha inválido"));
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

        DTONewPasswordRequest passwordRequest = new DTONewPasswordRequest();


        rabbitSender.sendNewPasswordRequest(passwordRequest);
    }
}
