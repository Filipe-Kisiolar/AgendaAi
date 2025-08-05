package kisiolar.filipe.Viviane.Ai.IntegrationAI;

import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.*;
import kisiolar.filipe.Viviane.Ai.Seguranca.AuthUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class CompromissosTools {

    private final CompromissosService compromissosService;

    public CompromissosTools(CompromissosService compromissosService) {
        this.compromissosService = compromissosService;
    }

    @Tool(name = "listar_compromissos",
            description = "listar todos os compromissos de um usuario")
    public String listarCompromissos(){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaListasCompromissos listarcompromissos =
                compromissosService.listarCompromissos(usuarioId);

        return listarcompromissos.toString();
    }

    @Tool(name = "buscar_compromisso_por_id",
            description = "busca um compromisso por id")
    public String buscarCompromissoPorId(long id){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaCompromisso dtoCompromissos = compromissosService.buscarCompromissoPorId(id,usuarioId);

        return dtoCompromissos.toString();
    }

    @Tool(name = "buscar_compromisso_por_nome",
            description = "busca um compromisso por nome ")
    public String buscarCompromissoPorNome(String nome){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaListasCompromissos lista =
                compromissosService.listarCompromissosPorNome(nome,usuarioId);

        return lista.toString();
    }

    @Tool(name = "listar_compromissos_do_dia",
            description = "listar todos os compromissos de um determinado dia")
    public String listarCompromissosDoDia(LocalDate dia){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaListasCompromissos lista = compromissosService.listarCompromissosDoDia(dia, usuarioId);

        return lista.toString();
    }

    @Tool(name = "listar_compromissos_da_semana",
            description = "listar todos os compromissos de uma determinada semana")
    public String listarCompromissosDaSemana(LocalDate dia){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        Map<DayOfWeek,DTORespostaListasCompromissos> lista =
                compromissosService.listarCompromissosDaSemana(dia,usuarioId);

        return lista.toString();
    }

    @Tool(name = "listar_compromissos_conflitantes",
            description = "listar todos os compromissos conflitantes de um usuario")
    public String listarCompromissosConflitantes(){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        List<List<DTOSaidaCompromissos>> lista =
                compromissosService.listarCompromissosConflitantes(usuarioId);

        return lista.toString();
    }

    @Tool(name = "criar_compromisso",
            description = "criar um compromisso")
    public String criarCompromisso(DTOCreateCompromissos dtoCompromissos){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaCompromisso compromissos = compromissosService.criarCompromisso(dtoCompromissos,usuarioId);

        return compromissos.toString();
    }

    @Tool(name = "alterar_compromisso",
            description = "alterar um compromisso")
    public String alterarCompromisso(long id,DTOUpdateCompromissos update){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        DTORespostaCompromisso compromissoAlterado = compromissosService.alterarCompromisso(id,usuarioId,update);

        return compromissoAlterado.toString();
    }

    @Tool(name = "deletar_compromisso",
            description = "deletar um compromisso")
    public String deletarcompromisso(long id){
        long usuarioId = AuthUtils.getIdUsuarioLogado();

        compromissosService.deletarCompromissoPorId(id,usuarioId);

        return "deletado com sucesso";
    }
}
