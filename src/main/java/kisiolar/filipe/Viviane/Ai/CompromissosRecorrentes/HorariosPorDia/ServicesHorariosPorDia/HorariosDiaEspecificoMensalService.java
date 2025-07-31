package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosModel;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOCreateCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTORespostaHorariosPorDia;
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
    public DTORespostaHorariosPorDia adicionarHorario(CompromissosRecorrentesModel compromissoRecorrente, DTOCreateHorariosDiaEspecificoMensal dtoHorario){
        HorariosDiaEspecificoMensal horariosCriado = mapperHorariosDiaEspecificoMensal.mapToModel(dtoHorario);

        horariosCriado.setCompromissoRecorrente(compromissoRecorrente);

        List<String> errosIdentificados = verificarValidadeDasInformacoes(horariosCriado,compromissoRecorrente);

        if (!errosIdentificados.isEmpty()){
            throw new BadRequestException("erros identificados no Horario :" + horariosCriado + "\n"+ errosIdentificados);
        }

        horariosDiaEspecificoMensalRepository.save(horariosCriado);

        List<DTORespostaCompromisso> compromissosCriados =
                criarCompromissosDiretamentePorDiaEspecificoMensal(compromissoRecorrente,horariosCriado);

        DTOSaidaHorariosDiaEspecificoMensal dtoSaida = mapperHorariosDiaEspecificoMensal.mapToDto(horariosCriado);

        return new DTORespostaHorariosPorDia(dtoSaida,compromissosCriados);
    }

    @Transactional
    public DTORespostaHorariosPorDia alterarHorario(CompromissosRecorrentesModel compromissoRecorrente, Long horarioId, DTOUpdateHorariosDiaEspecificoMensal dtoUpdateHorario){
        HorariosDiaEspecificoMensal horarioParaAtualizar = horariosDiaEspecificoMensalRepository.findById(horarioId)
                .orElseThrow(()-> new ResourceNotFindException("Esse id não foi achado nesse tipo de horário"));

        LocalTime inicioHorarioAntigo = horarioParaAtualizar.getHoraInicio();
        int diaDoMesHorarioAntigo = horarioParaAtualizar.getInicioDiaEspecificoMes();

        HorariosDiaEspecificoMensal horarioAntigo = new HorariosDiaEspecificoMensal();
        horarioAntigo.setHoraInicio(inicioHorarioAntigo);
        horarioAntigo.setInicioDiaEspecificoMes(diaDoMesHorarioAntigo);
        horarioAntigo.setCompromissoRecorrente(compromissoRecorrente);


        mapperHorariosDiaEspecificoMensal.atualizacao(dtoUpdateHorario,horarioParaAtualizar);

        List<String> errosIdentificados = verificarValidadeDasInformacoes(horarioParaAtualizar,compromissoRecorrente);

        if (!errosIdentificados.isEmpty()){
            throw new BadRequestException("erros identificados no Horario :" + horarioParaAtualizar + "\n"+ errosIdentificados);
        }

        apagarCompromissosAtreladosAoHorarioPorDia(horarioAntigo);

        horariosDiaEspecificoMensalRepository.save(horarioParaAtualizar);

        List<DTORespostaCompromisso> compromissosCriados =
                criarCompromissosDiretamentePorDiaEspecificoMensal(compromissoRecorrente,horarioParaAtualizar);

        DTOSaidaHorariosDiaEspecificoMensal dtoSaida = mapperHorariosDiaEspecificoMensal.mapToDto(horarioParaAtualizar);

        return new DTORespostaHorariosPorDia(dtoSaida,compromissosCriados);
    }

    @Transactional
    public List<DTORespostaCompromisso> criarCompromissosPorRecorrenciaDiaEspecificoMensal (CompromissosRecorrentesModel compromissoRecorrente){

        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();

        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long intervalo = compromissoRecorrente.getIntervalo();

        return compromissoRecorrente.getHorariosPorDia().stream()
                .map(HorariosDiaEspecificoMensal.class::cast)
                .flatMap(horario -> criarCompromissosPorDiaEspecificoMensal(compromissoRecorrente,horario
                ,inicioRecorrencia,fimRecorrencia,intervalo).stream())
                .toList();
    }

    @Transactional
    private List<DTORespostaCompromisso> criarCompromissosDiretamentePorDiaEspecificoMensal(CompromissosRecorrentesModel compromissoRecorrente, HorariosDiaEspecificoMensal horario){

        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();

        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long intervalo = compromissoRecorrente.getIntervalo();

        return criarCompromissosPorDiaEspecificoMensal(compromissoRecorrente,horario,
            inicioRecorrencia,fimRecorrencia,intervalo);

    }

    @Transactional
    private List<DTORespostaCompromisso> criarCompromissosPorDiaEspecificoMensal(
                CompromissosRecorrentesModel compromissoRecorrente,
                HorariosDiaEspecificoMensal horario,
                LocalDate inicioRecorrencia,LocalDate fimRecorrencia,
                long intervalo){
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
            compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos,
                    compromissoRecorrente.getUsuario().getId()));

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

    @Transactional
    private List<String> verificarValidadeDasInformacoes(
            HorariosDiaEspecificoMensal horario,
            CompromissosRecorrentesModel compromissoRecorrente){

        List<String> errosIdentificados = new ArrayList<>();

        boolean inconformidadeDiaDeInicio = horario.getInicioDiaEspecificoMes() > 0
                || horario.getInicioDiaEspecificoMes() <= 31;

        if (inconformidadeDiaDeInicio){
            errosIdentificados.add("O Dia Do Mes De Inicio Deve Ser Maior Que 0 E Menor Ou Igual ADMIN 31");
        }

        boolean inconformidadeDiaDeFim = horario.getFimDiaEspecificoMes() > 0
                || horario.getFimDiaEspecificoMes() <= 31;

        if (inconformidadeDiaDeFim){
            errosIdentificados.add("O Dia Do Mes De Fim Deve Ser Maior Que 0 E Menor Ou Igual ADMIN 31");
        }

        if (horario.getFimDiaEspecificoMes() < horario.getInicioDiaEspecificoMes()){
            errosIdentificados.add("O Dia De Fim Do Horario Não Pode Ser Antes Do Dia De Inicio");
        }

        boolean inconformidade_Inicio_Fim =
                horario.getInicioDiaEspecificoMes().equals(horario.getFimDiaEspecificoMes())
                        && horario.getHoraInicio().isAfter(horario.getHoraFim());

        if (inconformidade_Inicio_Fim){
            errosIdentificados
                    .add("O Fim Do Horario Não Pode Ser Antes Do Inicio" +
                            " Para Horarios Que O Fim é No Mesmo Dia Que O Inicio\n");
        }

        List<HorariosDiaEspecificoMensal> listaHorarios = compromissoRecorrente.getHorariosPorDia().stream()
                .map(HorariosDiaEspecificoMensal.class::cast)
                .toList();

        boolean haConflitos = verificarConflitosComHorarioNaLista(horario,listaHorarios);

        if(haConflitos){
            errosIdentificados.add("Esse horário conflita com outros já criados no mesmo Compromisso Recorrente\n");
        }

        return errosIdentificados;
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
