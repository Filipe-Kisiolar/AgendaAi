package kisiolar.filipe.Viviane.Ai.Usuarios;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuariosService {

    private UsuariosRepository usuariosRepository;

    public UsuariosService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    public List<UsuariosModel> listarUsuarios(){
        return usuariosRepository.findAll();
    }
}
