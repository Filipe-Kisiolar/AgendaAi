package kisiolar.filipe.Viviane.Ai.Compromissos;


import jakarta.validation.Valid;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.*;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Seguranca.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compromissos")
public class CompromissosController {

    @Autowired
    CompromissosService compromissosService;


    @GetMapping("/listarcompromissos")
    public ResponseEntity<?> listarCompromissos(){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaListasCompromissos listarcompromissos =
                compromissosService.listarCompromissos(usuarioId);

        if (listarcompromissos.listaCompromissos().isEmpty()){
            Map<String,Object> resposta = new HashMap<>();
            resposta.put("mensagem","ainda nao a compromissos registrados, para criar um copromisso recorrente entre no link:");
            resposta.put("link","/compromissos/criarcompromisso ");
            return ResponseEntity.status(HttpStatus.OK).body(resposta);
        }else {
            return ResponseEntity.ok(listarcompromissos);
        }
    }

    @GetMapping("/buscarcompromissoporid/{id}")
    public ResponseEntity<DTORespostaCompromisso> buscarCompromissoPorId(
            @PathVariable long id
    ){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaCompromisso dtoCompromissos = compromissosService.buscarCompromissoPorId(id,usuarioId);

        return ResponseEntity.ok(dtoCompromissos);
    }

    @GetMapping("/listarcompromissopornome/{nome}")
    public ResponseEntity<DTORespostaListasCompromissos> buscarCompromissoPorNome(
            @PathVariable String nome
    ){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaListasCompromissos lista =
                compromissosService.listarCompromissosPorNome(nome,usuarioId);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/listarcompromissosdodia/{dia}")
    public ResponseEntity<DTORespostaListasCompromissos> listarCompromissosDoDia(
            @PathVariable("dia") @DateTimeFormat(pattern = "MM-dd-yyyy") LocalDate dia
    ){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaListasCompromissos lista = compromissosService.listarCompromissosDoDia(dia, usuarioId);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("listarcompromissosdasemana/{dia}")
    public ResponseEntity<Map<DayOfWeek,DTORespostaListasCompromissos>> listarCompromissosDaSemana(
            @PathVariable("dia") @DateTimeFormat(pattern = "MM-dd-yyyy") LocalDate dia
    ){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        Map<DayOfWeek,DTORespostaListasCompromissos> lista =
                compromissosService.listarCompromissosDaSemana(dia,usuarioId);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/listarconflitos")
    public ResponseEntity<List<List<DTOSaidaCompromissos>>> listarCompromissosConflitantes(){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        List<List<DTOSaidaCompromissos>> lista =
                compromissosService.listarCompromissosConflitantes(usuarioId);

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/criarcompromisso")
    public ResponseEntity<DTORespostaCompromisso> criarCompromisso(
            @Valid @RequestBody DTOCreateCompromissos dtoCompromissos,
            BindingResult resultado
    ){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        if (resultado.hasErrors()){
            String erros = resultado.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            throw new BadRequestException("Erros na requisição: " + erros);
        }

        DTORespostaCompromisso compromissos = compromissosService.criarCompromisso(dtoCompromissos,usuarioId);

        return ResponseEntity.ok(compromissos);
    }

    @PatchMapping("/alterarcompromisso/{id}")
    public ResponseEntity<DTORespostaCompromisso> alterarCompromisso(
            @PathVariable long id,
            @RequestBody DTOUpdateCompromissos update
    ){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaCompromisso compromissoAlterado = compromissosService.alterarCompromisso(id,usuarioId,update);

        return ResponseEntity.ok(compromissoAlterado);
    }

    @DeleteMapping("/deletarcompromisso/{id}")
    public ResponseEntity<Void> deletarcompromisso(@PathVariable long id){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        compromissosService.deletarCompromissoPorId(id,usuarioId);

        return ResponseEntity.noContent().build();
    }
}