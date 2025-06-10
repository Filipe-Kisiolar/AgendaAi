package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosRepository;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.DTOCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.DTORespostaCriacaoCompromissoRecorrente;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.DTOUpdateCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
    public List<DTOCompromissosRecorrentes> listarCompromissos(){
        List<CompromissosRecorrentesModel> lista = compromissosRecorrentesRepository.findAll();

        return lista.stream().
                sorted(Comparator
                        .comparing(CompromissosRecorrentesModel::getDataInicioRecorrencia)).
                map(mapperCompromissosRecorrentes ::map).
                collect(Collectors.toList());
    }

    @Transactional
    public DTOCompromissosRecorrentes buscarCompromissoPorId(long id){
       CompromissosRecorrentesModel compromissosRecorrentesModel = compromissosRecorrentesRepository.findById(id).
               orElseThrow(() -> new RuntimeException("compromisso recorrente não encontrado"));

       return mapperCompromissosRecorrentes.map(compromissosRecorrentesModel);
    }

    @Transactional
    public DTOCompromissosRecorrentes buscarCompromissoPorNome(String nome){
        CompromissosRecorrentesModel compromissosRecorrentesModel = compromissosRecorrentesRepository.findByNome(nome).
                orElseThrow(() -> new RuntimeException("compromisso recorrente não encontrado"));

        return mapperCompromissosRecorrentes.map(compromissosRecorrentesModel);
    }

    @Transactional
    public List<DTOCompromissosRecorrentes> buscarCompromissoPorDiaDaSemana(DayOfWeek dia){
        List<CompromissosRecorrentesModel> lista = compromissosRecorrentesRepository.findByDiasDaSemana(dia);

        return  lista.stream().
                map(mapperCompromissosRecorrentes ::map).
                collect(Collectors.toList());
    }


    public DTORespostaCriacaoCompromissoRecorrente criarCompromisso(DTOCompromissosRecorrentes dtoCompromissosRecorrentes){

        //salvar o compromisso e ja guarda-lo
        CompromissosRecorrentesModel compromissoRecorrente = compromissosRecorrentesRepository.save(mapperCompromissosRecorrentes.map(dtoCompromissosRecorrentes));

        //chama o metodo criado para gerar os compromissos atrelados
        criarCompromissosPorRecorrencia(compromissoRecorrente);

        List<DTOCompromissosRecorrentes> conflitos = verificarConflitos(compromissoRecorrente);

        return new DTORespostaCriacaoCompromissoRecorrente(dtoCompromissosRecorrentes,conflitos);
    }

    @Transactional
    public DTORespostaCriacaoCompromissoRecorrente alterarCompromisso(long id, DTOUpdateCompromissosRecorrentes dtoUpdateCompromissosRecorrentes){
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

        //cria os novos compromissos a partir do compromisso recorrente atualizado
        criarCompromissosPorRecorrencia(compromissoSalvo);

        List<DTOCompromissosRecorrentes> conflitos = verificarConflitos(compromissoSalvo);

        return new DTORespostaCriacaoCompromissoRecorrente(mapperCompromissosRecorrentes.map(compromissoSalvo),conflitos);
    }

    public void deletarCompromissoPorId(long id){
        if(!compromissosRecorrentesRepository.existsById(id)) {
            throw new ResourceNotFindException("Compromisso com ID:" +id +"não foi encontrado");
        }
        compromissosRecorrentesRepository.deleteById(id);
    }

    //cria automaticamente compromissos a partir de um compromisso recorrente
    public void criarCompromissosPorRecorrencia(CompromissosRecorrentesModel compromissosModel){
        CompromissosRecorrentesModel compromissoRecorrente = compromissosRecorrentesRepository.findById(compromissosModel.getId()).
                orElseThrow(() -> new RuntimeException("compromisso recorrente não encontrado"));

        long diferencaEntreDias = ChronoUnit.DAYS.between(compromissoRecorrente.getDataInicioRecorrencia(),compromissoRecorrente.getDataFimRecorrencia());
        LocalDate dataDeInicio = compromissoRecorrente.getDataInicioRecorrencia();
            for(long i = 0;i<= diferencaEntreDias;i++){
            //confere se o dia esta dentro dos dias da semana q algo repete
            if(compromissoRecorrente.getDiasDaSemana().contains(dataDeInicio.plusDays(i).getDayOfWeek())) {
                compromissosService.criarCompromisso(mapperCompromissosRecorrentes.mapGerarCompromisso(compromissoRecorrente, dataDeInicio.plusDays(i)));
            }
          }
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

    //confere se ha compromisso recorrente que conflita com o que esta sendo criado ou alterado
    public List<DTOCompromissosRecorrentes> verificarConflitos(CompromissosRecorrentesModel compromisso){
        List<CompromissosRecorrentesModel> listaTodosCompromissos = compromissosRecorrentesRepository.findAll();

        List<CompromissosRecorrentesModel> compromissosConflitantes = listaTodosCompromissos.stream()
                .filter(c -> !c.getId().equals(compromisso.getId()))//tira o proprio compromisso
                .filter(c -> conflitamEntreSi(c,compromisso))
                .toList();

        return compromissosConflitantes.stream()
                .map(mapperCompromissosRecorrentes :: map)
                .collect(Collectors.toList());
    }
}
