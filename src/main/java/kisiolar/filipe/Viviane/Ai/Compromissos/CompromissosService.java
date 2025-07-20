package kisiolar.filipe.Viviane.Ai.Compromissos;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosModel;
import kisiolar.filipe.Viviane.Ai.Usuarios.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private UsuariosService usuariosService;

    @Transactional
    public DTORespostaListasCompromissos listarCompromissos(long usuarioId){
        usuariosService.findUsuarioById(usuarioId);

        LocalDateTime diaAtual = LocalDate.now().atStartOfDay();

        List<CompromissosModel> lista = compromissosRepository.listAllByUserAfterDate(diaAtual,usuarioId);

        List<DTOSaidaCompromissos> listaDto =ordenarListaPorHorario(lista).stream()
                .map(mapperCompromissos::map)
                .toList();

        return new DTORespostaListasCompromissos(listaDto);
    }

    @Transactional
    public DTORespostaCompromisso buscarCompromissoPorId(long id,long usuarioId){
        usuariosService.findUsuarioById(usuarioId);

        CompromissosModel compromissosModel = compromissosRepository.findByIdByUser(id,usuarioId)
                .orElseThrow(() -> new ResourceNotFindException("compromisso não encontrado" +
                        "para esse usuário"));

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
    public DTORespostaListasCompromissos listarCompromissosPorNome(String nome,long usuarioId){
        usuariosService.findUsuarioById(usuarioId);

        LocalDateTime diaAtual = LocalDate.now().atStartOfDay();

        List<CompromissosModel> lista = compromissosRepository.findByNomeByUser(nome,usuarioId,diaAtual);

        List<DTOSaidaCompromissos> listaDto =ordenarListaPorHorario(lista).stream()
                .map(mapperCompromissos::map)
                .toList();

        List<List<DTOSaidaCompromissos>> conflitos = compromissosConflitantesDaLista(lista);

        if(conflitos.isEmpty()){
            return new DTORespostaListasCompromissos(listaDto);
        }else {
            return new DTORespostaListasCompromissos(listaDto,conflitos);
        }
    }

    @Transactional
    public DTORespostaListasCompromissos listarCompromissosDoDia(LocalDate dia,long usuarioId){
        LocalDateTime comecoDoDia = dia.atStartOfDay();
        LocalDateTime finalDoDia = dia.plusDays(1).atStartOfDay();
        List<CompromissosModel> lista = compromissosRepository.findCompromissoByInicioBetwenn(comecoDoDia,finalDoDia,usuarioId);
        List<DTOSaidaCompromissos> listaDto =ordenarListaPorHorario(lista).stream()
                .map(mapperCompromissos::map)
                .toList();

        List<List<DTOSaidaCompromissos>> conflitos = compromissosConflitantesDaLista(lista);

        if(conflitos.isEmpty()){
            return new DTORespostaListasCompromissos(listaDto);
        }else {
            return new DTORespostaListasCompromissos(listaDto,conflitos);
        }
    }

    //gera uma lista de compromissos da semana a partir do dia que a requisicao foi feita
    @Transactional
    public Map<DayOfWeek, DTORespostaListasCompromissos> listarCompromissosDaSemana(
            LocalDate diaAtual,long usuarioId
    ) {
        LocalDateTime primeiroDiaDaSemana = diaAtual.atStartOfDay();
        LocalDateTime ultimoDiaDaSemana = diaAtual.plusWeeks(1).atStartOfDay();

        List<CompromissosModel> compromissosDaSemana = compromissosRepository
                .findCompromissoByInicioBetwenn(primeiroDiaDaSemana,ultimoDiaDaSemana,usuarioId);

        //Define a ordem dos dias da semana a partir da data atual
        List<DayOfWeek> ordemDosDias = IntStream.range(0, 7)
                .mapToObj(i -> diaAtual.plusDays(i).getDayOfWeek())
                .toList();

        //Inicializa o mapToModel com todos os dias da semana vazios, mantendo a ordem
        Map<DayOfWeek, List<CompromissosModel>> compromissosPorDia = new LinkedHashMap<>();
        for (DayOfWeek dia : ordemDosDias) {
            compromissosPorDia.put(dia, new ArrayList<>());
        }

        //Preenche o mapToModel com os compromissos encontrados
        for (CompromissosModel compromisso : compromissosDaSemana) {
            DayOfWeek dia = compromisso.getInicio().getDayOfWeek();
            compromissosPorDia.get(dia).add(compromisso);
        }

        //Ordena os compromissos de cada dia por hora
        compromissosPorDia.values().forEach(lista ->
                lista.sort(Comparator.comparing(CompromissosModel::getInicio)));

        //Monta o mapToModel de resposta com os conflitos
        Map<DayOfWeek, DTORespostaListasCompromissos> respostaPorDia = new LinkedHashMap<>();

        for (DayOfWeek dia : ordemDosDias) {
            List<CompromissosModel> listaCompromissos = compromissosPorDia.get(dia);

            List<DTOSaidaCompromissos> listaDto = listaCompromissos.stream()
                    .map(mapperCompromissos::map)
                    .toList();

            List<List<DTOSaidaCompromissos>> conflitos = compromissosConflitantesDaLista(listaCompromissos);

            DTORespostaListasCompromissos resposta = new DTORespostaListasCompromissos();
            resposta.setListaCompromissos(listaDto);
            resposta.setCompromissosConflitantes(conflitos);

            respostaPorDia.put(dia, resposta);
        }

        return respostaPorDia;
    }

    public List<List<DTOSaidaCompromissos>> listarCompromissosConflitantes(long usuarioId) {
        LocalDateTime diaAtual = LocalDate.now().atStartOfDay();

        List<CompromissosModel> listaTodosCompromissos =
                compromissosRepository.listAllByUserAfterDate(diaAtual,usuarioId);

        List<List<DTOSaidaCompromissos>> gruposDeConflito =
                compromissosConflitantesDaLista(listaTodosCompromissos);

        return gruposDeConflito;
    }

    public DTORespostaCompromisso criarCompromisso(DTOCreateCompromissos dtoCreateCompromissos,long usuarioId) {
        CompromissosModel compromissosModel = mapperCompromissos.map(dtoCreateCompromissos);

        UsuariosModel usuarioDoCompromisso = usuariosService.findUsuarioById(usuarioId);

        compromissosModel.setUsuario(usuarioDoCompromisso);

        if  (dtoCreateCompromissos.getCompromissoRecorrenteId() != 0) {
            CompromissosRecorrentesModel compromissosRecorrentesModel = compromissosRecorrentesRepository
                    .findById(dtoCreateCompromissos.getCompromissoRecorrenteId())
                    .orElseThrow(() -> new ResourceNotFindException("Compromisso recorrente não encontrado"));

            compromissosModel.setCompromissoRecorrente(compromissosRecorrentesModel);
        }

        List<String> errosIdentificados = verificarValidadeDasInformacoes(compromissosModel);

        if(!errosIdentificados.isEmpty()){
            throw new BadRequestException("Erros Na Requisicao:\n" + errosIdentificados);
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

    public DTORespostaCompromisso alterarCompromisso(
            long id,long usuarioId,
            DTOUpdateCompromissos dtoUpdateCompromissos
    ){
        usuariosService.findUsuarioById(usuarioId);

        CompromissosModel compromissosModel = compromissosRepository.findByIdByUser(id,usuarioId)
                .orElseThrow(() -> new RuntimeException("compromisso não encontrado para esse usuário"));

        mapperCompromissos.atualizacao(dtoUpdateCompromissos,compromissosModel);

        List<String> errosIdentificados = verificarValidadeDasInformacoes(compromissosModel);

        if(!errosIdentificados.isEmpty()){
            throw new BadRequestException("Erros Na Requisicao:\n" + errosIdentificados);
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

    public void deletarCompromissoPorId(long id){
        if(!compromissosRepository.existsById(id)){
            throw new ResourceNotFindException("Compromisso com ID:" +id +"não foi encontrado");
        }
        compromissosRepository.deleteById(id);
    }

    @Transactional
    public void deletarCompromissosAntigos(){
        LocalDateTime aPartirDe = LocalDateTime.now().minusMonths(1);
        compromissosRepository.deletarCompromissosAntigos(aPartirDe);
    }

    private List<String> verificarValidadeDasInformacoes(CompromissosModel compromisso){
        List<String> errosIdentificados = new ArrayList<>();

        boolean inconformidade_Inicio_Fim = compromisso.getInicio().isAfter(compromisso.getFim());

        if (inconformidade_Inicio_Fim){
            errosIdentificados.add("O Fim Do Compromisso nao Pode Ser Antes Do Inicio\n");
        }

        return errosIdentificados;
    }

    private List<CompromissosModel> ordenarListaPorHorario(List<CompromissosModel> lista){
        return lista.stream().sorted(Comparator
                        .comparing(CompromissosModel::getInicio)
                        .thenComparing(CompromissosModel::getFim)
                        )
                        .toList();
    }

    //verifica se ha conflito entre dois compromissos
    private boolean conflitamEntreSi(CompromissosModel compromisso1,CompromissosModel compromisso2){

        boolean conflitam = compromisso1.getInicio().isBefore(compromisso2.getFim()) &&
                compromisso1.getFim().isAfter(compromisso2.getInicio());

         return conflitam;
    }

    //confere se ha compromisso recorrente em uma lista que conflita com o que esta sendo passado
    private List<CompromissosModel> verificarConflitosNaLista(CompromissosModel compromisso,List<CompromissosModel> lista){

        List<CompromissosModel> listaConflitos = lista.stream()
                .filter(c -> !c.getId().equals(compromisso.getId()))//ignora o proprio compromisso
                .filter(c -> conflitamEntreSi(c,compromisso))
                .toList();

        return listaConflitos;
    }

    //lista conflitos de horario com o compromisso passado
    private List<CompromissosModel> verificarConflitos(CompromissosModel compromisso) {
        LocalDateTime diaAtual = LocalDate.now().atStartOfDay();

        List<CompromissosModel> listaTodosCompromissos =
                compromissosRepository.listAllByUserAfterDate(diaAtual,compromisso.getUsuario().getId());

        return verificarConflitosNaLista(compromisso,listaTodosCompromissos);
    }

    //retorna grupos de compromissos que conflitam a partir de uma lista
    private List<List<DTOSaidaCompromissos>> compromissosConflitantesDaLista(List<CompromissosModel> lista){
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

        gruposDeConflito.sort(
                Comparator.comparing((List<DTOSaidaCompromissos> grupo) -> grupo.getFirst().getInicio())
                        .thenComparing(grupo -> grupo.getFirst().getFim())
        );

        return gruposDeConflito;
    }
}
