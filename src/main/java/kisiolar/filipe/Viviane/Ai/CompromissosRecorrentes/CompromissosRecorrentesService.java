package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosModel;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosRepository;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOCreateCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOSaidaCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.MapperCompromissos;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.OrdenamentoDaSemanaNoMesEnum;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia.HorariosFrequenciaDiariaService;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia.HorariosPorDiaService;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum.FREQUENCIA_SEMANAL;
import static kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum.PADRAO_RELATIVO_MENSAL;

@Service
public class CompromissosRecorrentesService{
    //TODO:usar @transacional quando for fazer requisisao com mapper
    //TODO:NAO deixar o inicio da recorrencia,o final da recorrencia a data e o horario e os dias como opcionais
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
    private HorariosPorDiaService horariosPorDiaService;

    @Transactional
    //TODO: refatora para ter outra lista completa mapeada pelos dias da semana
    public DTORespostasListasCompromissoRecorrentes listarCompromissos(){
        List<CompromissosRecorrentesModel> lista = compromissosRecorrentesRepository.findAll();

        List<DTOSaidaCompromissosRecorrentes> listaDto =lista.stream().
                sorted(Comparator
                        .comparing(CompromissosRecorrentesModel::getDataInicioRecorrencia)).
                map(mapperCompromissosRecorrentes ::mapToDto).
                collect(Collectors.toList());

        return new DTORespostasListasCompromissoRecorrentes(listaDto);
    }

    @Transactional
    public DTORespostaCompromissoRecorrente buscarCompromissoPorId(long id){
       CompromissosRecorrentesModel compromissosRecorrentesModel = compromissosRecorrentesRepository.findById(id).
               orElseThrow(() -> new RuntimeException("compromisso recorrente não encontrado"));

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

    @Transactional
    public DTORespostaCompromissoRecorrente buscarCompromissoPorNome(String nome){
        CompromissosRecorrentesModel compromissosRecorrentesModel = compromissosRecorrentesRepository.findByNome(nome).
                orElseThrow(() -> new RuntimeException("compromisso recorrente não encontrado"));

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


    public List<List<DTOSaidaCompromissosRecorrentes>> listarCompromissosConflitantes(){
        List<CompromissosRecorrentesModel> listaTodosCompromissos = compromissosRecorrentesRepository.findAll();
        List<List<DTOSaidaCompromissosRecorrentes>> gruposDeConflito = compromissosConflitantesLista(listaTodosCompromissos);
        return gruposDeConflito;
    }

    public DTORespostaCompromissoRecorrente criarCompromissoRecorrente(DTOCreateCompromissosRecorrentes dtoCreateCompromissosRecorrentes){
        //todo fazer verificacao dos horarios,fazer um estream com verificacao baseada no modo de recorrencia setado
        //salvar o compromisso e ja guarda-lo
        CompromissosRecorrentesModel compromissoRecorrente = compromissosRecorrentesRepository.save(mapperCompromissosRecorrentes.mapToModel(dtoCreateCompromissosRecorrentes));

        //chama o metodo criado para gerar os compromissos atrelados e guarda conflitos(se houver)
        List<DTORespostaCompromisso> compromissosGeradosComConflito = criarCompromissosPorRecorrencia(compromissoRecorrente);

        List<DTOSaidaCompromissosRecorrentes> conflitos = verificarConflitos(compromissoRecorrente).stream()
                .map(mapperCompromissosRecorrentes ::mapToDto)
                .collect(Collectors.toList());

        DTOSaidaCompromissosRecorrentes saidaCompromissosRecorrentes = mapperCompromissosRecorrentes.mapToDto(compromissoRecorrente);

        if(conflitos.isEmpty() && compromissosGeradosComConflito.isEmpty()){
            return new DTORespostaCompromissoRecorrente(saidaCompromissosRecorrentes);
        } else if (conflitos.isEmpty()) {
            return DTORespostaCompromissoRecorrente.comConflitosGerados(saidaCompromissosRecorrentes,compromissosGeradosComConflito);
        } else if (compromissosGeradosComConflito.isEmpty()) {
            return DTORespostaCompromissoRecorrente.comConflitosRecorrentes(saidaCompromissosRecorrentes,conflitos);
        } else {
            return new DTORespostaCompromissoRecorrente(saidaCompromissosRecorrentes,conflitos,compromissosGeradosComConflito);
        }
    }

    @Transactional
     public DTORespostaCompromissoRecorrente alterarCompromissoRecorrente(long id, DTOUpdateCompromissosRecorrentes dtoUpdateCompromissosRecorrentes){
        //todo nao deixar o update mudar o modo de recorrencia
        CompromissosRecorrentesModel compromissosRecorrente = compromissosRecorrentesRepository.findById(id).
                orElseThrow(() -> new RuntimeException("compromisso recorrente não encontrado"));

        mapperCompromissosRecorrentes.atualizacao(dtoUpdateCompromissosRecorrentes, compromissosRecorrente);

        compromissosRecorrentesRepository.save(compromissosRecorrente);

        DTOSaidaCompromissosRecorrentes dtoSaidaCompromissosRecorrentes =  mapperCompromissosRecorrentes.mapToDto(compromissosRecorrente);

        return new DTORespostaCompromissoRecorrente(dtoSaidaCompromissosRecorrentes);
    }

    public void deletarCompromissoPorId(long id){
        if(!compromissosRecorrentesRepository.existsById(id)) {
            throw new ResourceNotFindException("Compromisso com ID:" +id +"não foi encontrado");
        }
        compromissosRecorrentesRepository.deleteById(id);
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

        boolean saoModosCompativeis = switch (modoDeRecorrencia_1) {
            case FREQUENCIA_SEMANAL -> modoDeRecorrencia_2 == FREQUENCIA_SEMANAL || modoDeRecorrencia_2 == PADRAO_RELATIVO_MENSAL;
            case PADRAO_RELATIVO_MENSAL -> modoDeRecorrencia_2 == PADRAO_RELATIVO_MENSAL || modoDeRecorrencia_2 == FREQUENCIA_SEMANAL;
            default -> modoDeRecorrencia_1 == modoDeRecorrencia_2;
        };

        if (!saoModosCompativeis){
            return false;
        }

        List<HorariosPorDiaModel> listaHorariosPorDia_1 = compromissosRecorrente1.getHorariosPorDias();

        List<HorariosPorDiaModel> listaHorariosPorDia_2 = compromissosRecorrente2.getHorariosPorDias();

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
        return verificarConflitosNaLista(compromissoRecorrente,compromissosRecorrentesRepository.findAll());
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
