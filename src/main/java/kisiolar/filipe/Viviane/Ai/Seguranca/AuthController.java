package kisiolar.filipe.Viviane.Ai.Seguranca;

import jakarta.validation.Valid;
import kisiolar.filipe.Viviane.Ai.Messaging.DTOs.DTOEmailRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@RequestBody DTOLogin login){
        String respostaComToken = authService.Autenticacao(login.email(),login.senha());

        return ResponseEntity.ok(respostaComToken);
    }

    @PostMapping("/recuperacaodesenha")
    public ResponseEntity<String> newPasswordRequest(@Valid @RequestBody DTOEmailRequest emailRequest){

        authService.sendPasswordResetEmail(emailRequest.email());

        return ResponseEntity.ok("\"Se o e-mail existir, enviaremos instruções para recuperação de senha.\"\n");
    }

    @PatchMapping("/novasenha")
    public ResponseEntity<String> setNewPassword(
            @Valid @RequestBody PasswordDto newPassword,@RequestParam("token")String token
    ){
        authService.resetPassword(newPassword,token);
        return ResponseEntity.ok("Senha Alterada,por favor faça o login novamente");
    }
}
