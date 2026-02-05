package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import jakarta.validation.Valid;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaListasCompromissos;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTORespostaHorariosPorDia;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOUpdateHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia.HorariosPorDiaService;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Seguranca.AuthUtils;
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
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostasListasCompromissoRecorrentes listarcompromissos =
                compromissosRecorrentesService.listarCompromissos(usuarioId);

        if (listarcompromissos.listaCompromissosRecorrentes().isEmpty()){
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
            @PathVariable long compromissoId
    ){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaCompromissoRecorrente dtoCompromissosRecorrentes =
                compromissosRecorrentesService.buscarCompromissoPorId(compromissoId,usuarioId);

        return ResponseEntity.ok(dtoCompromissosRecorrentes);
    }

    @GetMapping("/buscarcompromissopornome/{nome}")
    public ResponseEntity<DTORespostasListasCompromissoRecorrentes> buscarCompromissoPornome(
            @PathVariable String nome
    ){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostasListasCompromissoRecorrentes dtoCompromissosRecorrentes =
                compromissosRecorrentesService.buscarCompromissoPorNome(nome,usuarioId);

        return ResponseEntity.ok(dtoCompromissosRecorrentes);
    }

    @GetMapping("/listarconflitos")
    public ResponseEntity<List<List<DTOSaidaCompromissosRecorrentes>>> listarCompromissosConflitantes(){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        List<List<DTOSaidaCompromissosRecorrentes>> lista =
                compromissosRecorrentesService.listarCompromissosConflitantes(usuarioId);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/criarcompromisso")
    public ResponseEntity<DTORespostaCompromissoRecorrente> criarCompromisso(
            @Valid @RequestBody DTOCreateCompromissosRecorrentes dtoCreateCompromissosRecorrentes
            ,BindingResult resultado){

        long usuarioId = AuthUtils.getIdUsuarioLogado();

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

    @PatchMapping("/alterarcompromisso/{compromissoId}")
    public ResponseEntity<DTORespostaCompromissoRecorrente> alterarCompromisso(
            @PathVariable long compromissoId,
            @Valid @RequestBody DTOUpdateCompromissosRecorrentes updateCompromissosRecorrentes,
            BindingResult resultado
    ){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        if (resultado.hasErrors()) {
            String erros = resultado.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            throw new BadRequestException("Erros na requisição: " + erros);
        }

        DTORespostaCompromissoRecorrente compromissoAlterado =
                compromissosRecorrentesService
                        .alterarCompromissoRecorrente(usuarioId,compromissoId,updateCompromissosRecorrentes);

        return ResponseEntity.ok(compromissoAlterado);
    }

    @PatchMapping("/adicionarhorarionocompromisso/{compromissoRecorrenteId}")
    public ResponseEntity<DTORespostaHorariosPorDia> adicionarHorario(
            @PathVariable Long compromissoRecorrenteId,
            @RequestBody DTOCreateHorariosPorDiaBase horariosPorDia
    ){

        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaHorariosPorDia saidaHorariosPorDia =
                horariosPorDiaService.adicionarHorario(compromissoRecorrenteId,usuarioId,horariosPorDia);

        return ResponseEntity.ok(saidaHorariosPorDia);
    }

    @PatchMapping("/alterarhorariodocompromisso/{compromissoRecorrenteId}/{horarioId}")
    public ResponseEntity<DTORespostaHorariosPorDia> alterarHorario(
            @PathVariable Long compromissoRecorrenteId,
            @PathVariable Long horarioId,
            @RequestBody DTOUpdateHorariosPorDiaBase updateHorariosPorDia
    ) {

        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaHorariosPorDia saidaHorariosPorDia =
                horariosPorDiaService.alterarHorario(
                        compromissoRecorrenteId, horarioId,usuarioId,updateHorariosPorDia);

        return ResponseEntity.ok(saidaHorariosPorDia);
    }

    @DeleteMapping("/deletarhorariodocompromisso/{compromissoRecorrenteId}/{horarioId}")
    public ResponseEntity<Long> deletarHorario(
            @PathVariable Long compromissoRecorrenteId,
            @PathVariable Long horarioId
    ){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        long compromissosDeletados =
                horariosPorDiaService.deletarHorarioPorId(compromissoRecorrenteId,horarioId,usuarioId);

        return ResponseEntity.ok(compromissosDeletados);
    }

    @DeleteMapping("/deletarcompromisso/{compromissoId}")
    public ResponseEntity<Void> deletarcompromisso(@PathVariable long compromissoId){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        compromissosRecorrentesService.deletarCompromissoPorId(compromissoId,usuarioId);

        return ResponseEntity.noContent().build();
    }

}
