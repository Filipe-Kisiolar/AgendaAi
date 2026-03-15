package kisiolar.filipe.Viviane.Ai.Seguranca;

import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomDetailService implements UserDetailsService {

    private final UsuariosService usuariosService;

    public CustomDetailService(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return usuariosService.loadUserByEmail(userEmail);
    }
}
