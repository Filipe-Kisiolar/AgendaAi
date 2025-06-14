package kisiolar.filipe.Viviane.Ai.Compromissos;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CompromissosService {
    //TODO:usar @transacional quando for fazer requisisao com mapper
    @Autowired
    private CompromissosRepository compromissosRepository;

    @Autowired
    private CompromissosRecorrentesRepository compromissosRecorrentesRepository;

    @Autowired
    private MapperCompromissos mapperCompromissos;

    @Transactional
    public DTORespostaListasCompromissos listarCompromissos(){
        List<CompromissosModel> lista = compromissosRepository.findAll();
        List<DTOSaidaCompromissos> listaDto =lista.stream()
                        .sorted(Comparator
                            .comparing(CompromissosModel::getDia)
                            .thenComparing(CompromissosModel::getHoraInicial)
                        )
                        .map(mapperCompromissos::map)
                        .toList();

        return new DTORespostaListasCompromissos(listaDto);
    }

    @Transactional
    public DTORespostaCompromisso buscarCompromissoPorId(long id){
        CompromissosModel compromissosModel = compromissosRepository.findById(id).
                orElseThrow(() -> new ResourceNotFindException("compromisso não encontrado"));

        DTOSaidaCompromissos dtoSaidaCompromissos = mapperCompromissos.map(compromissosModel);

        List<DTOSaidaCompromissos> conflitos = verificarConflitos(compromissosModel).stream()
                .map(mapperCompromissos::map)
                .toList();

        if(conflitos.isEmpty()){
            return new DTORespostaCompromisso(dtoSaidaCompromissos);
        }else {
            return new DTORespostaCompromisso(dtoSaidaCompromissos,conflitos);
        }
    }

    @Transactional
    public DTORespostaListasCompromissos listarCompromissosPorNome(String nome){
        List<CompromissosModel> lista = compromissosRepository.findByNome(nome);

        List<DTOSaidaCompromissos> listaDto =lista.stream()
                .sorted(Comparator
                        .comparing(CompromissosModel::getDia)
                        .thenComparing(CompromissosModel::getHoraInicial)
                )
                .map(mapperCompromissos::map)
                .toList();

        List<List<DTOSaidaCompromissos>> conflitos = compromissosConflitantesLista(lista);

        if(conflitos.isEmpty()){
            return new DTORespostaListasCompromissos(listaDto);
        }else {
            return new DTORespostaListasCompromissos(listaDto,conflitos);
        }
    }

    @Transactional
    public DTORespostaListasCompromissos listarCompromissosDoDia(LocalDate dia){
        List<CompromissosModel> lista = compromissosRepository.findByDia(dia);
        List<DTOSaidaCompromissos> listaDto =lista.stream()
                .sorted(Comparator
                        .comparing(CompromissosModel::getDia)
                        .thenComparing(CompromissosModel::getHoraInicial)
                )
                .map(mapperCompromissos::map)
                .toList();

        List<List<DTOSaidaCompromissos>> conflitos = compromissosConflitantesLista(lista);

        if(conflitos.isEmpty()){
            return new DTORespostaListasCompromissos(listaDto);
        }else {
            return new DTORespostaListasCompromissos(listaDto,conflitos);
        }
    }

    //gera uma lista de compromissos da semana a partir do dia que a requisicao foi feita
    @Transactional
    public Map<DayOfWeek, DTORespostaListasCompromissos> listarCompromissosDaSemana(LocalDate diaAtual) {
        LocalDate diaFinal = diaAtual.plusDays(6);

        List<CompromissosModel> compromissosDaSemana = compromissosRepository.findByDiaBetween(diaAtual, diaFinal);

        //Define a ordem dos dias da semana a partir da data atual
        List<DayOfWeek> ordemDosDias = IntStream.range(0, 7)
                .mapToObj(i -> diaAtual.plusDays(i).getDayOfWeek())
                .toList();

        //Inicializa o map com todos os dias da semana vazios, mantendo a ordem
        Map<DayOfWeek, List<CompromissosModel>> compromissosPorDia = new LinkedHashMap<>();
        for (DayOfWeek dia : ordemDosDias) {
            compromissosPorDia.put(dia, new ArrayList<>());
        }

        //Preenche o map com os compromissos encontrados
        for (CompromissosModel compromisso : compromissosDaSemana) {
            DayOfWeek dia = compromisso.getDia().getDayOfWeek();
            compromissosPorDia.get(dia).add(compromisso);
        }

        //Ordena os compromissos de cada dia por hora
        compromissosPorDia.values().forEach(lista ->
                lista.sort(Comparator.comparing(CompromissosModel::getHoraInicial)));

        //Monta o map de resposta com os conflitos
        Map<DayOfWeek, DTORespostaListasCompromissos> respostaPorDia = new LinkedHashMap<>();

        for (DayOfWeek dia : ordemDosDias) {
            List<CompromissosModel> listaCompromissos = compromissosPorDia.get(dia);

            List<DTOSaidaCompromissos> listaDto = listaCompromissos.stream()
                    .map(mapperCompromissos::map)
                    .toList();

            List<List<DTOSaidaCompromissos>> conflitos = compromissosConflitantesLista(listaCompromissos);

            DTORespostaListasCompromissos resposta = new DTORespostaListasCompromissos();
            resposta.setListaCompromissos(listaDto);
            resposta.setCompromissosConflitantes(conflitos);

            respostaPorDia.put(dia, resposta);
        }

        return respostaPorDia;
    }

    public List<List<DTOSaidaCompromissos>> listarCompromissosConflitantes() {
        List<CompromissosModel> listaTodosCompromissos = compromissosRepository.findAll();

        List<List<DTOSaidaCompromissos>> gruposDeConflito = compromissosConflitantesLista(listaTodosCompromissos);
        return gruposDeConflito;

    }


    public DTORespostaCompromisso criarCompromisso(DTOCreateCompromissos dtoCreateCompromissos) {
        CompromissosModel compromissosModel = mapperCompromissos.map(dtoCreateCompromissos);

        if  (dtoCreateCompromissos.getCompromissoRecorrenteId() != 0) {
            CompromissosRecorrentesModel compromissosRecorrentesModel = compromissosRecorrentesRepository
                    .findById(dtoCreateCompromissos.getCompromissoRecorrenteId())
                    .orElseThrow(() -> new ResourceNotFindException("Compromisso recorrente não encontrado"));

            compromissosModel.setCompromissoRecorrente(compromissosRecorrentesModel);
        }

        compromissosRepository.save(compromissosModel);

        List<DTOSaidaCompromissos> conflitos = verificarConflitos(compromissosModel).stream()
                .map(mapperCompromissos::map)
                .collect(Collectors.toList());

        if(conflitos.isEmpty()){
            return new DTORespostaCompromisso(mapperCompromissos.map(compromissosModel));
        }else{
            return new DTORespostaCompromisso(mapperCompromissos.map(compromissosModel),conflitos);
        }
    }

    public DTORespostaCompromisso alterarCompromisso(long id, DTOUpdateCompromissos dtoUpdateCompromissos){
        CompromissosModel compromissosModel = compromissosRepository.findById(id).
                orElseThrow(() -> new RuntimeException("compromisso não encontrado"));

        mapperCompromissos.atualizacao(dtoUpdateCompromissos,compromissosModel);

        compromissosRepository.save(compromissosModel);

        List<DTOSaidaCompromissos> conflitos = verificarConflitos(compromissosModel).stream()
                .map(mapperCompromissos::map)
                .collect(Collectors.toList());

        if(conflitos.isEmpty()){
            return new DTORespostaCompromisso(mapperCompromissos.map(compromissosModel));
        }else{
            return new DTORespostaCompromisso(mapperCompromissos.map(compromissosModel),conflitos);
        }
    }

    public void deletarCompromissoPorId(long id){
        if(!compromissosRepository.existsById(id)){
            throw new ResourceNotFindException("Compromisso com ID:" +id +"não foi encontrado");
        }
        compromissosRepository.deleteById(id);
    }

    @Transactional
    public void deletarCompromissosAntigos(){
        LocalDate aPartirDe = LocalDate.now().minusMonths(1);
        compromissosRepository.deletarCompromissosAntigos(aPartirDe);
    }

    //verifica se ha conflito entre dois compromissos
    public boolean conflitamEntreSi(CompromissosModel compromisso1,CompromissosModel compromisso2){
        boolean temMesmoDia,horariosConflitam;


         temMesmoDia = compromisso1.getDia().equals(compromisso2.getDia());

         horariosConflitam = compromisso1.getHoraInicial().isBefore(compromisso2.getHoraFinal()) &&
                 compromisso1.getHoraFinal().isAfter(compromisso2.getHoraInicial());

         return temMesmoDia && horariosConflitam;
    }

    //confere se ha compromisso recorrente em uma lista que conflita com o que esta sendo passado
    public List<CompromissosModel> verificarConflitosNaLista(CompromissosModel compromisso,List<CompromissosModel> lista){

        List<CompromissosModel> listaConflitos = lista.stream()
                .filter(c -> !c.getId().equals(compromisso.getId()))//ignora o proprio compromisso
                .filter(c -> conflitamEntreSi(c,compromisso))
                .toList();

        return listaConflitos;
    }

    //lista conflitos de horario com o compromisso passado
    public List<CompromissosModel> verificarConflitos(CompromissosModel compromisso) {
       List<CompromissosModel> listaTodosCompromissos = compromissosRepository.findAll();

       return verificarConflitosNaLista(compromisso,listaTodosCompromissos);
    }

    //retorna grupos de compromissos que conflitam a partir de uma lista
    public List<List<DTOSaidaCompromissos>> compromissosConflitantesLista(List<CompromissosModel> lista){
        // Mapa de conflitos diretos entre compromissos
        Map<CompromissosModel, Set<CompromissosModel>> conflitosEntreCompromissos = new HashMap<>();

        for (CompromissosModel compromisso : lista) {
            List<CompromissosModel> listaConflitosIndividualizada= verificarConflitosNaLista(compromisso, lista);
            if(!listaConflitosIndividualizada.isEmpty()) {
                conflitosEntreCompromissos.put(compromisso, new HashSet<>(listaConflitosIndividualizada));
            }
        }

        Set<CompromissosModel> jaVisitados = new HashSet<>();
        List<List<DTOSaidaCompromissos>> gruposDeConflito = new ArrayList<>();

        for (CompromissosModel compromisso : conflitosEntreCompromissos.keySet()) {
            if (!jaVisitados.contains(compromisso)) {
                Set<CompromissosModel> grupo = new HashSet<>();
                Queue<CompromissosModel> fila = new LinkedList<>();
                fila.add(compromisso);

                while (!fila.isEmpty()) {
                    CompromissosModel atual = fila.poll();
                    if (jaVisitados.add(atual)) {
                        grupo.add(atual);
                        fila.addAll(conflitosEntreCompromissos.getOrDefault(atual, Set.of()));
                    }
                }

                List<DTOSaidaCompromissos> grupoDTO = grupo.stream()
                        .map(mapperCompromissos::map)
                        .collect(Collectors.toList());
                gruposDeConflito.add(grupoDTO);
            }
        }

        gruposDeConflito.sort(Comparator.comparing((List<DTOSaidaCompromissos> grupo) -> grupo.getFirst().getDia())
                .thenComparing(grupo -> grupo.getFirst().getHoraInicial())
                .thenComparing(grupo -> grupo.getFirst().getHoraFinal()));

        return gruposDeConflito;
    }
}
