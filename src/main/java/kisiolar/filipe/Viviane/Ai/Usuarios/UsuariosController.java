package kisiolar.filipe.Viviane.Ai.Usuarios;

import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Exceptions.TooLargeException;
import kisiolar.filipe.Viviane.Ai.Seguranca.AuthUtils;
import kisiolar.filipe.Viviane.Ai.Usuarios.DTOs.DTOUpdateUsuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;

    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @PatchMapping("/alterardados")
    public ResponseEntity<String> alterarDadosDoUsuario( @RequestBody DTOUpdateUsuario usuario){
        Long userId = AuthUtils.getIdUsuarioLogado();

        usuariosService.alterarUsuario(userId,usuario);

        return ResponseEntity.ok("Usuário Alterado");
    }

    @PatchMapping(value ="/alterarfoto",consumes = "multipart/form-data")
    public ResponseEntity<String> updateProflieImage(@RequestParam("file") MultipartFile profileImg){

        Long userId = AuthUtils.getIdUsuarioLogado();

        final long MAX = 5L * 1024 * 1024; // 5 MB

        if (profileImg.isEmpty()) {
            throw new BadRequestException("Arquivo vazio.");
        }

        if (profileImg.getSize() > MAX){
            throw new TooLargeException("A Imagem deve ser mavor que 5MB");
        }

        String contentType = profileImg.getContentType();
        if (contentType == null || !contentType.matches("image/(png|jpe?g|webp|avif)")) {
            throw new BadRequestException("Formato inválido. Aceito: png, jpg, jpeg, webp, avif.");
        }

        usuariosService.updateProfileImg(userId,profileImg);

        return ResponseEntity.ok("imagem alterada");
    }

    @DeleteMapping("/deletarfoto")
    public ResponseEntity<String> deleteProfileImage(){
        Long userId = AuthUtils.getIdUsuarioLogado();

        usuariosService.deleteProfileImage(userId);

        return ResponseEntity.ok("imagem deletada");
    }

    @DeleteMapping("/deletar")
    private ResponseEntity<String> deletarUsuario(){
        Long userId = AuthUtils.getIdUsuarioLogado();

        usuariosService.deletarUsuario(userId);

        return ResponseEntity.ok("Usuário Deletado");
    }
}
