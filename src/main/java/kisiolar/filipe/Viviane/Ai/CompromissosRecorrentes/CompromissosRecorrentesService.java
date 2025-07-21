package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosRepository;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;
import kisiolar.filipe.Viviane.Ai.Compromissos.MapperCompromissos;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTORespostaHorariosPorDia;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DataEspecificaAnual.DTOCreateHorariosDataEspecificaAnual;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal.DTOCreateHorariosDiaEspecificoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaDiaria.DTOCreateHorariosFrequenciaDiaria;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOCreateHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal.DTOCreateHorariosPadraoRelativoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia.HorariosPorDiaService;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosModel;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum.*;

@Service
public class CompromissosRecorrentesService{
    //TODO:usar @transacional quando for fazer requisisao com mapper
    @Autowired
    private CompromissosRecorrentesRepository compromissosRecorrentesRepository;
    @Autowired
    private CompromissosRepository compromissosRepository;
    @Autowired
    private CompromissosService compromissosService;
    @Autowired
    private MapperCompromissosRecorrentes mapperCompromissosRecorrentes;
    @Autowired
    private MapperCompromissos mapperCompromissos;
    @Autowired
    private UsuariosService usuariosService;
    @Autowired
    private HorariosPorDiaService horariosPorDiaService;

    public DTORespostasListasCompromissoRecorrentes listarCompromissos(long usuarioId){
        usuariosService.findUsuarioById(usuarioId);

        LocalDate diaAtual = LocalDate.now();

        List<CompromissosRecorrentesModel> lista =
                compromissosRecorrentesRepository.listAllByUserAfterDate(diaAtual,usuarioId);

        List<DTOSaidaCompromissosRecorrentes> listaDto =lista.stream().
                sorted(Comparator
                        .comparing(CompromissosRecorrentesModel::getDataInicioRecorrencia)).
                map(mapperCompromissosRecorrentes ::mapToDto).
                collect(Collectors.toList());

        return new DTORespostasListasCompromissoRecorrentes(listaDto);
    }

    @Transactional
    public DTORespostaCompromissoRecorrente buscarCompromissoPorId(long id,long usuarioId){
        usuariosService.findUsuarioById(usuarioId);

       CompromissosRecorrentesModel compromissosRecorrentesModel =
               compromissosRecorrentesRepository.findByIdByUser(id,usuarioId)
               .orElseThrow(() -> new ResourceNotFindException("compromisso recorrente não encontrado" +
                       "para esse usuário"));

       DTOSaidaCompromissosRecorrentes dtoCreateCompromissosRecorrentes =
               mapperCompromissosRecorrentes.mapToDto(compromissosRecorrentesModel);

       List<DTOSaidaCompromissosRecorrentes> conflitos =
               verificarConflitos(compromissosRecorrentesModel).stream()
               .map(mapperCompromissosRecorrentes::mapToDto)
               .toList();

       if (conflitos.isEmpty()){
           return new DTORespostaCompromissoRecorrente(dtoCreateCompromissosRecorrentes);
       }else{
           return DTORespostaCompromissoRecorrente.comConflitosRecorrentes(dtoCreateCompromissosRecorrentes,conflitos);
       }
    }

    @Transactional
    public DTORespostaCompromissoRecorrente buscarCompromissoPorNome(String nome,long usuarioId){
        usuariosService.findUsuarioById(usuarioId);

        CompromissosRecorrentesModel compromissosRecorrentesModel =
                compromissosRecorrentesRepository.findByNomeByUser(nome,usuarioId)
                .orElseThrow(() -> new ResourceNotFindException("compromisso recorrente não encontrado" +
                        " para esse compromisso"));

        DTOSaidaCompromissosRecorrentes dtoCreateCompromissosRecorrentes = mapperCompromissosRecorrentes.mapToDto(compromissosRecorrentesModel);

        List<DTOSaidaCompromissosRecorrentes> conflitos = verificarConflitos(compromissosRecorrentesModel).stream()
                .map(mapperCompromissosRecorrentes::mapToDto)
                .toList();

        if (conflitos.isEmpty()){
            return new DTORespostaCompromissoRecorrente(dtoCreateCompromissosRecorrentes);
        }else{
            return DTORespostaCompromissoRecorrente.comConflitosRecorrentes(dtoCreateCompromissosRecorrentes,conflitos);
        }
    }

    public List<List<DTOSaidaCompromissosRecorrentes>> listarCompromissosConflitantes(long usuarioId){
        usuariosService.findUsuarioById(usuarioId);

        LocalDate diaAtual = LocalDate.now();

        List<CompromissosRecorrentesModel> listaTodosCompromissos =
                compromissosRecorrentesRepository.listAllByUserAfterDate(diaAtual,usuarioId);

        List<List<DTOSaidaCompromissosRecorrentes>> gruposDeConflito = compromissosConflitantesLista(listaTodosCompromissos);

        return gruposDeConflito;
    }

    private boolean verificarConformidadeModoDeRecorrencia_Horario(
            ModoDeRecorrenciaEnum modoDeRecorrencia,DTOCreateHorariosPorDiaBase horario){

        return switch (horario){
            case DTOCreateHorariosFrequenciaDiaria diaria when modoDeRecorrencia == FREQUENCIA_DIARIA-> {
                yield true;
            }
            case DTOCreateHorariosFrequenciaSemanal semanal when modoDeRecorrencia == FREQUENCIA_SEMANAL -> {
                yield true;
            }
            case DTOCreateHorariosPadraoRelativoMensal relativoMensal when modoDeRecorrencia == PADRAO_RELATIVO_MENSAL-> {
                yield true;
            }
            case DTOCreateHorariosDiaEspecificoMensal mensal when modoDeRecorrencia == DIA_ESPECIFICO_MENSAL-> {
                yield true;

            }
            case DTOCreateHorariosDataEspecificaAnual anual when modoDeRecorrencia == DATA_ESPECIFICA_ANUAL -> {
                yield true;
            }
            default -> {
                yield false;
            }
        };
    }

    @Transactional
    public DTORespostaCompromissoRecorrente criarCompromissoRecorrente(
            long usuarioId,
            DTOCreateCompromissosRecorrentes dtoCreateCompromissosRecorrentes
    ){

        UsuariosModel usuario = usuariosService.findUsuarioById(usuarioId);

        CompromissosRecorrentesModel compromissoRecorrente = mapperCompromissosRecorrentes.mapToModel(dtoCreateCompromissosRecorrentes);

        compromissoRecorrente.setUsuario(usuario);

        List<String> errosIdentificados = verificarValidadeDasInformacoes(
                compromissoRecorrente,dtoCreateCompromissosRecorrentes.getHorariosPorDia());

        if(!errosIdentificados.isEmpty()){
            throw new BadRequestException("Erros Na Requisicao:\n" + errosIdentificados);
        }

        CompromissosRecorrentesModel compromissoSalvo = compromissosRecorrentesRepository.save(compromissoRecorrente);

        List<DTOCreateHorariosPorDiaBase> listaDosHorarios = dtoCreateCompromissosRecorrentes.getHorariosPorDia();

        List<DTORespostaCompromisso> compromissosGeradosComConflito = new ArrayList<>();

        //cria os horarios por dia pelo HorariosPorDiaService que por sua vez cria ja cria os compromissos
        for (DTOCreateHorariosPorDiaBase horario : listaDosHorarios){
            DTORespostaHorariosPorDia horarioGerado =
                horariosPorDiaService.adicionarHorario(
                        compromissoRecorrente.getId(),compromissoRecorrente.getUsuario().getId(),horario);

            List<DTORespostaCompromisso> compromissosGerados = horarioGerado.compromissosCriados;

            List<DTORespostaCompromisso> geradosComConflito = compromissosGerados.stream()
                    .filter(DTORespostaCompromisso::getExisteConflito).toList();

            compromissosGeradosComConflito.addAll(geradosComConflito);
        }

        List<DTOSaidaCompromissosRecorrentes> conflitosRecorrentes = verificarConflitos(compromissoSalvo).stream()
                .map(mapperCompromissosRecorrentes ::mapToDto)
                .collect(Collectors.toList());

        DTOSaidaCompromissosRecorrentes saidaCompromissosRecorrentes = mapperCompromissosRecorrentes.mapToDto(compromissoSalvo);

        if(conflitosRecorrentes.isEmpty() && compromissosGeradosComConflito.isEmpty()){
            return new DTORespostaCompromissoRecorrente(saidaCompromissosRecorrentes);
        } else if (conflitosRecorrentes.isEmpty()) {
            return DTORespostaCompromissoRecorrente.comConflitosGerados(saidaCompromissosRecorrentes, compromissosGeradosComConflito);
        } else if (compromissosGeradosComConflito.isEmpty()) {
            return DTORespostaCompromissoRecorrente.comConflitosRecorrentes(saidaCompromissosRecorrentes, conflitosRecorrentes);
        } else {
            return new DTORespostaCompromissoRecorrente(saidaCompromissosRecorrentes, conflitosRecorrentes, compromissosGeradosComConflito);
        }
    }

    @Transactional
     public DTORespostaCompromissoRecorrente alterarCompromissoRecorrente(
             long usuarioId,long compromissoId,
             DTOUpdateCompromissosRecorrentes dtoUpdateCompromissosRecorrentes
    ){

        usuariosService.findUsuarioById(usuarioId);

        CompromissosRecorrentesModel compromissosRecorrente =
                compromissosRecorrentesRepository.findByIdByUser(compromissoId,usuarioId)
                .orElseThrow(() -> new ResourceNotFindException("compromisso recorrente não encontrado"));

        Integer intervaloDto = dtoUpdateCompromissosRecorrentes.getIntervalo();

        boolean atualizouIntervalo =
                !intervaloDto.equals(compromissosRecorrente.getIntervalo());

        mapperCompromissosRecorrentes.atualizacao(dtoUpdateCompromissosRecorrentes, compromissosRecorrente);

        //criada para nao gerar erro na requisicao
        List<DTOCreateHorariosPorDiaBase> listaFalsa = List.of();

        List<String> errosIdentificados = verificarValidadeDasInformacoes(
                compromissosRecorrente,listaFalsa);

        if(!errosIdentificados.isEmpty()){
            throw new BadRequestException("Erros Na Requisicao:\n" + errosIdentificados);
        }

        compromissosRecorrentesRepository.save(compromissosRecorrente);

        if(atualizouIntervalo){
            compromissosRecorrente.getCompromissosGerados().clear();

            criarCompromissosPorRecorrencia(compromissosRecorrente);
        }

        DTOSaidaCompromissosRecorrentes dtoSaidaCompromissosRecorrentes =  mapperCompromissosRecorrentes.mapToDto(compromissosRecorrente);

        return new DTORespostaCompromissoRecorrente(dtoSaidaCompromissosRecorrentes);
    }

    public void deletarCompromissoPorId(long compromissoid,long usuarioId){
        usuariosService.findUsuarioById(usuarioId);

        if(!compromissosRecorrentesRepository.existsByIdAndUsuarioId(compromissoid,usuarioId)) {
            throw new ResourceNotFindException("Compromisso com ID:" +compromissoid +"não foi encontrado" +
                    "nesse usuário");
        }
        compromissosRecorrentesRepository.deleteById(compromissoid);
    }

    @Transactional
    public void deletarCompromissosAntigos(){
        LocalDate aPartirDe = LocalDate.now().minusMonths(1);
        compromissosRecorrentesRepository.deletarCompromissosAntigos(aPartirDe);
    }

    //cria automaticamente compromissos a partir de um compromisso recorrente
    @Transactional
    public List<DTORespostaCompromisso> criarCompromissosPorRecorrencia(CompromissosRecorrentesModel compromissosModel){
        CompromissosRecorrentesModel compromissoRecorrente = compromissosRecorrentesRepository.findById(compromissosModel.getId()).
                orElseThrow(() -> new RuntimeException("compromisso recorrente não encontrado"));

        return horariosPorDiaService.criarCompromissosPorModoDeRecorrencia(compromissoRecorrente);
    }

    @Transactional
    public List<String> verificarValidadeDasInformacoes(
            CompromissosRecorrentesModel compromissoRecorrente,List<DTOCreateHorariosPorDiaBase> listaHorarios){
        List<String> errosIdentificados = new ArrayList<>();

        ModoDeRecorrenciaEnum modoDeRecorrencia = compromissoRecorrente.getModoDeRecorrencia();

        boolean inconformidadeModoDeRecorrencia = listaHorarios.stream()
                .anyMatch(horario -> !verificarConformidadeModoDeRecorrencia_Horario(modoDeRecorrencia, horario));

        if(inconformidadeModoDeRecorrencia){
            errosIdentificados.add("Combinação inválida de tipo e modo de recorrência\n");
        }

        boolean inconformidadeIntervalo = compromissoRecorrente.getIntervalo() == null ||
                compromissoRecorrente.getIntervalo() <= 0;

        if(inconformidadeIntervalo){
            errosIdentificados.add("intervalo nao pode ser null nem menor ou igual a 0\n");
        }

        boolean inconformidadeInicio_Fim_Recorrencia =
                compromissoRecorrente.getDataFimRecorrencia()
                        .isBefore(compromissoRecorrente.getDataInicioRecorrencia());
        if (inconformidadeInicio_Fim_Recorrencia){
            errosIdentificados.add("A Data De Inicio Da Recorrencia Não Pode Ser Depois Da Data De Fim Da Recorrencia");
        }

        boolean horariosConflitamEntreSi = !listaHorarios.isEmpty() &&
                horariosPorDiaService.verificarConflitosListaCriacaoHorarios(
                        compromissoRecorrente.getModoDeRecorrencia(),listaHorarios);

        if (horariosConflitamEntreSi){
            errosIdentificados.add("Os Horarios Passados Conflitam");
        }

        return errosIdentificados;
    }

    public boolean saoConflitantes(CompromissosRecorrentesModel compromissosRecorrente1,
                                   CompromissosRecorrentesModel compromissosRecorrente2){
        boolean periodosDeRecorrenciaConflitam =
                compromissosRecorrente1.getDataInicioRecorrencia().isBefore(compromissosRecorrente2.getDataFimRecorrencia())
                && compromissosRecorrente1.getDataFimRecorrencia().isAfter(compromissosRecorrente2.getDataInicioRecorrencia());

        if(!periodosDeRecorrenciaConflitam){
            return false;
        }

        ModoDeRecorrenciaEnum modoDeRecorrencia_1 = compromissosRecorrente1.getModoDeRecorrencia();
        ModoDeRecorrenciaEnum modoDeRecorrencia_2 = compromissosRecorrente2.getModoDeRecorrencia();

        boolean saoModosCompativeis = modoDeRecorrencia_1.equals(modoDeRecorrencia_2);

        if (!saoModosCompativeis){
            return false;
        }

        List<HorariosPorDiaModel> listaHorariosPorDia_1 = compromissosRecorrente1.getHorariosPorDia();

        List<HorariosPorDiaModel> listaHorariosPorDia_2 = compromissosRecorrente2.getHorariosPorDia();

        return listaHorariosPorDia_1.stream()
                .anyMatch(horario -> horariosPorDiaService.verificarConflitosComHorarioNaLista(modoDeRecorrencia_1,
                        horario,listaHorariosPorDia_2));
    }

    public List<CompromissosRecorrentesModel> verificarConflitosNaLista(
            CompromissosRecorrentesModel compromisso,
            List<CompromissosRecorrentesModel> listaCompromissos) {

        return listaCompromissos.stream()
                .filter(c -> !c.getId().equals(compromisso.getId())) // ignora o próprio compromisso
                .filter(c -> saoConflitantes(compromisso, c))
                .toList();
    }

    public List<CompromissosRecorrentesModel> verificarConflitos(CompromissosRecorrentesModel compromissoRecorrente){
        LocalDate diaAtual = LocalDate.now();

        List<CompromissosRecorrentesModel> listaCompromissosAtualizada =
                compromissosRecorrentesRepository
                        .listAllByUserAfterDate(diaAtual,compromissoRecorrente.getUsuario().getId());

        return verificarConflitosNaLista(compromissoRecorrente,listaCompromissosAtualizada);
    }

    public List<List<DTOSaidaCompromissosRecorrentes>> compromissosConflitantesLista(List<CompromissosRecorrentesModel> lista) {
        Map<CompromissosRecorrentesModel, Set<CompromissosRecorrentesModel>> conflitosEntreCompromissos = new HashMap<>();

        for (CompromissosRecorrentesModel compromisso : lista) {
            List<CompromissosRecorrentesModel> conflitos = verificarConflitosNaLista(compromisso, lista);
            if (!conflitos.isEmpty()) {
                conflitosEntreCompromissos.put(compromisso, new HashSet<>(conflitos));
            }
        }

        Set<CompromissosRecorrentesModel> jaVisitados = new HashSet<>();
        List<List<DTOSaidaCompromissosRecorrentes>> gruposDeConflito = new ArrayList<>();

        for (CompromissosRecorrentesModel compromisso : conflitosEntreCompromissos.keySet()) {
            if (!jaVisitados.contains(compromisso)) {
                Set<CompromissosRecorrentesModel> grupo = new HashSet<>();
                Queue<CompromissosRecorrentesModel> fila = new LinkedList<>();
                fila.add(compromisso);

                while (!fila.isEmpty()) {
                    CompromissosRecorrentesModel atual = fila.poll();
                    if (jaVisitados.add(atual)) {
                        grupo.add(atual);
                        fila.addAll(conflitosEntreCompromissos.getOrDefault(atual, Set.of()));
                    }
                }

                List<DTOSaidaCompromissosRecorrentes> grupoDTO = grupo.stream()
                        .map(mapperCompromissosRecorrentes::mapToDto)
                        .collect(Collectors.toList());

                gruposDeConflito.add(grupoDTO);
            }
        }

        gruposDeConflito.sort(Comparator.comparing(
                        (List<DTOSaidaCompromissosRecorrentes> grupo) -> grupo.getFirst().getDataInicioRecorrencia())
                .thenComparing(grupo -> grupo.getFirst().getHorariosPorDia().getFirst().getHoraInicio())
                .thenComparing(grupo -> grupo.getFirst().getHorariosPorDia().getFirst().getHoraFim())
        );

        return gruposDeConflito;
    }
}
