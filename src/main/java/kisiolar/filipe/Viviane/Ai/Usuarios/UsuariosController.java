package kisiolar.filipe.Viviane.Ai.Usuarios;

import jakarta.validation.Valid;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOCreateUsuario;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOUpdateUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;

    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @PostMapping("/cadastrarUsuario")
    public ResponseEntity<String> cadastrarUsuario(@Valid @RequestBody DTOCreateUsuario usuarioDto,
                                                   BindingResult resultado){

        if (resultado.hasErrors()) {
            String erros = resultado.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            throw new BadRequestException("Erros na requisição: " + erros);
        }

        usuariosService.criarUsuario(usuarioDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário Salvo");
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
