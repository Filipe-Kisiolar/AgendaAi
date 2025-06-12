package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.DTOCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.DTORespostaCompromissoRecorrente;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.DTORespostasListasCompromissoRecorrentes;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.DTOUpdateCompromissosRecorrentes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/compromissosrecorrentes")
public class CompromissosRecorrentesController {

    @Autowired
    private CompromissosRecorrentesService compromissosRecorrentesService;

    @GetMapping("/listarcompromissos")
    public ResponseEntity<?> listarCompromissos(){
        DTORespostasListasCompromissoRecorrentes listarcompromissos = compromissosRecorrentesService.listarCompromissos();
        if (listarcompromissos.getListaCompromissosRecorrentes().isEmpty()){
            Map<String,Object> resposta = new HashMap<>();
            resposta.put("mensagem","ainda nao a compromissos recorrentes para criar um copromisso recorrente entre no link:");
            resposta.put("link","/compromissosrecorrentes/criarcompromisso ");
            return ResponseEntity.status(HttpStatus.OK).body(resposta);
        }else {
            return ResponseEntity.ok(listarcompromissos);
        }
    }

    @GetMapping("/buscarcompromissoporid/{id}")
    public ResponseEntity<DTORespostaCompromissoRecorrente> buscarCompromissoPorId(@PathVariable long id){
        DTORespostaCompromissoRecorrente dtoCompromissosRecorrentes = compromissosRecorrentesService.buscarCompromissoPorId(id);

        return ResponseEntity.ok(dtoCompromissosRecorrentes);
    }

    @GetMapping("/buscarcompromissopornome/{nome}")
    public ResponseEntity<DTORespostaCompromissoRecorrente> buscarCompromissoPornome(@PathVariable String nome){
        DTORespostaCompromissoRecorrente dtoCompromissosRecorrentes = compromissosRecorrentesService.buscarCompromissoPorNome(nome);

        return ResponseEntity.ok(dtoCompromissosRecorrentes);
    }

    @GetMapping("/listarcompromissosdiadasemana/{diadasemana}")
    public ResponseEntity<DTORespostasListasCompromissoRecorrentes> listarCompromissosPorDiaDaSemana(@PathVariable DayOfWeek diadasemana){
        DTORespostasListasCompromissoRecorrentes lista = compromissosRecorrentesService.buscarCompromissoPorDiaDaSemana(diadasemana);

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/listarconflitos")
    public ResponseEntity<List<List<DTOCompromissosRecorrentes>>> listarCompromissosConflitantes(){
        List<List<DTOCompromissosRecorrentes>> lista = compromissosRecorrentesService.listarCompromissosConflitantes();
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/criarcompromisso")
    public ResponseEntity<DTORespostaCompromissoRecorrente> criarCompromisso(@RequestBody DTOCompromissosRecorrentes dtoCompromissosRecorrentes){
        DTORespostaCompromissoRecorrente compromissoCriado = compromissosRecorrentesService.criarCompromisso(dtoCompromissosRecorrentes);

        return ResponseEntity.ok(compromissoCriado);
    }

    @PatchMapping("/alterarcompromisso/{id}")
    public ResponseEntity<DTORespostaCompromissoRecorrente> alterarCompromisso(@PathVariable long id, @RequestBody DTOUpdateCompromissosRecorrentes updateCompromissosRecorrentes){
        DTORespostaCompromissoRecorrente compromissoAlterado = compromissosRecorrentesService.alterarCompromisso(id,updateCompromissosRecorrentes);

        return ResponseEntity.ok(compromissoAlterado);
    }

    @DeleteMapping("/deletarcompromisso/{id}")
    public ResponseEntity<Void> deletarcompromisso(@PathVariable long id){
        compromissosRecorrentesService.deletarCompromissoPorId(id);

        return ResponseEntity.noContent().build();
    }

}
