package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosModel;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOCreateCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal.DTOCreateHorariosDiaEspecificoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal.DTOSaidaHorariosDiaEspecificoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal.DTOUpdateHorariosDiaEspecificoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.HorariosDiaEspecificoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia.MapperHorariosDiaEspecificoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.RepositoriesHorariosPorDia.HorariosDiaEspecificoMensalRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.MapperCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class HorariosDiaEspecificoMensalService extends HorariosServiceBase{

    private static final long SIM = 1;
    private static final long NAO = 0;

     private final HorariosDiaEspecificoMensalRepository horariosDiaEspecificoMensalRepository;

     private final MapperHorariosDiaEspecificoMensal mapperHorariosDiaEspecificoMensal;


    public HorariosDiaEspecificoMensalService(CompromissosRecorrentesRepository compromissosRecorrentesRepository, MapperCompromissosRecorrentes mapperCompromissosRecorrentes, CompromissosService compromissosService, HorariosDiaEspecificoMensalRepository horariosDiaEspecificoMensalRepository, MapperHorariosDiaEspecificoMensal mapperHorariosDiaEspecificoMensal) {
        super(compromissosRecorrentesRepository, mapperCompromissosRecorrentes, compromissosService);
        this.horariosDiaEspecificoMensalRepository = horariosDiaEspecificoMensalRepository;
        this.mapperHorariosDiaEspecificoMensal = mapperHorariosDiaEspecificoMensal;
    }

    @Transactional
    public DTOSaidaHorariosDiaEspecificoMensal adicionarHorario(CompromissosRecorrentesModel compromissoRecorrente, DTOCreateHorariosDiaEspecificoMensal dtoHorario){
        HorariosDiaEspecificoMensal horariosCriado = mapperHorariosDiaEspecificoMensal.mapToModel(dtoHorario);

        horariosCriado.setCompromissoRecorrente(compromissoRecorrente);

        List<HorariosDiaEspecificoMensal> listaHorarios = compromissoRecorrente.getHorariosPorDias().stream()
                .map(HorariosDiaEspecificoMensal.class::cast)
                .toList();

        boolean haConflitos = verificarConflitosComHorarioNaLista(horariosCriado,listaHorarios);

        if(haConflitos){
            throw new BadRequestException("Esse horário conflita com outros já criados no mesmo Compromisso Recorrente");
        }

        horariosDiaEspecificoMensalRepository.save(horariosCriado);

        criarCompromissosDiretamentePorDiaEspecificoMensal(compromissoRecorrente,horariosCriado);

        return mapperHorariosDiaEspecificoMensal.mapToDto(horariosCriado);
    }

    @Transactional
    public DTOSaidaHorariosDiaEspecificoMensal alterarHorario(CompromissosRecorrentesModel compromissoRecorrente, Long horarioId, DTOUpdateHorariosDiaEspecificoMensal dtoUpdateHorario){
        HorariosDiaEspecificoMensal horarioParaAtualizar = horariosDiaEspecificoMensalRepository.findById(horarioId)
                .orElseThrow(()-> new ResourceNotFindException("Esse id não foi achado nesse tipo de horário"));

        LocalTime inicioHorarioAntigo = horarioParaAtualizar.getHoraInicio();
        int diaDoMesHorarioAntigo = horarioParaAtualizar.getInicioDiaEspecificoMes();

        HorariosDiaEspecificoMensal horarioAntigo = new HorariosDiaEspecificoMensal();
        horarioAntigo.setHoraInicio(inicioHorarioAntigo);
        horarioAntigo.setInicioDiaEspecificoMes(diaDoMesHorarioAntigo);
        horarioAntigo.setCompromissoRecorrente(compromissoRecorrente);


        mapperHorariosDiaEspecificoMensal.atualizacao(dtoUpdateHorario,horarioParaAtualizar);

        List<HorariosDiaEspecificoMensal> outrosHorarios = compromissoRecorrente.getHorariosPorDias().stream()
                .filter(h -> !h.getId().equals(horarioId))
                .map(HorariosDiaEspecificoMensal.class::cast)
                .toList();

        boolean haConflitos = verificarConflitosComHorarioNaLista(horarioParaAtualizar,outrosHorarios);

        if(haConflitos){
            throw new BadRequestException("Esse horário conflita com outros já criados no mesmo Compromisso Recorrente");
        }

        apagarCompromissosAtreladosAoHorarioPorDia(horarioAntigo);

        horariosDiaEspecificoMensalRepository.save(horarioParaAtualizar);

        criarCompromissosDiretamentePorDiaEspecificoMensal(compromissoRecorrente,horarioParaAtualizar);

        return mapperHorariosDiaEspecificoMensal.mapToDto(horarioParaAtualizar);
    }

    @Transactional
    public List<DTORespostaCompromisso> criarCompromissosPorRecorrenciaDiaEspecificoMensal (CompromissosRecorrentesModel compromissoRecorrente){

        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();

        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long intervalo = compromissoRecorrente.getIntervalo();

        long numeroMesesDeRecorrencia = ChronoUnit.MONTHS.between(inicioRecorrencia, fimRecorrencia);

        return compromissoRecorrente.getHorariosPorDias().stream()
                .map(HorariosDiaEspecificoMensal.class::cast)
                .flatMap(horario -> criarCompromissosPorDiaEspecificoMensal(compromissoRecorrente,horario
                ,inicioRecorrencia,fimRecorrencia,intervalo,numeroMesesDeRecorrencia).stream())
                .toList();
    }

    @Transactional
    private void criarCompromissosDiretamentePorDiaEspecificoMensal(CompromissosRecorrentesModel compromissoRecorrente, HorariosDiaEspecificoMensal horario){

        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();

        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long intervalo = compromissoRecorrente.getIntervalo();

        long numeroMesesDeRecorrencia = ChronoUnit.MONTHS.between(inicioRecorrencia, fimRecorrencia);

        criarCompromissosPorDiaEspecificoMensal(compromissoRecorrente,horario,
            inicioRecorrencia,fimRecorrencia,intervalo,numeroMesesDeRecorrencia);

    }

    @Transactional
    private List<DTORespostaCompromisso> criarCompromissosPorDiaEspecificoMensal(
                CompromissosRecorrentesModel compromissoRecorrente,
                HorariosDiaEspecificoMensal horario,
                LocalDate inicioRecorrencia,LocalDate fimRecorrencia,
                long intervalo,
                long numeroMesesDeRecorrencia){
        long pulaUmMes;

        int ano = inicioRecorrencia.getYear();
        int mes = inicioRecorrencia.getMonthValue();

        int diaInicioSeguro = Math.min(
                horario.getInicioDiaEspecificoMes(),
                YearMonth.of(ano, mes).lengthOfMonth()
        );

        int diaFimSeguro = Math.min(
                horario.getFimDiaEspecificoMes(),
                YearMonth.of(ano, mes).lengthOfMonth()
        );

        LocalDateTime inicioPrimeiroCompromisso = LocalDateTime.of(
                ano, mes, diaInicioSeguro,
                horario.getHoraInicio().getHour(),
                horario.getHoraInicio().getMinute()
        );

        LocalDateTime fimPrimeiroCompromisso = LocalDateTime.of(
                ano, mes, diaFimSeguro,
                horario.getHoraFim().getHour(),
                horario.getHoraFim().getMinute()
        );

        if (inicioPrimeiroCompromisso.isBefore(inicioRecorrencia.atTime(horario.getHoraInicio()))) {
            pulaUmMes = SIM;
        }else {
            pulaUmMes = NAO;
        }

        List<DTORespostaCompromisso> compromissosGerados = new ArrayList<>();

        long i = pulaUmMes;

        LocalDateTime inicioCompromisso = inicioPrimeiroCompromisso.plusMonths(i);

        LocalDateTime fimCompromisso = fimPrimeiroCompromisso.plusMonths(i);

        while (!inicioCompromisso.toLocalDate().isAfter(fimRecorrencia)) {

            DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                    .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);
            compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));

             i = i + intervalo;

             inicioCompromisso = inicioPrimeiroCompromisso.plusMonths(i);

             fimCompromisso = fimPrimeiroCompromisso.plusMonths(i);
        }

        return compromissosGerados;
    }

    @Transactional
    public Long apagarCompromissosAtreladosAoHorarioPorDia(HorariosDiaEspecificoMensal horariosPorDia){
        CompromissosRecorrentesModel compromissosRecorrentesAtrelado = horariosPorDia.getCompromissoRecorrente();

        List<CompromissosModel> listaDosCompromissos = compromissosRecorrentesAtrelado.getCompromissosGerados();

        long tamanhoInicial = listaDosCompromissos.size();

        int diaDoMesHorario = horariosPorDia.getInicioDiaEspecificoMes();
        LocalTime inicioHorario = horariosPorDia.getHoraInicio();

        listaDosCompromissos.removeIf(
                c -> c.getInicio().toLocalDate().getDayOfMonth() == diaDoMesHorario
                        && c.getInicio().toLocalTime().equals(inicioHorario)
        );

        long numeroCompromissosApagados = tamanhoInicial - listaDosCompromissos.size();

        return numeroCompromissosApagados;
    }

    public boolean verificarConflitoEntreHorariosDiaEspecificoMensal(
            HorariosDiaEspecificoMensal horariosPorDia_1,
            HorariosDiaEspecificoMensal horariosPorDia_2) {

        int ano = LocalDate.now().getYear();
        int mes = LocalDate.now().getMonthValue();
        int ultimoDiaDoMes = YearMonth.of(ano, mes).lengthOfMonth();

        int diaInicio1 = Math.min(horariosPorDia_1.getInicioDiaEspecificoMes(), ultimoDiaDoMes);
        int diaFim1 = Math.min(horariosPorDia_1.getFimDiaEspecificoMes(), ultimoDiaDoMes);
        int diaInicio2 = Math.min(horariosPorDia_2.getInicioDiaEspecificoMes(), ultimoDiaDoMes);
        int diaFim2 = Math.min(horariosPorDia_2.getFimDiaEspecificoMes(), ultimoDiaDoMes);

        LocalDateTime inicioHorario_1 = LocalDateTime.of(
                ano, mes, diaInicio1,
                horariosPorDia_1.getHoraInicio().getHour(),
                horariosPorDia_1.getHoraInicio().getMinute()
        );

        LocalDateTime fimHorario_1 = LocalDateTime.of(
                ano, mes, diaFim1,
                horariosPorDia_1.getHoraFim().getHour(),
                horariosPorDia_1.getHoraFim().getMinute()
        );

        LocalDateTime inicioHorario_2 = LocalDateTime.of(
                ano, mes, diaInicio2,
                horariosPorDia_2.getHoraInicio().getHour(),
                horariosPorDia_2.getHoraInicio().getMinute()
        );

        LocalDateTime fimHorario_2 = LocalDateTime.of(
                ano, mes, diaFim2,
                horariosPorDia_2.getHoraFim().getHour(),
                horariosPorDia_2.getHoraFim().getMinute()
        );

        return inicioHorario_1.isBefore(fimHorario_2)
                && fimHorario_1.isAfter(inicioHorario_2);
    }

    protected boolean verificarConflitosComHorarioNaLista(HorariosDiaEspecificoMensal horario,
                                                        List<HorariosDiaEspecificoMensal> listaHorarios){
        return listaHorarios.stream()
                .anyMatch(h -> verificarConflitoEntreHorariosDiaEspecificoMensal(h,horario)
                && !Objects.equals(h.getId(), horario.getId()));
    }
}
