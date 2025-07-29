package kisiolar.filipe.Viviane.Ai.Seguranca;

import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosModel;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
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

    public AuthService(UsuariosRepository usuariosRepository, @Lazy AuthenticationManager authenticationManager, TokenService tokenService) {
        this.usuariosRepository = usuariosRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuariosModel usuariosModel = usuariosRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário ou senha inválido"));

        System.out.println("senha carregada do banco" + usuariosModel.getSenha());

        return usuariosModel;
    }

    public String Autenticacao(String email, String senha){
        UsernamePasswordAuthenticationToken userAndPass =
                new UsernamePasswordAuthenticationToken(email,senha);

        Authentication authenticate = authenticationManager.authenticate(userAndPass);

        UsuariosModel usuario = (UsuariosModel) authenticate.getPrincipal();

        return tokenService.generateToken(usuario);
    }
}
