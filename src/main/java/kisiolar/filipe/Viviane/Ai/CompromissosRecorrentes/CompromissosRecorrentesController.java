package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import jakarta.validation.Valid;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTORespostaHorariosPorDia;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOUpdateHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia.HorariosPorDiaService;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ResponseEntity<DTORespostaCompromissoRecorrente> criarCompromisso(
            @Valid @RequestBody DTOCreateCompromissosRecorrentes dtoCreateCompromissosRecorrentes
            ,BindingResult resultado){

        if (resultado.hasErrors()) {
            String erros = resultado.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            throw new BadRequestException("Erros na requisição: " + erros);
        }

        DTORespostaCompromissoRecorrente compromissoCriado = compromissosRecorrentesService.criarCompromissoRecorrente(dtoCreateCompromissosRecorrentes);

        return ResponseEntity.ok(compromissoCriado);
    }

    @PatchMapping("/alterarcompromisso/{id}")
    public ResponseEntity<DTORespostaCompromissoRecorrente> alterarCompromisso(
            @PathVariable long id,@Valid @RequestBody DTOUpdateCompromissosRecorrentes updateCompromissosRecorrentes
            ,BindingResult resultado
    ){
        if (resultado.hasErrors()) {
            String erros = resultado.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            throw new BadRequestException("Erros na requisição: " + erros);
        }

        DTORespostaCompromissoRecorrente compromissoAlterado = compromissosRecorrentesService.alterarCompromissoRecorrente(id,updateCompromissosRecorrentes);

        return ResponseEntity.ok(compromissoAlterado);
    }

    @PatchMapping("/adicionarhorarionocompromisso/{compromissoRecorrenteId}")
    public ResponseEntity<DTORespostaHorariosPorDia> adicionarHorario(
            @PathVariable Long compromissoRecorrenteId,
            @RequestBody DTOCreateHorariosPorDiaBase horariosPorDia){
        DTORespostaHorariosPorDia saidaHorariosPorDia = horariosPorDiaService.adicionarHorario(compromissoRecorrenteId,horariosPorDia);

        return ResponseEntity.ok(saidaHorariosPorDia);
    }

    @PatchMapping("/alterarhorariodocompromisso/{compromissoRecorrenteId}/{horarioId}")
    public ResponseEntity<DTORespostaHorariosPorDia> alterarHorario(
            @PathVariable Long compromissoRecorrenteId,
            @PathVariable Long horarioId,
            @RequestBody DTOUpdateHorariosPorDiaBase updateHorariosPorDia) {

        DTORespostaHorariosPorDia saidaHorariosPorDia = horariosPorDiaService.alterarHorario(compromissoRecorrenteId, horarioId, updateHorariosPorDia);
        return ResponseEntity.ok(saidaHorariosPorDia);
    }

    @DeleteMapping("/deletarhorariodocompromisso/{compromissoRecorrenteId}/{horarioId}")
    public ResponseEntity<Long> deletarHorario(
            @PathVariable Long compromissoRecorrenteId,
            @PathVariable Long horarioId){
        long compromissosDeletados = horariosPorDiaService.deletarHorarioPorId(compromissoRecorrenteId,horarioId);

        return ResponseEntity.ok(compromissosDeletados);
    }

    @DeleteMapping("/deletarcompromisso/{id}")
    public ResponseEntity<Void> deletarcompromisso(@PathVariable long id){
        compromissosRecorrentesService.deletarCompromissoPorId(id);

        return ResponseEntity.noContent().build();
    }

}
