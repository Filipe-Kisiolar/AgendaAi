package kisiolar.filipe.Viviane.Ai.Compromissos;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
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
    public List<DTOSaidaCompromissos> listarCompromissos(){
        List<CompromissosModel> lista = compromissosRepository.findAll();
        return lista.stream().
                map(mapperCompromissos::map).
                collect(Collectors.toList());

    }

    @Transactional
    public DTOSaidaCompromissos buscarCompromissoPorId(long id){
        CompromissosModel compromissosModel = compromissosRepository.findById(id).orElse(null);

        return mapperCompromissos.map(compromissosModel);
    }

    @Transactional
    public List<DTOSaidaCompromissos> listarCompromissosPorNome(String nome){
        List<CompromissosModel> lista = compromissosRepository.findByNome(nome);

        return lista.stream().
                map(mapperCompromissos::map).
                collect(Collectors.toList());
    }

    @Transactional
    public List<DTOSaidaCompromissos> listarCompromissosDoDia(LocalDate dia){
        List<CompromissosModel> lista = compromissosRepository.findByDia(dia);
        return lista.stream().
                map(mapperCompromissos::map).
                collect(Collectors.toList());
    }

    //gera uma lista de compromissos da semana a partir do dia que a requisicao foi feita
    @Transactional
    public Map<DayOfWeek,List<DTOSaidaCompromissos>> listarCompromissosDaSemana(LocalDate diaAtual){

        LocalDate diaFinal = diaAtual.plusDays(6);

        List<CompromissosModel> compromissosDaSemana = compromissosRepository.findByDiaBetween(diaAtual,diaFinal);

        //gera uma lista de dias na ordem do dia da semana atual ate o dia da semana final
        List<DayOfWeek> ordemDosDias = IntStream.range(0, 7)
                .mapToObj(i -> diaAtual.plusDays(i).getDayOfWeek())
                .collect(Collectors.toList());

        //inicializa um map que tem a ordem criada na lista ordemDosDias
        Map<DayOfWeek ,List<DTOSaidaCompromissos>> diasOrganizados= new LinkedHashMap<>();
        for (DayOfWeek dia: ordemDosDias){
            diasOrganizados.put(dia, new ArrayList<>());
        }
        //altera os models para dtosaida e ja preenche o map com eles
        for (CompromissosModel compromisso : compromissosDaSemana){
            DayOfWeek dia = compromisso.getDia().getDayOfWeek();
            if (diasOrganizados.containsKey(dia)){
                diasOrganizados.get(dia).add(mapperCompromissos.map(compromisso));
            }
        }

        //aqui organiza os compromissos pelos horarios
        for (List<DTOSaidaCompromissos> lista : diasOrganizados.values()) {
            lista.sort(Comparator.comparing(DTOSaidaCompromissos::getHoraInicial));
        }
        return diasOrganizados;
    }

    public DTOSaidaCompromissos criarCompromisso(DTOCreateCompromissos dtoCreateCompromissos) {
        CompromissosModel compromissosModel = mapperCompromissos.map(dtoCreateCompromissos);

        if  (dtoCreateCompromissos.getCompromissoRecorrenteId() != 0) {
            CompromissosRecorrentesModel compromissosRecorrentesModel = compromissosRecorrentesRepository
                    .findById(dtoCreateCompromissos.getCompromissoRecorrenteId())
                    .orElseThrow(() -> new RuntimeException("Compromisso recorrente não encontrado"));

            compromissosModel.setCompromissoRecorrente(compromissosRecorrentesModel);
        }

        compromissosRepository.save(compromissosModel);

        return mapperCompromissos.map(compromissosModel);
    }

    public DTOSaidaCompromissos alterarCompromisso(long id,DTOUpdateCompromissos dtoUpdateCompromissos){
        CompromissosModel compromissosModel = compromissosRepository.findById(id).orElse(null);
        mapperCompromissos.atualizacao(dtoUpdateCompromissos,compromissosModel);
        compromissosRepository.save(compromissosModel);

        return mapperCompromissos.map(compromissosModel);
    }

    public void deletarCompromissoPorId(long id){
        //devo reaftorar depois para caso nao exista esse id
        compromissosRepository.deleteById(id);
    }
}
