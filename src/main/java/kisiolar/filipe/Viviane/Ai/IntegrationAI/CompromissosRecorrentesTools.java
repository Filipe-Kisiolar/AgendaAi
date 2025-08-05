package kisiolar.filipe.Viviane.Ai.IntegrationAI;

import jakarta.validation.Valid;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesService;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTORespostaHorariosPorDia;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOUpdateHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia.HorariosPorDiaService;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Seguranca.AuthUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompromissosRecorrentesTools {

    private final CompromissosRecorrentesService compromissosRecorrentesService;

    private final HorariosPorDiaService horariosPorDiaService;

    public CompromissosRecorrentesTools(CompromissosRecorrentesService compromissosRecorrentesService, HorariosPorDiaService horariosPorDiaService) {
        this.compromissosRecorrentesService = compromissosRecorrentesService;
        this.horariosPorDiaService = horariosPorDiaService;
    }

    @Tool(name = "listar_compromissos_recorrentes",
            description = "listar todos os compromissos recorrentes de um usuario")
    public String listarCompromissos(){

        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostasListasCompromissoRecorrentes listarcompromissos =
                compromissosRecorrentesService.listarCompromissos(usuarioId);

        return listarcompromissos.toString();
    }

    @Tool(name = "buscar_compromisso_recorrente_por_id",
            description = "busca um compromisso recorrente por id")
    public String buscarCompromissoPorId(long compromissoId){

        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaCompromissoRecorrente dtoCompromissosRecorrentes =
                compromissosRecorrentesService.buscarCompromissoPorId(compromissoId,usuarioId);

        return dtoCompromissosRecorrentes.toString();
    }

    @Tool(name = "buscar_compromisso_recorrentes_por_nome",
            description = "busca um compromisso recorrente por nome ")
    public String buscarCompromissoPornome(String nome){

        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaCompromissoRecorrente dtoCompromissosRecorrentes =
                compromissosRecorrentesService.buscarCompromissoPorNome(nome,usuarioId);

        return dtoCompromissosRecorrentes.toString();
    }

    @Tool(name = "listar_compromissos_recorrentes_conflitantes",
            description = "listar todos os compromissos recorrentes conflitantes de um usuario")
    public String listarCompromissosConflitantes(){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        List<List<DTOSaidaCompromissosRecorrentes>> lista =
                compromissosRecorrentesService.listarCompromissosConflitantes(usuarioId);
        return lista.toString();
    }

    @Tool(name = "criar_compromisso_recorrente ",
            description = "cria um compromisso recorrente")
    public String criarCompromisso(
            @Valid  DTOCreateCompromissosRecorrentes dtoCreateCompromissosRecorrentes, BindingResult resultado
    ){

        long usuarioId = AuthUtils.getIdUsuarioLogado();

        if (resultado.hasErrors()) {
            String erros = resultado.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            throw new BadRequestException("Erros na requisição: " + erros);
        }

        DTORespostaCompromissoRecorrente compromissoCriado =
                compromissosRecorrentesService.criarCompromissoRecorrente(usuarioId,dtoCreateCompromissosRecorrentes);

        return compromissoCriado.toString();
    }

    @Tool(name = "altera_compromisso_recorrente ",
            description = "altera um compromisso recorrente")
    public String alterarCompromisso(
            long compromissoId,
            @Valid  DTOUpdateCompromissosRecorrentes updateCompromissosRecorrentes,
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
                        .alterarCompromissoRecorrente(compromissoId,usuarioId,updateCompromissosRecorrentes);

        return compromissoAlterado.toString();
    }

    @Tool(name = "adiciona_um_horario_a_um_compromisso_recorrente ",
            description = "adiciona um horario a um compromisso recorrente")
    public String adicionarHorario(
            Long compromissoRecorrenteId,
            DTOCreateHorariosPorDiaBase horariosPorDia
    ){

        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaHorariosPorDia saidaHorariosPorDia =
                horariosPorDiaService.adicionarHorario(compromissoRecorrenteId,usuarioId,horariosPorDia);

        return saidaHorariosPorDia.toString();
    }

    @Tool(name = "altera_um_horario ",
         description = "altera um horario atrelado a um compromisso recorrente")
    public String alterarHorario(
             Long compromissoRecorrenteId,
            Long horarioId,
             DTOUpdateHorariosPorDiaBase updateHorariosPorDia
    ) {

        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaHorariosPorDia saidaHorariosPorDia =
                horariosPorDiaService.alterarHorario(
                        compromissoRecorrenteId, horarioId,usuarioId,updateHorariosPorDia);

        return saidaHorariosPorDia.toString();
    }

    @Tool(name = "deleta_um_horario ",
         description = "deleta um horario atrelado a um compromisso recorrente")
    public String deletarHorario(Long compromissoRecorrenteId, Long horarioId){

        long usuarioId = AuthUtils.getIdUsuarioLogado();

        long compromissosDeletados =
                horariosPorDiaService.deletarHorarioPorId(compromissoRecorrenteId,horarioId,usuarioId);

        return Long.toString(compromissosDeletados);
    }

    @Tool(name = "deleta_compromisso_recorrente ",
            description = "deleta um compromisso recorrente")
    public String deletarcompromisso(long compromissoId){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        compromissosRecorrentesService.deletarCompromissoPorId(compromissoId,usuarioId);

        return "apagado com sucesso";
    }

}

