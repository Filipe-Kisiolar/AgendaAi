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


    @GetMapping("/listarcompromissos")
    public ResponseEntity<?> listarCompromissos(){
        DTORespostaListasCompromissos listarcompromissos = compromissosService.listarCompromissos();
        if (listarcompromissos.getListaCompromissos().isEmpty()){
            Map<String,Object> resposta = new HashMap<>();
            resposta.put("mensagem","ainda nao a compromissos recorrentes para criar um copromisso recorrente entre no link:");
            resposta.put("link","/compromissosrecorrentes/criarcompromisso ");
            return ResponseEntity.status(HttpStatus.OK).body(resposta);
        }else {
            return ResponseEntity.ok(listarcompromissos);
        }
    }

    @GetMapping("/buscarcompromissoporid/{id}")
    public ResponseEntity<DTORespostaCompromisso> buscarCompromissoPorId(@PathVariable long id){
        DTORespostaCompromisso dtoCompromissos = compromissosService.buscarCompromissoPorId(id);

        return ResponseEntity.ok(dtoCompromissos);
    }

    @GetMapping("/listarcompromissopornome/{nome}")
    public ResponseEntity<DTORespostaListasCompromissos> buscarCompromissoPorNome(@PathVariable String nome){
        DTORespostaListasCompromissos lista = compromissosService.listarCompromissosPorNome(nome);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/listarcompromissosdodia/{dia}")
    public ResponseEntity<DTORespostaListasCompromissos> listarCompromissosDoDia(@PathVariable @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
                                                                             LocalDate dia){
        DTORespostaListasCompromissos lista = compromissosService.listarCompromissosDoDia(dia);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("listarcompromissosdasemana/{dia}")
    public ResponseEntity<Map<DayOfWeek,DTORespostaListasCompromissos>> listarCompromissosDaSemana(@PathVariable LocalDate dia){
        Map<DayOfWeek,DTORespostaListasCompromissos> lista = compromissosService.listarCompromissosDaSemana(dia);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/listarconflitos")
    public ResponseEntity<List<List<DTOSaidaCompromissos>>> listarCompromissosConflitantes(){
        List<List<DTOSaidaCompromissos>> lista = compromissosService.listarCompromissosConflitantes();

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/criarcompromisso")
    public ResponseEntity<DTORespostaCompromisso> criarCompromisso(@RequestBody DTOCreateCompromissos dtoCompromissos){
        DTORespostaCompromisso compromissos = compromissosService.criarCompromisso(dtoCompromissos);

        return ResponseEntity.ok(compromissos);
    }

    @PatchMapping("/alterarcompromisso/{id}")
    public ResponseEntity<DTORespostaCompromisso> alterarCompromisso(@PathVariable long id, @RequestBody DTOUpdateCompromissos update){
        DTORespostaCompromisso compromissoAlterado = compromissosService.alterarCompromisso(id,update);

        return ResponseEntity.ok(compromissoAlterado);
    }

    @DeleteMapping("/deletarcompromisso/{id}")
    public ResponseEntity<Void> deletarcompromisso(@PathVariable long id){
        compromissosService.deletarCompromissoPorId(id);

        return ResponseEntity.noContent().build();
    }
}
