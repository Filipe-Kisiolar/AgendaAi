package kisiolar.filipe.Viviane.Ai.Seguranca;

import jakarta.validation.Valid;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOCreateUsuario;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuariosService usuariosService;

    private final AuthService authService;

    public AuthController(UsuariosService usuariosService, AuthService authService) {
        this.usuariosService = usuariosService;
        this.authService = authService;
    }

    @PostMapping("/cadastro")
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

    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@RequestBody DTOLogin login){
        String respostaComToken = authService.Autenticacao(login.email(),login.senha());

        return ResponseEntity.ok(respostaComToken);
    }

    @PatchMapping("/recuperacaodesenha")
    public ResponseEntity<String> newPasswordRequest(@RequestBody String userEmail){

        authService.sendPasswordResetEmail(userEmail);

        return ResponseEntity.ok("\"Se o e-mail existir, enviaremos instruções para recuperação de senha.\"\n");
    }

}
