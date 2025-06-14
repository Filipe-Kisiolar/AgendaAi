package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosRepository;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.DTOCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.DTORespostaCompromissoRecorrente;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.DTORespostasListasCompromissoRecorrentes;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.DTOUpdateCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional
    //TODO: refatora para ter outra lista completa mapeada pelos dias da semana
    public DTORespostasListasCompromissoRecorrentes listarCompromissos(){
        List<CompromissosRecorrentesModel> lista = compromissosRecorrentesRepository.findAll();

        List<DTOCompromissosRecorrentes> listaDto =lista.stream().
                sorted(Comparator
                        .comparing(CompromissosRecorrentesModel::getDataInicioRecorrencia)).
                map(mapperCompromissosRecorrentes ::map).
                collect(Collectors.toList());

        return new DTORespostasListasCompromissoRecorrentes(listaDto);
    }

    @Transactional
    public DTORespostaCompromissoRecorrente buscarCompromissoPorId(long id){
       CompromissosRecorrentesModel compromissosRecorrentesModel = compromissosRecorrentesRepository.findById(id).
               orElseThrow(() -> new RuntimeException("compromisso recorrente não encontrado"));

       DTOCompromissosRecorrentes dtoCompromissosRecorrentes = mapperCompromissosRecorrentes.map(compromissosRecorrentesModel);

       List<DTOCompromissosRecorrentes> conflitos = verificarConflitos(compromissosRecorrentesModel).stream()
               .map(mapperCompromissosRecorrentes::map)
               .toList();

       if (conflitos.isEmpty()){
           return new DTORespostaCompromissoRecorrente(dtoCompromissosRecorrentes);
       }else{
           return DTORespostaCompromissoRecorrente.comConflitosRecorrentes(dtoCompromissosRecorrentes,conflitos);
       }
    }

    @Transactional
    public DTORespostaCompromissoRecorrente buscarCompromissoPorNome(String nome){
        CompromissosRecorrentesModel compromissosRecorrentesModel = compromissosRecorrentesRepository.findByNome(nome).
                orElseThrow(() -> new RuntimeException("compromisso recorrente não encontrado"));

        DTOCompromissosRecorrentes dtoCompromissosRecorrentes = mapperCompromissosRecorrentes.map(compromissosRecorrentesModel);

        List<DTOCompromissosRecorrentes> conflitos = verificarConflitos(compromissosRecorrentesModel).stream()
                .map(mapperCompromissosRecorrentes::map)
                .toList();

        if (conflitos.isEmpty()){
            return new DTORespostaCompromissoRecorrente(dtoCompromissosRecorrentes);
        }else{
            return DTORespostaCompromissoRecorrente.comConflitosRecorrentes(dtoCompromissosRecorrentes,conflitos);
        }
    }

    @Transactional
    public DTORespostasListasCompromissoRecorrentes buscarCompromissoPorDiaDaSemana(DayOfWeek dia){
        List<CompromissosRecorrentesModel> lista = compromissosRecorrentesRepository.findByDiasDaSemana(dia);

        List<DTOCompromissosRecorrentes> listaDto =lista.stream().
                sorted(Comparator
                        .comparing(CompromissosRecorrentesModel::getDataInicioRecorrencia)).
                map(mapperCompromissosRecorrentes ::map).
                collect(Collectors.toList());

        List<List<DTOCompromissosRecorrentes>> conflitos = compromissosConflitantesLista(lista);

        if(conflitos.isEmpty()){
            return new DTORespostasListasCompromissoRecorrentes(listaDto);
        }else {
            return new DTORespostasListasCompromissoRecorrentes(listaDto,conflitos);
        }
    }

    public List<List<DTOCompromissosRecorrentes>> listarCompromissosConflitantes(){
        List<CompromissosRecorrentesModel> listaTodosCompromissos = compromissosRecorrentesRepository.findAll();
        List<List<DTOCompromissosRecorrentes>> gruposDeConflito = compromissosConflitantesLista(listaTodosCompromissos);
        return gruposDeConflito;
    }

    public DTORespostaCompromissoRecorrente criarCompromisso(DTOCompromissosRecorrentes dtoCompromissosRecorrentes){

        //salvar o compromisso e ja guarda-lo
        CompromissosRecorrentesModel compromissoRecorrente = compromissosRecorrentesRepository.save(mapperCompromissosRecorrentes.map(dtoCompromissosRecorrentes));

        //chama o metodo criado para gerar os compromissos atrelados e guarda conflitos(se houver)
        List<DTORespostaCompromisso> compromissosGeradosComConflito = criarCompromissosPorRecorrencia(compromissoRecorrente);

        List<DTOCompromissosRecorrentes> conflitos = verificarConflitos(compromissoRecorrente).stream()
                .map(mapperCompromissosRecorrentes :: map)
                .collect(Collectors.toList());

        if(conflitos.isEmpty() && compromissosGeradosComConflito.isEmpty()){
            return new DTORespostaCompromissoRecorrente(dtoCompromissosRecorrentes);
        } else if (conflitos.isEmpty()) {
            return DTORespostaCompromissoRecorrente.comConflitosGerados(dtoCompromissosRecorrentes,compromissosGeradosComConflito);
        } else if (compromissosGeradosComConflito.isEmpty()) {
            return DTORespostaCompromissoRecorrente.comConflitosRecorrentes(dtoCompromissosRecorrentes,conflitos);
        } else {
            return new DTORespostaCompromissoRecorrente(dtoCompromissosRecorrentes,conflitos,compromissosGeradosComConflito);
        }
    }

    @Transactional
    public DTORespostaCompromissoRecorrente alterarCompromisso(long id, DTOUpdateCompromissosRecorrentes dtoUpdateCompromissosRecorrentes){
        CompromissosRecorrentesModel compromissosRecorrentesModel = compromissosRecorrentesRepository.findById(id).
                orElseThrow(() -> new RuntimeException("compromisso recorrente não encontrado"));

        //apaga os compromissos anteriores
        if(!compromissosRecorrentesModel.getCompromissosGerados().isEmpty()){
        compromissosRepository.deleteAll(compromissosRecorrentesModel.getCompromissosGerados());
        compromissosRecorrentesModel.getCompromissosGerados().clear();
        }

        mapperCompromissosRecorrentes.atualizacao(dtoUpdateCompromissosRecorrentes,compromissosRecorrentesModel);
        //salva as alteracoes do compromisso recorrente e o guarda em uma variavel
        CompromissosRecorrentesModel compromissoSalvo = compromissosRecorrentesRepository.save(compromissosRecorrentesModel);

        DTOCompromissosRecorrentes compromissoSalvoDto = mapperCompromissosRecorrentes.map(compromissoSalvo);

        //chama o metodo criado para gerar os compromissos atrelados e guarda conflitos(se houver)
        List<DTORespostaCompromisso> compromissosGeradosComConflito = criarCompromissosPorRecorrencia(compromissoSalvo);

        List<DTOCompromissosRecorrentes> conflitos = verificarConflitos(compromissoSalvo).stream()
                .map(mapperCompromissosRecorrentes :: map)
                .collect(Collectors.toList());

        if(conflitos.isEmpty() && compromissosGeradosComConflito.isEmpty()){
            return new DTORespostaCompromissoRecorrente(compromissoSalvoDto);
        } else if (conflitos.isEmpty()) {
            return DTORespostaCompromissoRecorrente.comConflitosGerados(compromissoSalvoDto,compromissosGeradosComConflito);
        } else if (compromissosGeradosComConflito.isEmpty()) {
            return DTORespostaCompromissoRecorrente.comConflitosRecorrentes(compromissoSalvoDto,conflitos);
        } else {
            return new DTORespostaCompromissoRecorrente(compromissoSalvoDto,conflitos,compromissosGeradosComConflito);
        }
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
    public List<DTORespostaCompromisso> criarCompromissosPorRecorrencia(CompromissosRecorrentesModel compromissosModel){
        CompromissosRecorrentesModel compromissoRecorrente = compromissosRecorrentesRepository.findById(compromissosModel.getId()).
                orElseThrow(() -> new RuntimeException("compromisso recorrente não encontrado"));

        long diferencaEntreDias = ChronoUnit.DAYS.between(compromissoRecorrente.getDataInicioRecorrencia(),compromissoRecorrente.getDataFimRecorrencia());
        LocalDate dataDeInicio = compromissoRecorrente.getDataInicioRecorrencia();

        //lista para pegar os compromissos com conflitos gerados
        List<DTORespostaCompromisso> compromissosComConflito = new ArrayList<>();

            for(long i = 0;i<= diferencaEntreDias;i++){
            //confere se o dia esta dentro dos dias da semana q compromisso recorrente repete
            if(compromissoRecorrente.getDiasDaSemana().contains(dataDeInicio.plusDays(i).getDayOfWeek())) {
                DTORespostaCompromisso compromissoCriado = compromissosService.criarCompromisso(mapperCompromissosRecorrentes
                        .mapGerarCompromisso(compromissoRecorrente, dataDeInicio.plusDays(i)));
                if(compromissoCriado.getExisteConflito()){
                    compromissosComConflito.add(compromissoCriado);
                }
            }
          }
            return compromissosComConflito;
        }

    //verifica se ha conflito entre dois compromissos recorrentes
    public boolean conflitamEntreSi(CompromissosRecorrentesModel compromisso1, CompromissosRecorrentesModel compromisso2){
        boolean periodosConflitam,diaDaSemanaConflita,horariosConflitam;

        periodosConflitam = compromisso1.getDataInicioRecorrencia().isBefore(compromisso2.getDataFimRecorrencia())
                && compromisso1.getDataFimRecorrencia().isAfter(compromisso2.getDataInicioRecorrencia());

        diaDaSemanaConflita = !Collections.disjoint(compromisso1.getDiasDaSemana(), compromisso2.getDiasDaSemana());

        horariosConflitam = compromisso1.getHoraInicial().isBefore(compromisso2.getHoraFinal()) &&
                compromisso1.getHoraFinal().isAfter(compromisso2.getHoraInicial());

        return periodosConflitam && diaDaSemanaConflita && horariosConflitam;
    }

    //confere se ha compromisso recorrente em uma lista que conflita com o que esta sendo passado
    public List<CompromissosRecorrentesModel> verificarConflitosNaLista(
            CompromissosRecorrentesModel compromisso,
            List<CompromissosRecorrentesModel> listaCompromissos){

        List<CompromissosRecorrentesModel> compromissosConflitantes = listaCompromissos.stream()
                .filter(c -> !c.getId().equals(compromisso.getId()))//tira o proprio compromisso
                .filter(c -> conflitamEntreSi(c,compromisso))
                .toList();

        return compromissosConflitantes;
    }

    //confere se ha compromisso recorrente que conflita com o que esta sendo criado ou alterado
    public List<CompromissosRecorrentesModel> verificarConflitos(CompromissosRecorrentesModel compromisso){
        List<CompromissosRecorrentesModel> listaTodosCompromissos = compromissosRecorrentesRepository.findAll();

        return  verificarConflitosNaLista(compromisso,listaTodosCompromissos);
    }

    //retorna grupos de compromissos que conflitam a partir de uma lista
    public List<List<DTOCompromissosRecorrentes>> compromissosConflitantesLista(List<CompromissosRecorrentesModel> lista){
        Map<CompromissosRecorrentesModel,Set<CompromissosRecorrentesModel>> conflitosEntreCompromissos = new HashMap<>();

        for(CompromissosRecorrentesModel compromissos : lista) {
            List<CompromissosRecorrentesModel> listaIndividualConflitos = verificarConflitosNaLista(compromissos, lista);
            if(!listaIndividualConflitos.isEmpty()) {
                conflitosEntreCompromissos.put(compromissos, new HashSet<>(listaIndividualConflitos));
            }
        }
        Set<CompromissosRecorrentesModel> jaVisitados = new HashSet<>();
        List<List<DTOCompromissosRecorrentes>> gruposDeConflito = new ArrayList<>();

        for(CompromissosRecorrentesModel compromisso : conflitosEntreCompromissos.keySet()){
            if(!jaVisitados.contains(compromisso)) {
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

                List<DTOCompromissosRecorrentes> grupoDTO = grupo.stream()
                        .map(mapperCompromissosRecorrentes::map)
                        .collect(Collectors.toList());
                gruposDeConflito.add(grupoDTO);
            }
        }
        gruposDeConflito.sort(Comparator.comparing((List<DTOCompromissosRecorrentes> grupo) -> grupo.getFirst().getDataInicioRecorrencia())
                .thenComparing(grupo -> grupo.getFirst().getHoraInicial())
                .thenComparing(grupo -> grupo.getFirst().getHoraFinal()));

        return gruposDeConflito;
    }
}
