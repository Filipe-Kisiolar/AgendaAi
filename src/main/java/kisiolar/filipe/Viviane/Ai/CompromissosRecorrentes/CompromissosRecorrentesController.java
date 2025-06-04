package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/compromissosrecorrentes")
public class CompromissosRecorrentesController {

    @Autowired
    private CompromissosRecorrentesService compromissosRecorrentesService;

    @GetMapping("/listarCompromissos")
    public ResponseEntity<?> listarCompromissos(){
        List<DTOCompromissosRecorrentes> listarcompromissos = compromissosRecorrentesService.listarCompromissos();
        if (listarcompromissos.isEmpty()){
            Map<String,Object> resposta = new HashMap<>();
            resposta.put("mensagem","ainda nao a compromissos recorrentes para criar um copromisso recorrente entre no link:");
            resposta.put("link","/compromissosrecorrentes/criarcompromisso ");
            return ResponseEntity.status(HttpStatus.OK).body(resposta);
        }else {
            return ResponseEntity.ok(listarcompromissos);
        }
    }

    @GetMapping("/buscarcompromissoporid/{id}")
    public ResponseEntity<DTOCompromissosRecorrentes> buscarCompromissoPorId(@PathVariable long id){
        DTOCompromissosRecorrentes dtoCompromissosRecorrentes = compromissosRecorrentesService.buscarCompromissoPorId(id);

        return ResponseEntity.ok(dtoCompromissosRecorrentes);
    }

    @GetMapping("/buscarcompromissopornome/{nome}")
    public ResponseEntity<DTOCompromissosRecorrentes> buscarCompromissoPornome(@PathVariable String nome){
        DTOCompromissosRecorrentes dtoCompromissosRecorrentes = compromissosRecorrentesService.buscarCompromissoPorNome(nome);

        return ResponseEntity.ok(dtoCompromissosRecorrentes);
    }

    @GetMapping("/listarcompromissosdiadasemana/{diadasemana}")
    public ResponseEntity<List<DTOCompromissosRecorrentes>> listarCompromissosPorDiaDaSemana(@PathVariable DayOfWeek dia){
        List<DTOCompromissosRecorrentes> lista = compromissosRecorrentesService.buscarCompromissoPorDiaDaSemana(dia);

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/criarcompromisso")
    public ResponseEntity<DTOCompromissosRecorrentes> criarCompromisso(@RequestBody DTOCompromissosRecorrentes dtoCompromissosRecorrentes){
        compromissosRecorrentesService.criarCompromisso(dtoCompromissosRecorrentes);

        return ResponseEntity.ok(dtoCompromissosRecorrentes);
    }

    @PatchMapping("/alterarcompromisso/{id}")
    public ResponseEntity<DTOCompromissosRecorrentes> alterarCompromisso(@PathVariable long id,@RequestBody DTOUpdateCompromissosRecorrentes updateCompromissosRecorrentes){
        DTOCompromissosRecorrentes compromissoAlterado = compromissosRecorrentesService.alterarCompromisso(id,updateCompromissosRecorrentes);

        return ResponseEntity.ok(compromissoAlterado);
    }

    @DeleteMapping("/deletarcompromisso/{id}")
    public ResponseEntity<Void> deletarcompromisso(@PathVariable long id){
        compromissosRecorrentesService.deletarCompromissoPorId(id);

        return ResponseEntity.noContent().build();
    }

}
