package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOSaidaCompromissos;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDia;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOSaidaHorariosPorDia;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOUpdateHorariosPorDia;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/compromissosrecorrentes")
public class CompromissosRecorrentesController {

    @Autowired
    private CompromissosRecorrentesService compromissosRecorrentesService;

    @Autowired
    private HorariosPorDiaService horariosPorDiaService;

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

    @GetMapping("/listarconflitos")
    public ResponseEntity<List<List<DTOSaidaCompromissosRecorrentes>>> listarCompromissosConflitantes(){
        List<List<DTOSaidaCompromissosRecorrentes>> lista = compromissosRecorrentesService.listarCompromissosConflitantes();
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/criarcompromisso")
    public ResponseEntity<DTORespostaCompromissoRecorrente> criarCompromisso(@RequestBody DTOCreateCompromissosRecorrentes dtoCreateCompromissosRecorrentes){
        DTORespostaCompromissoRecorrente compromissoCriado = compromissosRecorrentesService.criarCompromissoRecorrente(dtoCreateCompromissosRecorrentes);

        return ResponseEntity.ok(compromissoCriado);
    }

    @PatchMapping("/alterarcompromisso/{id}")
    public ResponseEntity<DTORespostaCompromissoRecorrente> alterarCompromisso(@PathVariable long id, @RequestBody DTOUpdateCompromissosRecorrentes updateCompromissosRecorrentes){
        DTORespostaCompromissoRecorrente compromissoAlterado = compromissosRecorrentesService.alterarCompromissoRecorrente(id,updateCompromissosRecorrentes);

        return ResponseEntity.ok(compromissoAlterado);
    }

    @PatchMapping("/adicionarhorarionocompromisso/{id}")
    public ResponseEntity<DTOSaidaHorariosPorDia> adicionarHorario(@PathVariable Long compromissoRecorrenteId,
                                                                   @RequestBody DTOCreateHorariosPorDia horariosPorDia){
        DTOSaidaHorariosPorDia saidaHorariosPorDia = horariosPorDiaService.adicionarHorario(compromissoRecorrenteId,horariosPorDia);

        return ResponseEntity.ok(saidaHorariosPorDia);
    }

    @PatchMapping("/alterarhorariodocompromisso/{compromissoid}/{idhorario}")
    public ResponseEntity<DTOSaidaHorariosPorDia> alterarHorario(
            @PathVariable("compromissoid") Long compromissoRecorrenteId,
            @PathVariable("idhorario") Long horarioId,
            @RequestBody DTOUpdateHorariosPorDia updateHorariosPorDia) {

        DTOSaidaHorariosPorDia saidaHorariosPorDia = horariosPorDiaService.alterarHorario(compromissoRecorrenteId, horarioId, updateHorariosPorDia);
        return ResponseEntity.ok(saidaHorariosPorDia);
    }

    @DeleteMapping("/deletarhorariodocompromisso/{id")
    public ResponseEntity<List<DTOSaidaCompromissos>> deletarHorario(@PathVariable Long compromissoRecorrenteId,
                                               @PathVariable Long horarioId){
        List<DTOSaidaCompromissos> compromissosDeletados = horariosPorDiaService.deletarHorarioPorId(compromissoRecorrenteId,horarioId);

        return ResponseEntity.ok(compromissosDeletados);
    }

    @DeleteMapping("/deletarcompromisso/{compromissoid}/{horarioid}")
    public ResponseEntity<Void> deletarcompromisso(@PathVariable long id){
        compromissosRecorrentesService.deletarCompromissoPorId(id);

        return ResponseEntity.noContent().build();
    }

}
