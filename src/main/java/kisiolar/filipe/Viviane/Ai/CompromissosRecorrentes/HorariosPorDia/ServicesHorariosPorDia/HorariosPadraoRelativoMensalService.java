package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosModel;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOCreateCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTORespostaHorariosPorDia;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal.DTOCreateHorariosPadraoRelativoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal.DTOSaidaHorariosPadraoRelativoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal.DTOUpdateHorariosPadraoRelativoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.OrdenamentoDaSemanaNoMesEnum;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.HorariosPadraoRelativoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia.MapperHorariosPadraoRelativoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.RepositoriesHorariosPorDia.HorariosPadraoRelativoMensalRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.MapperCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class HorariosPadraoRelativoMensalService extends HorariosServiceBase{

    private final HorariosPadraoRelativoMensalRepository horariosPadraoRelativoMensalRepository;

    private final MapperHorariosPadraoRelativoMensal mapperHorariosPadraoRelativoMensal;

    public HorariosPadraoRelativoMensalService(CompromissosRecorrentesRepository compromissosRecorrentesRepository, MapperCompromissosRecorrentes mapperCompromissosRecorrentes, CompromissosService compromissosService, HorariosPadraoRelativoMensalRepository horariosPadraoRelativoMensalRepository, MapperHorariosPadraoRelativoMensal mapperHorariosPadraoRelativoMensal) {
        super(compromissosRecorrentesRepository, mapperCompromissosRecorrentes, compromissosService);
        this.horariosPadraoRelativoMensalRepository = horariosPadraoRelativoMensalRepository;
        this.mapperHorariosPadraoRelativoMensal = mapperHorariosPadraoRelativoMensal;
    }

    @Transactional
    public DTORespostaHorariosPorDia adicionarHorario(CompromissosRecorrentesModel compromissoRecorrente, DTOCreateHorariosPadraoRelativoMensal dtoHorario){
        HorariosPadraoRelativoMensal horariosCriado = mapperHorariosPadraoRelativoMensal.mapToModel(dtoHorario);

        horariosCriado.setCompromissoRecorrente(compromissoRecorrente);

        List<String> errosIdentificados = verificarValidadeDasInformacoes(horariosCriado,compromissoRecorrente);

        if (!errosIdentificados.isEmpty()){
            throw new BadRequestException("erros identificados no Horario :" + horariosCriado + "\n"+ errosIdentificados);
        }

        horariosPadraoRelativoMensalRepository.save(horariosCriado);

        List<DTORespostaCompromisso> compromissosCriados =
                criarCompromissosDiretamentePorPadraoRelativoMensal(compromissoRecorrente,horariosCriado);

        DTOSaidaHorariosPadraoRelativoMensal dtoSaida = mapperHorariosPadraoRelativoMensal.mapToDto(horariosCriado);

        return new DTORespostaHorariosPorDia(dtoSaida,compromissosCriados);
    }

    @Transactional
    public DTORespostaHorariosPorDia alterarHorario(CompromissosRecorrentesModel compromissoRecorrente, Long horarioId, DTOUpdateHorariosPadraoRelativoMensal dtoUpdateHorario){
        HorariosPadraoRelativoMensal horarioParaAtualizar = horariosPadraoRelativoMensalRepository.findById(horarioId)
                .orElseThrow(()-> new ResourceNotFindException("Esse id não foi achado nesse tipo de horário"));

        LocalTime inicioHorarioAntigo = horarioParaAtualizar.getHoraInicio();
        DayOfWeek diaDaSemanaAntigo = horarioParaAtualizar.getDiaDaSemanaInicio();
        OrdenamentoDaSemanaNoMesEnum ordenamentoAntigo = horarioParaAtualizar.getOrdenamentoDaSemanaNoMes();

        HorariosPadraoRelativoMensal horarioComDadosAntigos = new HorariosPadraoRelativoMensal();
        horarioComDadosAntigos.setHoraInicio(inicioHorarioAntigo);
        horarioComDadosAntigos.setDiaDaSemanaInicio(diaDaSemanaAntigo);
        horarioComDadosAntigos.setOrdenamentoDaSemanaNoMes(ordenamentoAntigo);
        horarioComDadosAntigos.setCompromissoRecorrente(compromissoRecorrente);


        mapperHorariosPadraoRelativoMensal.atualizacao(dtoUpdateHorario,horarioParaAtualizar);

        List<String> errosIdentificados = verificarValidadeDasInformacoes(horarioParaAtualizar,compromissoRecorrente);

        if (!errosIdentificados.isEmpty()){
            throw new BadRequestException("erros identificados no Horario :" + horarioParaAtualizar + "\n"+ errosIdentificados);
        }

        apagarCompromissosAtreladosAoHorarioPorDia(horarioComDadosAntigos);

        horariosPadraoRelativoMensalRepository.save(horarioParaAtualizar);

        List<DTORespostaCompromisso> compromissosCriados =
                criarCompromissosDiretamentePorPadraoRelativoMensal(compromissoRecorrente,horarioParaAtualizar);

        DTOSaidaHorariosPadraoRelativoMensal dtoSaida = mapperHorariosPadraoRelativoMensal.mapToDto(horarioParaAtualizar);

        return new DTORespostaHorariosPorDia(dtoSaida,compromissosCriados);
    }

    @Transactional
    public List<DTORespostaCompromisso> criarCompromissosPorRecorrenciaPadraoRelativoMensal(CompromissosRecorrentesModel compromissoRecorrente){
        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();
        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long intervalo = compromissoRecorrente.getIntervalo();

        return compromissoRecorrente.getHorariosPorDia().stream()
                .map(HorariosPadraoRelativoMensal.class::cast)
                .flatMap(horario -> criarCompromissosPorPadraoRelativoMensal(compromissoRecorrente,horario
                ,inicioRecorrencia,fimRecorrencia,intervalo).stream())
                .toList();

    }

    @Transactional
    private List<DTORespostaCompromisso> criarCompromissosDiretamentePorPadraoRelativoMensal(CompromissosRecorrentesModel compromissoRecorrente, HorariosPadraoRelativoMensal horario){
        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();
        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long intervalo = compromissoRecorrente.getIntervalo();

        return criarCompromissosPorPadraoRelativoMensal(compromissoRecorrente,horario
            ,inicioRecorrencia,fimRecorrencia,intervalo);
    }

    @Transactional
    private List<DTORespostaCompromisso> criarCompromissosPorPadraoRelativoMensal(
            CompromissosRecorrentesModel compromissoRecorrente,
            HorariosPadraoRelativoMensal horario,LocalDate inicioRecorrencia,
            LocalDate fimRecorrencia, long intervalo){

        int semanaProcurada;
        if(horario.getOrdenamentoDaSemanaNoMes().equals(OrdenamentoDaSemanaNoMesEnum.ULTIMA_SEMANA)){
            semanaProcurada = -1;
        }else {
            semanaProcurada = horario.getOrdenamentoDaSemanaNoMes().ordinal() + 1;
        }

        LocalDate primeiroDiaDoMesDoPrimeiroCompromisso = inicioRecorrencia.withDayOfMonth(1);

        DayOfWeek diaDaSemanaInicioCompromisso = horario.getDiaDaSemanaInicio();

        DayOfWeek diaDaSemanaFimCompromisso = horario.getDiaDaSemanaFim();

        TemporalAdjuster distanciaAteDiaComeco = TemporalAdjusters.
                dayOfWeekInMonth(semanaProcurada, diaDaSemanaInicioCompromisso);

        TemporalAdjuster distanciaAteDiaFim = TemporalAdjusters.
                dayOfWeekInMonth(semanaProcurada, diaDaSemanaFimCompromisso);

        LocalDate diaInicioCompromisso = primeiroDiaDoMesDoPrimeiroCompromisso.with(distanciaAteDiaComeco);

        List<DTORespostaCompromisso> compromissosGerados = new ArrayList<>();

        LocalDateTime inicioCompromisso = diaInicioCompromisso.atTime(horario.getHoraInicio());

        LocalDateTime fimCompromisso = primeiroDiaDoMesDoPrimeiroCompromisso.with(distanciaAteDiaFim).atTime(horario.getHoraFim());

        long i = 1;

        //caso o primeiro compromisso que sera criado seja depois do inicio da recorrencia ele é criado
        if(!diaInicioCompromisso.isBefore(inicioRecorrencia)){

            DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                    .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);

            compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos,
                    compromissoRecorrente.getUsuario().getId()));

            i = intervalo;
        }
        //caso o if a cima nao ocorra a primeira ocorrencia do compromisso sera no mes subsequente e sera espaçado pelo intervalo apos sua criacao
        while(inicioCompromisso.toLocalDate().isBefore(fimRecorrencia)){
            LocalDate primeiroDiaDoMes = primeiroDiaDoMesDoPrimeiroCompromisso.plusMonths(i);

            inicioCompromisso = primeiroDiaDoMes.with(distanciaAteDiaComeco).atTime(horario.getHoraInicio());

            fimCompromisso = primeiroDiaDoMes.with(distanciaAteDiaFim).atTime(horario.getHoraFim());

            DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                    .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);
            compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos,
                    compromissoRecorrente.getUsuario().getId()));
            i= i + intervalo;
        }
        return compromissosGerados;
    }

    private boolean isCompromissoGreadoPeloHorario (
            CompromissosModel compromisso,HorariosPadraoRelativoMensal horariosPorDia){

        LocalDate diaCompromisso = compromisso.getInicio().toLocalDate();
        DayOfWeek diaSemanaCompromisso = compromisso.getInicio().getDayOfWeek();
        LocalTime horaInicioCompromisso = compromisso.getInicio().toLocalTime();

        if (!diaSemanaCompromisso.equals(horariosPorDia.getDiaDaSemanaInicio()) || !horaInicioCompromisso.equals(horariosPorDia.getHoraInicio())) {
            return false;
        }

        LocalDate primeiroDiaDoMes = diaCompromisso.withDayOfMonth(1);

        int numOcorrencia = IntStream.rangeClosed(1,diaCompromisso.getDayOfMonth())
                .map(i -> primeiroDiaDoMes.withDayOfMonth(i)
                                .getDayOfWeek() == diaSemanaCompromisso
                                ? 1
                                :0
                        ).sum();

        LocalDate proximaOcorrencia = diaCompromisso.plusWeeks(1);
        boolean ehUltimaOcorrenciaDoMes = proximaOcorrencia.getMonth() != diaCompromisso.getMonth();

        int ordenamentoDaSemanaNoMes = numOcorrencia - 1;

        OrdenamentoDaSemanaNoMesEnum ordenamentoReal = ehUltimaOcorrenciaDoMes
                ? OrdenamentoDaSemanaNoMesEnum.ULTIMA_SEMANA
                : OrdenamentoDaSemanaNoMesEnum.values()[ordenamentoDaSemanaNoMes];

        if (ordenamentoReal != horariosPorDia.getOrdenamentoDaSemanaNoMes()) {
            return false;
        }

        return true;
    }

    @Transactional
    public Long apagarCompromissosAtreladosAoHorarioPorDia(HorariosPadraoRelativoMensal horariosPorDia){
        CompromissosRecorrentesModel compromissosRecorrentesAtrelado = horariosPorDia.getCompromissoRecorrente();

        List<CompromissosModel> listaDosCompromissos = compromissosRecorrentesAtrelado.getCompromissosGerados();

        long tamanhoInicial = listaDosCompromissos.size();

        listaDosCompromissos.removeIf(c -> isCompromissoGreadoPeloHorario(c,horariosPorDia));

        long numeroCompromissosApagados = tamanhoInicial - listaDosCompromissos.size();

        return numeroCompromissosApagados;
    }

    @Transactional
    private List<String> verificarValidadeDasInformacoes(HorariosPadraoRelativoMensal horario, CompromissosRecorrentesModel compromissoRecorrente){
        List<String> errosIdentificados = new ArrayList<>();

        boolean inconformidade_Inicio_Fim = horario.getDiaDaSemanaInicio().equals(horario.getDiaDaSemanaFim())
                && horario.getHoraInicio().isAfter(horario.getHoraFim());

        if (inconformidade_Inicio_Fim){
            errosIdentificados
                    .add("O Fim Do Horario Nao Pode Ser Antes Do Inicio" +
                            " Para Horarios Que O Fim é No Mesmo Dia Que O Inicio\n");
        }

        List<HorariosPadraoRelativoMensal> listaHorarios = compromissoRecorrente.getHorariosPorDia().stream()
                .map(HorariosPadraoRelativoMensal.class::cast)
                .toList();

        boolean haConflitos = verificarConflitosComHorarioNaLista(horario,listaHorarios);

        if(haConflitos){
            errosIdentificados.add("Esse horário conflita com outros já criados no mesmo Compromisso Recorrente\n");
        }

        return errosIdentificados;
    }

    private boolean verificarConflitoEntreHorariosUsamPadraoRelativo(HorariosPadraoRelativoMensal horario_1, HorariosPadraoRelativoMensal horario_2) {

        Set<DayOfWeek> dias1 = getDiasDaSemanaEntre(horario_1.getDiaDaSemanaInicio(), horario_1.getDiaDaSemanaFim());
        Set<DayOfWeek> dias2 = getDiasDaSemanaEntre(horario_2.getDiaDaSemanaInicio(), horario_2.getDiaDaSemanaFim());

        boolean mesmoOrdenamentoDaSemana =
                horario_1.getOrdenamentoDaSemanaNoMes().equals(horario_2.getOrdenamentoDaSemanaNoMes());

        boolean intersecaoDias = dias1.stream().anyMatch(dias2::contains);

        boolean horariosConflitam = horario_1.getHoraInicio().isBefore(horario_2.getHoraFim())
                && horario_1.getHoraFim().isAfter(horario_2.getHoraInicio());

        return mesmoOrdenamentoDaSemana && intersecaoDias && horariosConflitam;
    }

    private Set<DayOfWeek> getDiasDaSemanaEntre(DayOfWeek inicio, DayOfWeek fim) {
        Set<DayOfWeek> dias = new HashSet<>();
        DayOfWeek atual = inicio;
        do {
            dias.add(atual);
            atual = atual.plus(1);
        } while (atual != fim.plus(1));
        return dias;
    }

    protected boolean verificarConflitosComHorarioNaLista(HorariosPadraoRelativoMensal horario,List<HorariosPadraoRelativoMensal> listaHorariosPorDia){
        return listaHorariosPorDia.stream()
                .anyMatch(h -> verificarConflitoEntreHorariosUsamPadraoRelativo(h,horario) &&
                        !Objects.equals(h.getId(), horario.getId()));
    }
}
