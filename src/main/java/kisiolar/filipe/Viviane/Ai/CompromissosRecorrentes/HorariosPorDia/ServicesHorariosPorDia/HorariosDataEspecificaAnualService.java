package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosModel;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOCreateCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DataEspecificaAnual.DTOCreateHorariosDataEspecificaAnual;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DataEspecificaAnual.DTOSaidaHorariosDataEspecificaAnual;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DataEspecificaAnual.DTOUpdateHorariosDataEspecificaAnual;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.HorariosDataEspecificaAnual;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia.MapperHorariosDataEspecificaAnual;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.RepositoriesHorariosPorDia.HorariosDataEspecificaAnualRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.MapperCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class HorariosDataEspecificaAnualService extends HorariosServiceBase {

    private final HorariosDataEspecificaAnualRepository horariosDataEspecificaAnualRepository;

    private final MapperHorariosDataEspecificaAnual mapperHorariosDataEspecificaAnual;

    public HorariosDataEspecificaAnualService(CompromissosRecorrentesRepository compromissosRecorrentesRepository, MapperCompromissosRecorrentes mapperCompromissosRecorrentes, CompromissosService compromissosService, HorariosDataEspecificaAnualRepository horariosDataEspecificaAnualRepository, MapperHorariosDataEspecificaAnual mapperHorariosDataEspecificaAnual) {
        super(compromissosRecorrentesRepository, mapperCompromissosRecorrentes, compromissosService);
        this.horariosDataEspecificaAnualRepository = horariosDataEspecificaAnualRepository;
        this.mapperHorariosDataEspecificaAnual = mapperHorariosDataEspecificaAnual;
    }

    @Transactional
    public DTOSaidaHorariosDataEspecificaAnual adicionarHorario(CompromissosRecorrentesModel compromissoRecorrente, DTOCreateHorariosDataEspecificaAnual dtoHorario){
        HorariosDataEspecificaAnual horariosCriado = mapperHorariosDataEspecificaAnual.mapToModel(dtoHorario);

        horariosCriado.setCompromissoRecorrente(compromissoRecorrente);

        List<HorariosDataEspecificaAnual> listaHorarios = compromissoRecorrente.getHorariosPorDias().stream()
                .map(HorariosDataEspecificaAnual.class::cast)
                .toList();

        boolean haConflitos = verificarConflitosComHorarioNaLista(horariosCriado,listaHorarios);

        if(haConflitos){
            throw new BadRequestException("Esse horário conflita com outros já criados no mesmo Compromisso Recorrente");
        }

        horariosDataEspecificaAnualRepository.save(horariosCriado);

        criarCompromissosDiretamentePorDataEspecificaAnual(compromissoRecorrente,horariosCriado);

        return mapperHorariosDataEspecificaAnual.mapToDto(horariosCriado);
    }

    @Transactional
    public DTOSaidaHorariosDataEspecificaAnual alterarHorario(CompromissosRecorrentesModel compromissoRecorrente, Long horarioId, DTOUpdateHorariosDataEspecificaAnual dtoUpdateHorario){
        HorariosDataEspecificaAnual horarioParaAtualizar = horariosDataEspecificaAnualRepository.findById(horarioId)
                .orElseThrow(()-> new ResourceNotFindException("Esse id não foi achado nesse tipo de horário"));

        MonthDay dataAnualAntigo = horarioParaAtualizar.getInicioDataEspecificaDoAno();
        LocalTime inicioHorarioAntigo = horarioParaAtualizar.getHoraInicio();

        HorariosDataEspecificaAnual horarioAntigo = new HorariosDataEspecificaAnual();
        horarioAntigo.setInicioDataEspecificaDoAno(dataAnualAntigo);
        horarioAntigo.setHoraInicio(inicioHorarioAntigo);
        horarioAntigo.setCompromissoRecorrente(compromissoRecorrente);

        mapperHorariosDataEspecificaAnual.atualizacao(dtoUpdateHorario,horarioParaAtualizar);

        List<HorariosDataEspecificaAnual> outrosHorarios = compromissoRecorrente.getHorariosPorDias().stream()
                .filter(h -> !h.getId().equals(horarioId))
                .map(HorariosDataEspecificaAnual.class::cast)
                .toList();

        boolean haConflitos = verificarConflitosComHorarioNaLista(horarioParaAtualizar,outrosHorarios);

        if(haConflitos){
            throw new BadRequestException("Esse horário conflita com outros já criados no mesmo Compromisso Recorrente");
        }

        apagarCompromissosAtreladosAoHorarioPorDia(horarioAntigo);

        horariosDataEspecificaAnualRepository.save(horarioParaAtualizar);

        criarCompromissosDiretamentePorDataEspecificaAnual(compromissoRecorrente,horarioParaAtualizar);

        return mapperHorariosDataEspecificaAnual.mapToDto(horarioParaAtualizar);
    }

    @Transactional
    public List<DTORespostaCompromisso> criarCompromissosPorRecorrenciaDataEspecificaAnual (CompromissosRecorrentesModel compromissoRecorrente){
        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();
        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long intervalo = compromissoRecorrente.getIntervalo();

        int anoInicio = inicioRecorrencia.getYear();
        int anoFim = fimRecorrencia.getYear();

        return compromissoRecorrente.getHorariosPorDias().stream()
                .map(HorariosDataEspecificaAnual.class::cast)
                .flatMap(horario -> criarCompromissosPorDataEspecificaAnual(compromissoRecorrente,horario,
                        intervalo,anoInicio,anoFim,fimRecorrencia).stream())
                .toList();
    }

    @Transactional
    private void criarCompromissosDiretamentePorDataEspecificaAnual(CompromissosRecorrentesModel compromissoRecorrente, HorariosDataEspecificaAnual horario){
        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();
        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long intervalo = compromissoRecorrente.getIntervalo();

        int anoInicio = inicioRecorrencia.getYear();
        int anoFim = fimRecorrencia.getYear();


        criarCompromissosPorDataEspecificaAnual(compromissoRecorrente,horario,
                intervalo,anoInicio,anoFim,fimRecorrencia);
    }

    @Transactional
    private List<DTORespostaCompromisso> criarCompromissosPorDataEspecificaAnual(
            CompromissosRecorrentesModel compromissoRecorrente,
            HorariosDataEspecificaAnual horario,
            long intervalo ,int anoInicio,int anoFim,LocalDate fimRecorrencia
            ){

        MonthDay diaDoAnoInicio = horario.getInicioDataEspecificaDoAno();

        MonthDay diaDoAnoFim = horario.getFimDataEspecificaDoAno();

        List<DTORespostaCompromisso> compromissosGerados = new ArrayList<>();
        int i = 0;
        int anoAtual;
        do {
            anoAtual =(int)(anoInicio + i * intervalo);

            int diaInicio = Math.min(diaDoAnoInicio.getDayOfMonth(),
                    YearMonth.of(anoAtual, diaDoAnoInicio.getMonth()).lengthOfMonth());
            int diaFim = Math.min(diaDoAnoFim.getDayOfMonth(),
                    YearMonth.of(anoAtual, diaDoAnoFim.getMonth()).lengthOfMonth());

            LocalDateTime inicioCompromisso = LocalDate.of(anoAtual, diaDoAnoInicio.getMonth(), diaInicio)
                    .atTime(horario.getHoraInicio());
            LocalDateTime fimCompromisso = LocalDate.of(anoAtual, diaDoAnoFim.getMonth(), diaFim)
                    .atTime(horario.getHoraFim());

            if (inicioCompromisso.toLocalDate().isAfter(fimRecorrencia)) break;

            DTOCreateCompromissos dto = mapperCompromissosRecorrentes
                    .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);
            compromissosGerados.add(compromissosService.criarCompromisso(dto));

            i++;
        }while ( anoAtual < anoFim );


        return compromissosGerados;
    }

    @Transactional
    public Long apagarCompromissosAtreladosAoHorarioPorDia(HorariosDataEspecificaAnual horariosPorDia){
        CompromissosRecorrentesModel compromissosRecorrentesAtrelado = horariosPorDia.getCompromissoRecorrente();

        List<CompromissosModel> listaDosCompromissos = compromissosRecorrentesAtrelado.getCompromissosGerados();

        long tamanhoInicial = listaDosCompromissos.size();

        Month mesDoHorario = horariosPorDia.getInicioDataEspecificaDoAno().getMonth();
        int diaDoMesHorario = horariosPorDia.getInicioDataEspecificaDoAno().getDayOfMonth();
        LocalTime inicioHorario = horariosPorDia.getHoraInicio();

        listaDosCompromissos.removeIf(
                c -> c.getInicio().getMonth().equals(mesDoHorario)
                        && c.getInicio().getDayOfMonth() == diaDoMesHorario
                        && c.getInicio().toLocalTime().equals(inicioHorario)
        );

        long numeroCompromissosApagados = tamanhoInicial - listaDosCompromissos.size();

        return numeroCompromissosApagados;
    }

    public boolean verificarConflitoEntreHorariosDiaEspecificoAnual(HorariosDataEspecificaAnual h1, HorariosDataEspecificaAnual h2) {
        int ano = LocalDate.now().getYear();

        int diaInicioSeguro_1 = Math.min(
                h1.getInicioDataEspecificaDoAno().getDayOfMonth(),
                YearMonth.of(ano, h1.getInicioDataEspecificaDoAno().getMonth()).lengthOfMonth()
        );
        int diaFimSeguro_1 = Math.min(
                h1.getFimDataEspecificaDoAno().getDayOfMonth(),
                YearMonth.of(ano, h1.getFimDataEspecificaDoAno().getMonth()).lengthOfMonth()
        );
        LocalDateTime inicio1 = LocalDate.of(ano, h1.getInicioDataEspecificaDoAno().getMonth(), diaInicioSeguro_1)
                .atTime(h1.getHoraInicio());
        LocalDateTime fim1 = LocalDate.of(ano, h1.getFimDataEspecificaDoAno().getMonth(), diaFimSeguro_1)
                .atTime(h1.getHoraFim());

        int diaInicioSeguro_2 = Math.min(
                h2.getInicioDataEspecificaDoAno().getDayOfMonth(),
                YearMonth.of(ano, h2.getInicioDataEspecificaDoAno().getMonth()).lengthOfMonth()
        );
        int diaFimSeguro_2 = Math.min(
                h2.getFimDataEspecificaDoAno().getDayOfMonth(),
                YearMonth.of(ano, h2.getFimDataEspecificaDoAno().getMonth()).lengthOfMonth()
        );
        LocalDateTime inicio2 = LocalDate.of(ano, h2.getInicioDataEspecificaDoAno().getMonth(), diaInicioSeguro_2)
                .atTime(h2.getHoraInicio());
        LocalDateTime fim2 = LocalDate.of(ano, h2.getFimDataEspecificaDoAno().getMonth(), diaFimSeguro_2)
                .atTime(h2.getHoraFim());

        return inicio1.isBefore(fim2) && fim1.isAfter(inicio2);
    }


    public boolean verificarConflitosComHorarioNaLista(HorariosDataEspecificaAnual horario, List<HorariosDataEspecificaAnual> listaHorarios){
        return listaHorarios.stream()
                .anyMatch(h -> verificarConflitoEntreHorariosDiaEspecificoAnual(h,horario) &&
                        !Objects.equals(h.getId(), horario.getId()));
    }
}
