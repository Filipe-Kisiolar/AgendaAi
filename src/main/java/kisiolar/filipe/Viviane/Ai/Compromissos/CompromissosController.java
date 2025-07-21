package kisiolar.filipe.Viviane.Ai.Compromissos;


import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/compromissos")
public class CompromissosController {

    @Autowired
    CompromissosService compromissosService;


    @GetMapping("/listarcompromissos/{usuarioId}")
    public ResponseEntity<?> listarCompromissos(@PathVariable long usuarioId){
        DTORespostaListasCompromissos listarcompromissos =
                compromissosService.listarCompromissos(usuarioId);

        if (listarcompromissos.getListaCompromissos().isEmpty()){
            Map<String,Object> resposta = new HashMap<>();
            resposta.put("mensagem","ainda nao a compromissos registrados, para criar um copromisso recorrente entre no link:");
            resposta.put("link","/compromissos/criarcompromisso ");
            return ResponseEntity.status(HttpStatus.OK).body(resposta);
        }else {
            return ResponseEntity.ok(listarcompromissos);
        }
    }

    @GetMapping("/buscarcompromissoporid/{usuarioId}/{id}")
    public ResponseEntity<DTORespostaCompromisso> buscarCompromissoPorId(
            @PathVariable long id,@PathVariable long usuarioId
    ){
        DTORespostaCompromisso dtoCompromissos = compromissosService.buscarCompromissoPorId(id,usuarioId);

        return ResponseEntity.ok(dtoCompromissos);
    }

    @GetMapping("/listarcompromissopornome/{usuarioId}/{nome}")
    public ResponseEntity<DTORespostaListasCompromissos> buscarCompromissoPorNome(
            @PathVariable String nome,@PathVariable long usuarioId
    ){
        DTORespostaListasCompromissos lista =
                compromissosService.listarCompromissosPorNome(nome,usuarioId);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/listarcompromissosdodia/{usuarioId}/{dia}")
    public ResponseEntity<DTORespostaListasCompromissos> listarCompromissosDoDia(
            @PathVariable @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate dia,
            @PathVariable long usuarioId
    ){
        DTORespostaListasCompromissos lista = compromissosService.listarCompromissosDoDia(dia, usuarioId);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("listarcompromissosdasemana/{usuarioId}/{dia}")
    public ResponseEntity<Map<DayOfWeek,DTORespostaListasCompromissos>> listarCompromissosDaSemana(
            @PathVariable LocalDate dia,@PathVariable long usuarioId
    ){
        Map<DayOfWeek,DTORespostaListasCompromissos> lista =
                compromissosService.listarCompromissosDaSemana(dia,usuarioId);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/listarconflitos/{usuarioId}")
    public ResponseEntity<List<List<DTOSaidaCompromissos>>> listarCompromissosConflitantes(
            @PathVariable long usuarioId
    ){
        List<List<DTOSaidaCompromissos>> lista =
                compromissosService.listarCompromissosConflitantes(usuarioId);

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/criarcompromisso/{usuarioId}/{usuarioId}")
    public ResponseEntity<DTORespostaCompromisso> criarCompromisso(
            @RequestBody DTOCreateCompromissos dtoCompromissos,
            @PathVariable long usuarioId
    ){
        DTORespostaCompromisso compromissos = compromissosService.criarCompromisso(dtoCompromissos,usuarioId);

        return ResponseEntity.ok(compromissos);
    }

    @PatchMapping("/alterarcompromisso/{usuarioId}/{id}")
    public ResponseEntity<DTORespostaCompromisso> alterarCompromisso(
            @PathVariable long id, @PathVariable long usuarioId,
            @RequestBody DTOUpdateCompromissos update){
        DTORespostaCompromisso compromissoAlterado = compromissosService.alterarCompromisso(id,usuarioId,update);

        return ResponseEntity.ok(compromissoAlterado);
    }

    @DeleteMapping("/deletarcompromisso/{id}")
    public ResponseEntity<Void> deletarcompromisso(@PathVariable long id){
        compromissosService.deletarCompromissoPorId(id);

        return ResponseEntity.noContent().build();
    }
}
