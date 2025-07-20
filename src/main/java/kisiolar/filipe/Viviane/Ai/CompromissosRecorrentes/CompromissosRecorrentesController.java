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

    @GetMapping("/listarcompromissos/{usuarioId}")
    public ResponseEntity<?> listarCompromissos(@PathVariable long usuarioId){
        DTORespostasListasCompromissoRecorrentes listarcompromissos =
                compromissosRecorrentesService.listarCompromissos(usuarioId);

        if (listarcompromissos.getListaCompromissosRecorrentes().isEmpty()){
            Map<String,Object> resposta = new HashMap<>();
            resposta.put("mensagem","ainda nao a compromissos recorrentes para criar um copromisso recorrente entre no link:");
            resposta.put("link","/compromissosrecorrentes/criarcompromisso ");
            return ResponseEntity.status(HttpStatus.OK).body(resposta);
        }else {
            return ResponseEntity.ok(listarcompromissos);
        }
    }

    @GetMapping("/buscarcompromissoporid/{compromissoId}")
    public ResponseEntity<DTORespostaCompromissoRecorrente> buscarCompromissoPorId(
            @PathVariable long compromissoId,@PathVariable long usuarioId
    ){
        DTORespostaCompromissoRecorrente dtoCompromissosRecorrentes =
                compromissosRecorrentesService.buscarCompromissoPorId(compromissoId,usuarioId);

        return ResponseEntity.ok(dtoCompromissosRecorrentes);
    }

    @GetMapping("/buscarcompromissopornome/{usuarioId}/{nome}")
    public ResponseEntity<DTORespostaCompromissoRecorrente> buscarCompromissoPornome(
            @PathVariable String nome,@PathVariable long usuarioId
    ){
        DTORespostaCompromissoRecorrente dtoCompromissosRecorrentes =
                compromissosRecorrentesService.buscarCompromissoPorNome(nome,usuarioId);

        return ResponseEntity.ok(dtoCompromissosRecorrentes);
    }

    @GetMapping("/listarconflitos/{usuarioId}")
    public ResponseEntity<List<List<DTOSaidaCompromissosRecorrentes>>> listarCompromissosConflitantes(@PathVariable long usuarioId){
        List<List<DTOSaidaCompromissosRecorrentes>> lista =
                compromissosRecorrentesService.listarCompromissosConflitantes(usuarioId);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/criarcompromisso/{usuarioId}")
    public ResponseEntity<DTORespostaCompromissoRecorrente> criarCompromisso(
            @PathVariable long usuarioId,
            @Valid @RequestBody DTOCreateCompromissosRecorrentes dtoCreateCompromissosRecorrentes
            ,BindingResult resultado){

        if (resultado.hasErrors()) {
            String erros = resultado.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            throw new BadRequestException("Erros na requisição: " + erros);
        }

        DTORespostaCompromissoRecorrente compromissoCriado =
                compromissosRecorrentesService.criarCompromissoRecorrente(usuarioId,dtoCreateCompromissosRecorrentes);

        return ResponseEntity.ok(compromissoCriado);
    }

    @PatchMapping("/alterarcompromisso/{usuarioId}/{compromissoId}")
    public ResponseEntity<DTORespostaCompromissoRecorrente> alterarCompromisso(
            @PathVariable long usuarioId,
            @PathVariable long compromissoId,@Valid @RequestBody DTOUpdateCompromissosRecorrentes updateCompromissosRecorrentes
            ,BindingResult resultado
    ){
        if (resultado.hasErrors()) {
            String erros = resultado.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            throw new BadRequestException("Erros na requisição: " + erros);
        }

        DTORespostaCompromissoRecorrente compromissoAlterado =
                compromissosRecorrentesService
                        .alterarCompromissoRecorrente(compromissoId,usuarioId,updateCompromissosRecorrentes);

        return ResponseEntity.ok(compromissoAlterado);
    }

    @PatchMapping("/adicionarhorarionocompromisso//{usuarioId}/{compromissoRecorrenteId}")
    public ResponseEntity<DTORespostaHorariosPorDia> adicionarHorario(
            @PathVariable long usuarioId,
            @PathVariable Long compromissoRecorrenteId,
            @RequestBody DTOCreateHorariosPorDiaBase horariosPorDia){
        DTORespostaHorariosPorDia saidaHorariosPorDia = horariosPorDiaService.adicionarHorario(compromissoRecorrenteId,horariosPorDia);

        return ResponseEntity.ok(saidaHorariosPorDia);
    }

    @PatchMapping("/alterarhorariodocompromisso/{usuarioId}/{compromissoRecorrenteId}/{horarioId}")
    public ResponseEntity<DTORespostaHorariosPorDia> alterarHorario(
            @PathVariable long usuarioId,
            @PathVariable Long compromissoRecorrenteId,
            @PathVariable Long horarioId,
            @RequestBody DTOUpdateHorariosPorDiaBase updateHorariosPorDia) {

        DTORespostaHorariosPorDia saidaHorariosPorDia = horariosPorDiaService.alterarHorario(compromissoRecorrenteId, horarioId, updateHorariosPorDia);
        return ResponseEntity.ok(saidaHorariosPorDia);
    }

    @DeleteMapping("/deletarhorariodocompromisso/{usuarioId}/{compromissoRecorrenteId}/{horarioId}")
    public ResponseEntity<Long> deletarHorario(
            @PathVariable long usuarioId,
            @PathVariable Long compromissoRecorrenteId,
            @PathVariable Long horarioId){
        long compromissosDeletados = horariosPorDiaService.deletarHorarioPorId(compromissoRecorrenteId,horarioId);

        return ResponseEntity.ok(compromissosDeletados);
    }

    @DeleteMapping("/deletarcompromisso/{usuarioId}/{compromissoId}")
    public ResponseEntity<Void> deletarcompromisso(@PathVariable long usuarioId,@PathVariable long compromissoId){
        compromissosRecorrentesService.deletarCompromissoPorId(compromissoId,usuarioId);

        return ResponseEntity.noContent().build();
    }

}
