package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosModel;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOCreateCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOCreateHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOSaidaHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOUpdateHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.HorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia.MapperHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.RepositoriesHorariosPorDia.HorariosFrequenciaSemanalRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.MapperCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class HorariosFrequenciaSemanalService extends HorariosServiceBase{

    private static final int PASSADESEMANA = 7;

    private final HorariosFrequenciaSemanalRepository horariosFrequenciaSemanalRepository;

    private final MapperHorariosFrequenciaSemanal mapperHorariosFrequenciaSemanal;

    public HorariosFrequenciaSemanalService(CompromissosRecorrentesRepository compromissosRecorrentesRepository, MapperCompromissosRecorrentes mapperCompromissosRecorrentes, CompromissosService compromissosService, HorariosFrequenciaSemanalRepository horariosFrequenciaSemanalRepository, MapperHorariosFrequenciaSemanal mapperHorariosFrequenciaSemanal) {
        super(compromissosRecorrentesRepository, mapperCompromissosRecorrentes, compromissosService);
        this.horariosFrequenciaSemanalRepository = horariosFrequenciaSemanalRepository;
        this.mapperHorariosFrequenciaSemanal = mapperHorariosFrequenciaSemanal;
    }

    @Transactional
    public DTOSaidaHorariosFrequenciaSemanal adicionarHorario(CompromissosRecorrentesModel compromissoRecorrente, DTOCreateHorariosFrequenciaSemanal dtoHorario){
        HorariosFrequenciaSemanal horariosCriado = mapperHorariosFrequenciaSemanal.mapToModel(dtoHorario);

        horariosCriado.setCompromissoRecorrente(compromissoRecorrente);

        List<HorariosFrequenciaSemanal> listaHorarios = compromissoRecorrente.getHorariosPorDias().stream()
                .map(HorariosFrequenciaSemanal.class::cast)
                .toList();

        boolean haConflitos = verificarConflitosComHorarioNaLista(horariosCriado,listaHorarios);

        if(haConflitos){
            throw new BadRequestException("Esse horário conflita com outros já criados no mesmo Compromisso Recorrente");
        }

        horariosFrequenciaSemanalRepository.save(horariosCriado);

        criarCompromissosDiretamentePorFrequenciaSemanal(compromissoRecorrente,horariosCriado);

        return mapperHorariosFrequenciaSemanal.mapToDto(horariosCriado);
    }

    @Transactional
    public DTOSaidaHorariosFrequenciaSemanal alterarHorario(CompromissosRecorrentesModel compromissoRecorrente, Long horarioId, DTOUpdateHorariosFrequenciaSemanal dtoUpdateHorario){
        HorariosFrequenciaSemanal horarioParaAtualizar = horariosFrequenciaSemanalRepository.findById(horarioId)
                .orElseThrow(()-> new ResourceNotFindException("Esse id não foi achado nesse tipo de horário"));

        mapperHorariosFrequenciaSemanal.atualizacao(dtoUpdateHorario,horarioParaAtualizar);

        List<HorariosFrequenciaSemanal> outrosHorarios = compromissoRecorrente.getHorariosPorDias().stream()
                .filter(h -> !h.getId().equals(horarioId))
                .map(HorariosFrequenciaSemanal.class::cast)
                .toList();

        boolean haConflitos = verificarConflitosComHorarioNaLista(horarioParaAtualizar,outrosHorarios);

        if(haConflitos){
            throw new BadRequestException("Esse horário conflita com outros já criados no mesmo Compromisso Recorrente");
        }

        apagarCompromissosAtreladosAoHorarioPorDia(horarioParaAtualizar);

        horariosFrequenciaSemanalRepository.save(horarioParaAtualizar);

        criarCompromissosDiretamentePorFrequenciaSemanal(compromissoRecorrente,horarioParaAtualizar);

        return mapperHorariosFrequenciaSemanal.mapToDto(horarioParaAtualizar);
    }

    @Transactional
    public List<DTORespostaCompromisso> criarCompromissosPorRecorrenciaFrequenciaSemanal (CompromissosRecorrentesModel compromissoRecorrente){
        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();

        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long intervalo = compromissoRecorrente.getIntervalo();

        return compromissoRecorrente.getHorariosPorDias().stream()
                .map(HorariosFrequenciaSemanal.class::cast)
                .flatMap(horariosFrequenciaSemanal -> criarCompromissosPorFrequenciaSemanal(compromissoRecorrente,
                        horariosFrequenciaSemanal,inicioRecorrencia,fimRecorrencia,intervalo)
                        .stream())
                .toList();
    }

    @Transactional
    private void criarCompromissosDiretamentePorFrequenciaSemanal(CompromissosRecorrentesModel compromissoRecorrente, HorariosFrequenciaSemanal horario){

        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();

        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long intervalo = compromissoRecorrente.getIntervalo();

        criarCompromissosPorFrequenciaSemanal(compromissoRecorrente,horario,inicioRecorrencia,
                fimRecorrencia,intervalo);
    }

    @Transactional
    private List<DTORespostaCompromisso> criarCompromissosPorFrequenciaSemanal(
            CompromissosRecorrentesModel compromissoRecorrente,
            HorariosFrequenciaSemanal horario,
            LocalDate inicioRecorrencia,LocalDate fimRecorrencia, long intervalo){

        DayOfWeek diaDaSemanaInicioCompromisso = horario.getDiaDaSemanaInicio();

        DayOfWeek diaDaSemanaFimCompromisso = horario.getDiaDaSemanaFim();

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

        List<DTORespostaCompromisso> compromissosGerados = new ArrayList<>();

        for (LocalDate dataBase = primeiroDiaValido ;!dataBase.isAfter(fimRecorrencia);
             dataBase = dataBase.plusWeeks(intervalo)){

            LocalDateTime inicioCompromisso = dataBase.atTime(horario.getHoraInicio());
            LocalDateTime fimCompromisso = dataBase.plusDays(diferencaComeco_Fim).atTime(horario.getHoraFim());

            DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                    .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);

            compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));
        }
        return compromissosGerados;
    }

    @Transactional
    public Long apagarCompromissosAtreladosAoHorarioPorDia(HorariosFrequenciaSemanal horariosPorDia){
        CompromissosRecorrentesModel compromissosRecorrentesAtrelado = horariosPorDia.getCompromissoRecorrente();

        List<CompromissosModel> listaDosCompromissos = compromissosRecorrentesAtrelado.getCompromissosGerados();

        long numeroCompromissosApagados = 0;

        for(CompromissosModel compromisso : listaDosCompromissos){
            DayOfWeek diaDaSemanaDoCompromisso = compromisso.getInicio().getDayOfWeek();
            LocalTime horarioInicioCompromisso = compromisso.getInicio().toLocalTime();

            boolean compromissoFoiGeradoPeloHorario = diaDaSemanaDoCompromisso.equals(horariosPorDia.getDiaDaSemanaInicio())
                    && horarioInicioCompromisso.equals(horariosPorDia.getHoraInicio());

            if(compromissoFoiGeradoPeloHorario){
                compromissosService.deletarCompromissoPorId(compromisso.getId());

                numeroCompromissosApagados++;
            }

        }
        return numeroCompromissosApagados;
    }


    private boolean verificarConflitoEntreHorariosUsamDiasDaSemana(HorariosFrequenciaSemanal horario_1, HorariosFrequenciaSemanal horario_2) {

        Set<DayOfWeek> dias1 = getDiasDaSemanaEntre(horario_1.getDiaDaSemanaInicio(), horario_1.getDiaDaSemanaFim());
        Set<DayOfWeek> dias2 = getDiasDaSemanaEntre(horario_2.getDiaDaSemanaInicio(), horario_2.getDiaDaSemanaFim());

        boolean intersecaoDias = dias1.stream().anyMatch(dias2::contains);

        boolean horariosConflitam = horario_1.getHoraInicio().isBefore(horario_2.getHoraFim())
                && horario_1.getHoraFim().isAfter(horario_2.getHoraInicio());

        return intersecaoDias && horariosConflitam;
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

    protected boolean verificarConflitosComHorarioNaLista(HorariosFrequenciaSemanal horario, List<HorariosFrequenciaSemanal> listaHorariosPorDia){
        return listaHorariosPorDia.stream()
                .anyMatch(h -> verificarConflitoEntreHorariosUsamDiasDaSemana(h,horario) &&
                        !Objects.equals(h.getId(), horario.getId()));
    }
}
