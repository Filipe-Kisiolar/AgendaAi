package kisiolar.filipe.Viviane.Ai.Usuarios;

import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOUpdateUsuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;

    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @PatchMapping("/alterarDados/{id}")
    public ResponseEntity<String> alterarDadosDoUsuario(@PathVariable long id, @RequestBody DTOUpdateUsuario usuario){
        usuariosService.alterarUsuario(id,usuario);

        return ResponseEntity.ok("Usuário Alterado");
    }

    @DeleteMapping("/deletar/{id}")
    private ResponseEntity<String> deletarUsuario(@PathVariable long id){
        usuariosService.deletarUsuario(id);

        return ResponseEntity.ok("Usuário Deletado");
    }

}
