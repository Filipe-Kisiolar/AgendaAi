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
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaService;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
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
    private HorariosPorDiaService horariosPorDiaService;
    @Autowired
    private MapperCompromissosRecorrentes mapperCompromissosRecorrentes;
    @Autowired
    private MapperCompromissos mapperCompromissos;

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

        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();

        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long diasDeRecorrencia = ChronoUnit.DAYS.between(inicioRecorrencia,fimRecorrencia);

        long intervalo = compromissoRecorrente.getIntervalo();

        List<HorariosPorDiaModel> ListaHorariosPorDias = compromissoRecorrente.getHorariosPorDias();

        List<DTORespostaCompromisso> compromissosGerados = new ArrayList<>();

        switch (compromissoRecorrente.getModoDeRecorrencia()){
            case FREQUENCIA_DIARIA -> {
                for (HorariosPorDiaModel horariosDoDia: ListaHorariosPorDias) {
                    long diferencaComeco_Fim = horariosDoDia.getHoraInicio().compareTo(horariosDoDia.getHoraFim());
                    if(diferencaComeco_Fim > 0){
                        for (long i = 0; i < diasDeRecorrencia; i = i + intervalo) {

                            LocalDateTime inicioCompromisso = inicioRecorrencia.plusDays(i).atTime(horariosDoDia.getHoraInicio());
                            LocalDateTime fimCompromisso = inicioCompromisso.toLocalDate().atTime(horariosDoDia.getHoraFim());

                            fimCompromisso = fimCompromisso.plusDays(1);

                            DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                                    .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);

                            compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
                        }
                    }else{
                        for (long i = 0; i < diasDeRecorrencia; i = i + intervalo) {

                            LocalDateTime inicioCompromisso = inicioRecorrencia.plusDays(i).atTime(horariosDoDia.getHoraInicio());
                            LocalDateTime fimCompromisso = inicioCompromisso.toLocalDate().plusDays(diferencaComeco_Fim)
                                    .atTime(horariosDoDia.getHoraFim());

                            DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                                    .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);

                            compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
                        }
                    }
                }
            }
            case FREQUENCIA_SEMANAL -> {
                final int PASSADESEMANA = 7;
                for (HorariosPorDiaModel horariosDoDia: ListaHorariosPorDias) {
                    DayOfWeek diaDaSemanaInicioCompromisso = horariosDoDia.getDiaDaSemanaInicio();

                    DayOfWeek diaDaSemanaFimCompromisso = horariosDoDia.getDiaDaSemanaFim();

                    int distanciaAtePrimeiroDiaValido =
                            diaDaSemanaInicioCompromisso.getValue() - inicioRecorrencia.getDayOfWeek().getValue();

                    if(distanciaAtePrimeiroDiaValido<0){
                        distanciaAtePrimeiroDiaValido +=PASSADESEMANA;
                    }

                    LocalDate primeiroDiaValido = inicioRecorrencia.plusDays(distanciaAtePrimeiroDiaValido);

                    int diferencaComeco_Fim = diaDaSemanaFimCompromisso.getValue() - diaDaSemanaInicioCompromisso.getValue();

                    if(diferencaComeco_Fim < 0){
                        diferencaComeco_Fim +=PASSADESEMANA;
                    }

                    for (LocalDate dataBase = primeiroDiaValido ;dataBase.isBefore(compromissoRecorrente.getDataFimRecorrencia());
                         dataBase = dataBase.plusWeeks(intervalo)){

                        LocalDateTime inicioCompromisso = dataBase.atTime(horariosDoDia.getHoraInicio());
                        LocalDateTime fimCompromisso = dataBase.plusDays(diferencaComeco_Fim).atTime(horariosDoDia.getHoraFim());

                        DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                                .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);

                        compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
                    }
                }
            }
            case PADRAO_RELATIVO_MENSAL -> {
                long numeroMesesDeRecorrencia = ChronoUnit.MONTHS.between(inicioRecorrencia,fimRecorrencia);
                int semanaProcurada;
                if(compromissoRecorrente.getOrdenamentoDaSemanaNoMes().equals(OrdenamentoDaSemanaNoMesEnum.ULTIMA_SEMANA)){
                     semanaProcurada = -1;
                }else {
                     semanaProcurada = compromissoRecorrente.getOrdenamentoDaSemanaNoMes().ordinal() + 1;
                }

                for(HorariosPorDiaModel horariosDoDia : ListaHorariosPorDias){
                    LocalDate primeiroDiaDoMesDoPrimeiroCompromisso = inicioRecorrencia.withDayOfMonth(1);

                    DayOfWeek diaDaSemanaInicioCompromisso = horariosDoDia.getDiaDaSemanaInicio();

                    DayOfWeek diaDaSemanaFimCompromisso = horariosDoDia.getDiaDaSemanaFim();

                    TemporalAdjuster distanciaAteDiaComeco = TemporalAdjusters.
                            dayOfWeekInMonth(semanaProcurada, diaDaSemanaInicioCompromisso);

                    TemporalAdjuster distanciaAteDiaFim = TemporalAdjusters.
                            dayOfWeekInMonth(semanaProcurada, diaDaSemanaFimCompromisso);

                    LocalDate diaInicioCompromisso = primeiroDiaDoMesDoPrimeiroCompromisso.with(distanciaAteDiaComeco);

                    //caso o primeiro compromisso que sera criado seja depois do inicio da recorrencia ele é criado
                    if(diaInicioCompromisso.isAfter(inicioRecorrencia)){

                        LocalDateTime inicioCompromisso = diaInicioCompromisso.atTime(horariosDoDia.getHoraInicio());

                        LocalDateTime fimCompromisso = primeiroDiaDoMesDoPrimeiroCompromisso.with(distanciaAteDiaFim).atTime(horariosDoDia.getHoraFim());

                        DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                                .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);
                        compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
                    }
                    //criacao dos outros compromissos
                    for(long i = intervalo; i< numeroMesesDeRecorrencia; i= i + intervalo){
                        LocalDate primeiroDiaDoMes = primeiroDiaDoMesDoPrimeiroCompromisso.plusMonths(i);

                        LocalDateTime inicioCompromisso = primeiroDiaDoMes.with(distanciaAteDiaComeco).atTime(horariosDoDia.getHoraInicio());

                        LocalDateTime fimCompromisso = primeiroDiaDoMes.with(distanciaAteDiaFim).atTime(horariosDoDia.getHoraFim());

                        DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                                .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);
                        compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
                    }
                }

            }
            case DIA_ESPECIFICO_MENSAL -> {
                final long SIM = 1;
                final long NAO = 0;
                long pulaUmMes = NAO;
                long numeroMesesDeRecorrencia = ChronoUnit.MONTHS.between(inicioRecorrencia, fimRecorrencia);
                for (HorariosPorDiaModel horariosDoDia : ListaHorariosPorDias){
                    LocalDateTime inicioPrimeiroCompromisso = LocalDateTime.of(
                            inicioRecorrencia.getYear(),
                            inicioRecorrencia.getMonth(),
                            horariosDoDia.getInicioDiaEspecificoMes(),
                            horariosDoDia.getHoraInicio().getHour(),
                            horariosDoDia.getHoraInicio().getMinute()
                    );

                    LocalDateTime fimPrimeiroCompromisso = LocalDateTime.of(
                            inicioRecorrencia.getYear(),
                            inicioRecorrencia.getMonth(),
                            horariosDoDia.getFimDiaEspecificoMes(),
                            horariosDoDia.getHoraFim().getHour(),
                            horariosDoDia.getHoraFim().getMinute()
                    );

                    if(inicioPrimeiroCompromisso.isBefore(inicioRecorrencia.atTime(horariosDoDia.getHoraInicio()))){
                        pulaUmMes = SIM;
                    }

                    for (long i = pulaUmMes; i< numeroMesesDeRecorrencia; i= i + intervalo ){
                        LocalDateTime inicioCompromisso = inicioPrimeiroCompromisso.plusMonths(i);
                        LocalDateTime fimCompromisso = fimPrimeiroCompromisso.plusMonths(i);

                        DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                                .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);
                        compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
                    }
                }
            }
            case DATA_ESPECIFICA_ANUAL -> {
                final long SIM = 1;
                final long NAO = 0;
                long pulaUmANO = NAO;
                long numeroAnosDeRecorrencia = ChronoUnit.YEARS.between(inicioRecorrencia, fimRecorrencia);
                for (HorariosPorDiaModel horariosDoDia : ListaHorariosPorDias) {
                    MonthDay diaDoAnoInicio = horariosDoDia.getInicioDataEspecificaDoAno();

                    MonthDay diaDoAnoFim = horariosDoDia.getFimDataEspecificaDoAno();

                    LocalDateTime inicioPrimeiroCompromisso = diaDoAnoInicio.atYear(inicioRecorrencia.getYear())
                            .atTime(horariosDoDia.getHoraInicio());

                    LocalDateTime fimPrimeiroCompromisso = diaDoAnoFim.atYear(inicioPrimeiroCompromisso.getYear())
                            .atTime(horariosDoDia.getHoraFim());

                    if (inicioPrimeiroCompromisso.isBefore(inicioRecorrencia.atTime(horariosDoDia.getHoraInicio()))) {
                        pulaUmANO = SIM;
                    }

                    for (long i = pulaUmANO; i < numeroAnosDeRecorrencia; i = i + intervalo) {
                        LocalDateTime inicioCompromisso = inicioPrimeiroCompromisso.plusYears(i);
                        LocalDateTime fimCompromisso = fimPrimeiroCompromisso.plusYears(i);

                        DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                                .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);
                        compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
                    }
                }
            }
        }
        return compromissosGerados;
    }

    @Transactional
    public List<DTORespostaCompromisso> criarCompromissosDiretamentePorHorariosPorDia(HorariosPorDiaModel horariosDoDia){

        CompromissosRecorrentesModel compromissoRecorrenteAtrelado = horariosDoDia.getCompromissoRecorrente();

        LocalDate inicioRecorrencia = compromissoRecorrenteAtrelado.getDataInicioRecorrencia();

        LocalDate fimRecorrencia = compromissoRecorrenteAtrelado.getDataFimRecorrencia();

        long diasDeRecorrencia = ChronoUnit.DAYS.between(inicioRecorrencia,fimRecorrencia);

        long intervalo = compromissoRecorrenteAtrelado.getIntervalo();

        List<DTORespostaCompromisso> compromissosGerados = new ArrayList<>();

        switch (compromissoRecorrenteAtrelado.getModoDeRecorrencia()) {
            case FREQUENCIA_DIARIA -> {

                long diferencaComeco_Fim = horariosDoDia.getHoraInicio().compareTo(horariosDoDia.getHoraFim());
                if (diferencaComeco_Fim > 0) {
                    for (long i = 0; i < diasDeRecorrencia; i = i + intervalo) {

                        LocalDateTime inicioCompromisso = inicioRecorrencia.plusDays(i).atTime(horariosDoDia.getHoraInicio());
                        LocalDateTime fimCompromisso = inicioCompromisso.toLocalDate().atTime(horariosDoDia.getHoraFim());

                        fimCompromisso = fimCompromisso.plusDays(1);

                        DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                                .mapGerarCompromisso(compromissoRecorrenteAtrelado, inicioCompromisso, fimCompromisso);

                        compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
                    }
                } else {
                    for (long i = 0; i < diasDeRecorrencia; i = i + intervalo) {

                        LocalDateTime inicioCompromisso = inicioRecorrencia.plusDays(i).atTime(horariosDoDia.getHoraInicio());
                        LocalDateTime fimCompromisso = inicioCompromisso.toLocalDate().plusDays(diferencaComeco_Fim)
                                .atTime(horariosDoDia.getHoraFim());

                        DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                                .mapGerarCompromisso(compromissoRecorrenteAtrelado, inicioCompromisso, fimCompromisso);

                        compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
                    }
                }
            }
            case FREQUENCIA_SEMANAL -> {
                final int PASSADESEMANA = 7;
                DayOfWeek diaDaSemanaInicioCompromisso = horariosDoDia.getDiaDaSemanaInicio();

                DayOfWeek diaDaSemanaFimCompromisso = horariosDoDia.getDiaDaSemanaFim();

                int distanciaAtePrimeiroDiaValido =
                        diaDaSemanaInicioCompromisso.getValue() - inicioRecorrencia.getDayOfWeek().getValue();

                if (distanciaAtePrimeiroDiaValido < 0) {
                    distanciaAtePrimeiroDiaValido += PASSADESEMANA;
                }

                LocalDate primeiroDiaValido = inicioRecorrencia.plusDays(distanciaAtePrimeiroDiaValido);

                int diferencaComeco_Fim = diaDaSemanaFimCompromisso.getValue() - diaDaSemanaInicioCompromisso.getValue();

                if (diferencaComeco_Fim < 0) {
                    diferencaComeco_Fim += PASSADESEMANA;
                }

                for (LocalDate dataBase = primeiroDiaValido; dataBase.isBefore(compromissoRecorrenteAtrelado.getDataFimRecorrencia());
                     dataBase = dataBase.plusWeeks(intervalo)) {

                    LocalDateTime inicioCompromisso = dataBase.atTime(horariosDoDia.getHoraInicio());
                    LocalDateTime fimCompromisso = dataBase.plusDays(diferencaComeco_Fim).atTime(horariosDoDia.getHoraFim());

                    DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                            .mapGerarCompromisso(compromissoRecorrenteAtrelado, inicioCompromisso, fimCompromisso);

                    compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
                }
            }
            case PADRAO_RELATIVO_MENSAL -> {
                long numeroMesesDeRecorrencia = ChronoUnit.MONTHS.between(inicioRecorrencia, fimRecorrencia);
                int semanaProcurada;
                if (compromissoRecorrenteAtrelado.getOrdenamentoDaSemanaNoMes().equals(OrdenamentoDaSemanaNoMesEnum.ULTIMA_SEMANA)) {
                    semanaProcurada = -1;
                } else {
                    semanaProcurada = compromissoRecorrenteAtrelado.getOrdenamentoDaSemanaNoMes().ordinal() + 1;
                }

                LocalDate primeiroDiaDoMesDoPrimeiroCompromisso = inicioRecorrencia.withDayOfMonth(1);

                DayOfWeek diaDaSemanaInicioCompromisso = horariosDoDia.getDiaDaSemanaInicio();

                DayOfWeek diaDaSemanaFimCompromisso = horariosDoDia.getDiaDaSemanaFim();

                TemporalAdjuster distanciaAteDiaComeco = TemporalAdjusters.
                        dayOfWeekInMonth(semanaProcurada, diaDaSemanaInicioCompromisso);

                TemporalAdjuster distanciaAteDiaFim = TemporalAdjusters.
                        dayOfWeekInMonth(semanaProcurada, diaDaSemanaFimCompromisso);

                LocalDate diaInicioCompromisso = primeiroDiaDoMesDoPrimeiroCompromisso.with(distanciaAteDiaComeco);

                //caso o primeiro compromisso que sera criado seja depois do inicio da recorrencia ele é criado
                if (diaInicioCompromisso.isAfter(inicioRecorrencia)) {

                    LocalDateTime inicioCompromisso = diaInicioCompromisso.atTime(horariosDoDia.getHoraInicio());

                    LocalDateTime fimCompromisso = primeiroDiaDoMesDoPrimeiroCompromisso.with(distanciaAteDiaFim).atTime(horariosDoDia.getHoraFim());

                    DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                            .mapGerarCompromisso(compromissoRecorrenteAtrelado, inicioCompromisso, fimCompromisso);
                    compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
                }
                //criacao dos outros compromissos
                for (long i = intervalo; i < numeroMesesDeRecorrencia; i = i + intervalo) {
                    LocalDate primeiroDiaDoMes = primeiroDiaDoMesDoPrimeiroCompromisso.plusMonths(i);

                    LocalDateTime inicioCompromisso = primeiroDiaDoMes.with(distanciaAteDiaComeco).atTime(horariosDoDia.getHoraInicio());

                    LocalDateTime fimCompromisso = primeiroDiaDoMes.with(distanciaAteDiaFim).atTime(horariosDoDia.getHoraFim());

                    DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                            .mapGerarCompromisso(compromissoRecorrenteAtrelado, inicioCompromisso, fimCompromisso);
                    compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
                }
            }
            case DIA_ESPECIFICO_MENSAL -> {
                final long SIM = 1;
                final long NAO = 0;
                long pulaUmMes = NAO;
                long numeroMesesDeRecorrencia = ChronoUnit.MONTHS.between(inicioRecorrencia, fimRecorrencia);
                LocalDateTime inicioPrimeiroCompromisso = LocalDateTime.of(
                        inicioRecorrencia.getYear(),
                        inicioRecorrencia.getMonth(),
                        horariosDoDia.getInicioDiaEspecificoMes(),
                        horariosDoDia.getHoraInicio().getHour(),
                        horariosDoDia.getHoraInicio().getMinute()
                );

                LocalDateTime fimPrimeiroCompromisso = LocalDateTime.of(
                        inicioRecorrencia.getYear(),
                        inicioRecorrencia.getMonth(),
                        horariosDoDia.getFimDiaEspecificoMes(),
                        horariosDoDia.getHoraFim().getHour(),
                        horariosDoDia.getHoraFim().getMinute()
                );

                if (inicioPrimeiroCompromisso.isBefore(inicioRecorrencia.atTime(horariosDoDia.getHoraInicio()))) {
                    pulaUmMes = SIM;
                }

                for (long i = pulaUmMes; i < numeroMesesDeRecorrencia; i = i + intervalo) {
                    LocalDateTime inicioCompromisso = inicioPrimeiroCompromisso.plusMonths(i);
                    LocalDateTime fimCompromisso = fimPrimeiroCompromisso.plusMonths(i);

                    DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                            .mapGerarCompromisso(compromissoRecorrenteAtrelado, inicioCompromisso, fimCompromisso);
                    compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
                }
            }
            case DATA_ESPECIFICA_ANUAL -> {
                final long SIM = 1;
                final long NAO = 0;
                long pulaUmANO = NAO;
                long numeroAnosDeRecorrencia = ChronoUnit.YEARS.between(inicioRecorrencia, fimRecorrencia);
                MonthDay diaDoAnoInicio = horariosDoDia.getInicioDataEspecificaDoAno();

                MonthDay diaDoAnoFim = horariosDoDia.getFimDataEspecificaDoAno();

                LocalDateTime inicioPrimeiroCompromisso = diaDoAnoInicio.atYear(inicioRecorrencia.getYear())
                        .atTime(horariosDoDia.getHoraInicio());

                LocalDateTime fimPrimeiroCompromisso = diaDoAnoFim.atYear(inicioPrimeiroCompromisso.getYear())
                        .atTime(horariosDoDia.getHoraFim());

                if (inicioPrimeiroCompromisso.isBefore(inicioRecorrencia.atTime(horariosDoDia.getHoraInicio()))) {
                    pulaUmANO = SIM;
                }

                for (long i = pulaUmANO; i < numeroAnosDeRecorrencia; i = i + intervalo) {
                    LocalDateTime inicioCompromisso = inicioPrimeiroCompromisso.plusYears(i);
                    LocalDateTime fimCompromisso = fimPrimeiroCompromisso.plusYears(i);

                    DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                            .mapGerarCompromisso(compromissoRecorrenteAtrelado, inicioCompromisso, fimCompromisso);
                    compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));

                }
            }
        }
        return compromissosGerados;
    }

    @Transactional
    public List<DTOSaidaCompromissos> apagarCompromissosAtreladosAoHorarioPorDia(HorariosPorDiaModel horariosPorDia){
        CompromissosRecorrentesModel compromissosRecorrentesAtrelado = horariosPorDia.getCompromissoRecorrente();

        List<CompromissosModel> listaDosCompromissos = compromissosRecorrentesAtrelado.getCompromissosGerados();

        List<CompromissosModel> listaCompromissosDeletados  = new ArrayList<>();

        for(CompromissosModel compromisso : listaDosCompromissos){
            DayOfWeek diaDaSemanaDoCompromisso = compromisso.getInicio().getDayOfWeek();
            LocalTime horarioInicioCompromisso = compromisso.getInicio().toLocalTime();

            boolean compromissoFoiGeradoPeloHorario = diaDaSemanaDoCompromisso == horariosPorDia.getDiaDaSemanaInicio()
                    && horarioInicioCompromisso == horariosPorDia.getHoraInicio();

            if(compromissoFoiGeradoPeloHorario){
                listaCompromissosDeletados.add(compromisso);
                compromissosService.deletarCompromissoPorId(compromisso.getId());
            }

        }
       return mapperCompromissos.mapToDtoList(listaCompromissosDeletados);
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

        boolean saoDoMesmoModoDeRecorrencia = modoDeRecorrencia_1.equals(modoDeRecorrencia_2);
        boolean saoSemanalEMensalRelativo =
                (modoDeRecorrencia_1 == ModoDeRecorrenciaEnum.FREQUENCIA_SEMANAL && modoDeRecorrencia_2 == ModoDeRecorrenciaEnum.PADRAO_RELATIVO_MENSAL) ||
                        (modoDeRecorrencia_1 == ModoDeRecorrenciaEnum.PADRAO_RELATIVO_MENSAL && modoDeRecorrencia_2 == ModoDeRecorrenciaEnum.FREQUENCIA_SEMANAL);

        boolean modosDeRecorrenciaUsamMesmoTipoHorarios = saoDoMesmoModoDeRecorrencia || saoSemanalEMensalRelativo;

        if (!modosDeRecorrenciaUsamMesmoTipoHorarios){
            return false;
        }

        List<HorariosPorDiaModel> listaHorariosPorDia_1 = compromissosRecorrente1.getHorariosPorDias();

        List<HorariosPorDiaModel> listaHorariosPorDia_2 = compromissosRecorrente2.getHorariosPorDias();

        boolean temConflito = false;
        int i = 0;
        while (!temConflito && i< listaHorariosPorDia_1.size()){

            HorariosPorDiaModel horariosPorDia_1 = listaHorariosPorDia_1.get(i);

            temConflito = horariosPorDiaService.verificarConflitosComHorarioNaLista(compromissosRecorrente1,horariosPorDia_1,listaHorariosPorDia_2);

            i++;
        }

        return temConflito;
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

    @PostConstruct
    public void init() {
        horariosPorDiaService.setCompromissosRecorrentesService(this);
    }
}
