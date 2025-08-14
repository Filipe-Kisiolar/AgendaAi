package kisiolar.filipe.Viviane.Ai.IntegrationAI;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesService;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.*;
import kisiolar.filipe.Viviane.Ai.Seguranca.AuthUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompromissosRecorrentesTools {

    private final CompromissosRecorrentesService compromissosRecorrentesService;

    public CompromissosRecorrentesTools(CompromissosRecorrentesService compromissosRecorrentesService) {
        this.compromissosRecorrentesService = compromissosRecorrentesService;
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

        DTORespostasListasCompromissoRecorrentes dtoCompromissosRecorrentes =
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
}