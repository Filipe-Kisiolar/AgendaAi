package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosModel;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOCreateCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaDiaria.DTOCreateHorariosFrequenciaDiaria;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaDiaria.DTOSaidaHorariosFrequenciaDiaria;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaDiaria.DTOUpdateHorariosFrequenciaDiaria;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels.HorariosFrequenciaDiaria;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia.MapperHorariosFrequenciaDiaria;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.RepositoriesHorariosPorDia.HorariosFrequenciaDiariaRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.MapperCompromissosRecorrentes;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class HorariosFrequenciaDiariaService extends HorariosServiceBase {

    private final HorariosFrequenciaDiariaRepository horariosFrequenciaDiariaRepository;

    private final MapperHorariosFrequenciaDiaria mapperHorariosFrequenciaDiaria;

    public HorariosFrequenciaDiariaService(CompromissosRecorrentesRepository compromissosRecorrentesRepository, MapperCompromissosRecorrentes mapperCompromissosRecorrentes, CompromissosService compromissosService, HorariosFrequenciaDiariaRepository horariosFrequenciaDiariaRepository, MapperHorariosFrequenciaDiaria mapperHorariosFrequenciaDiaria) {
        super(compromissosRecorrentesRepository, mapperCompromissosRecorrentes, compromissosService);
        this.horariosFrequenciaDiariaRepository = horariosFrequenciaDiariaRepository;
        this.mapperHorariosFrequenciaDiaria = mapperHorariosFrequenciaDiaria;
    }

    @Transactional
    public DTOSaidaHorariosFrequenciaDiaria adicionarHorario(CompromissosRecorrentesModel compromissoRecorrente, DTOCreateHorariosFrequenciaDiaria dtoHorario){
        HorariosFrequenciaDiaria horariosCriado = mapperHorariosFrequenciaDiaria.mapToModel(dtoHorario);

        horariosCriado.setCompromissoRecorrente(compromissoRecorrente);

        List<HorariosFrequenciaDiaria> listaHorarios = compromissoRecorrente.getHorariosPorDias().stream()
                .map(HorariosFrequenciaDiaria.class::cast)
                .toList();

        boolean haConflitos = verificarConflitosComHorarioNaLista(horariosCriado,listaHorarios);

        if(haConflitos){
            throw new BadRequestException("Esse horário conflita com outros já criados no mesmo Compromisso Recorrente");
        }

        horariosFrequenciaDiariaRepository.save(horariosCriado);

        criarCompromissosDiretamentePorFrequenciaDiaria(compromissoRecorrente,horariosCriado);

        return mapperHorariosFrequenciaDiaria.mapToDto(horariosCriado);
    }

    @Transactional
    public DTOSaidaHorariosFrequenciaDiaria alterarHorario(CompromissosRecorrentesModel compromissoRecorrente, Long horarioId, DTOUpdateHorariosFrequenciaDiaria dtoUpdateHorario){
        HorariosFrequenciaDiaria horarioParaAtualizar = horariosFrequenciaDiariaRepository.findById(horarioId)
                .orElseThrow(()-> new ResourceNotFindException("Esse id não foi achado nesse tipo de horário"));

        mapperHorariosFrequenciaDiaria.atualizacao(dtoUpdateHorario,horarioParaAtualizar);

        List<HorariosFrequenciaDiaria> outrosHorarios = compromissoRecorrente.getHorariosPorDias().stream()
                .filter(h -> !h.getId().equals(horarioId))
                .map(HorariosFrequenciaDiaria.class::cast)
                .toList();

        boolean haConflitos = verificarConflitosComHorarioNaLista(horarioParaAtualizar,outrosHorarios);

        if(haConflitos){
            throw new BadRequestException("Esse horário conflita com outros já criados no mesmo Compromisso Recorrente");
        }

        apagarCompromissosAtreladosAoHorarioPorDia(horarioParaAtualizar);

        horariosFrequenciaDiariaRepository.save(horarioParaAtualizar);

        criarCompromissosDiretamentePorFrequenciaDiaria(compromissoRecorrente,horarioParaAtualizar);

        return mapperHorariosFrequenciaDiaria.mapToDto(horarioParaAtualizar);
    }

    @Transactional
    public List<DTORespostaCompromisso> criarCompromissosPorRecorrenciaFrequenciaDiaria(CompromissosRecorrentesModel compromissoRecorrente){

        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();

        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long intervalo = compromissoRecorrente.getIntervalo();

        return compromissoRecorrente.getHorariosPorDias().stream()
                .map(HorariosFrequenciaDiaria.class::cast)
                .flatMap(horario -> criarCompromissosPorFrequenciaDiaria(compromissoRecorrente,
                        horario,inicioRecorrencia,fimRecorrencia,intervalo).stream())
                .toList();
    }

    @Transactional
    public void criarCompromissosDiretamentePorFrequenciaDiaria(CompromissosRecorrentesModel compromissoRecorrente,HorariosFrequenciaDiaria horario){
        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();

        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long intervalo = compromissoRecorrente.getIntervalo();

        criarCompromissosPorFrequenciaDiaria(compromissoRecorrente,horario,
                inicioRecorrencia,fimRecorrencia,intervalo);
    }

    @Transactional
    private List<DTORespostaCompromisso> criarCompromissosPorFrequenciaDiaria(
            CompromissosRecorrentesModel compromissoRecorrente,
            HorariosFrequenciaDiaria horario,
            LocalDate inicioRecorrencia,LocalDate fimRecorrencia, long intervalo){

        LocalTime horaInicio = horario.getHoraInicio();
        LocalTime horaFim = horario.getHoraFim();

        boolean terminaNoDiaSeguinte = horaInicio.isAfter(horaFim);

        long i = 0;

        List<DTORespostaCompromisso> compromissosGerados = new ArrayList<>();
        if(terminaNoDiaSeguinte){
            while (!inicioRecorrencia.plusDays(i).isAfter(fimRecorrencia)) {

                LocalDateTime inicioCompromisso = inicioRecorrencia.plusDays(i).atTime(horaInicio);
                LocalDateTime fimCompromisso = inicioCompromisso.toLocalDate()
                        .plusDays(1).atTime(horaFim);

                DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                        .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);

                compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));

                i = i + intervalo;
            }
        }else{
            while (!inicioRecorrencia.plusDays(i).isAfter(fimRecorrencia)) {

                LocalDateTime inicioCompromisso = inicioRecorrencia.plusDays(i).atTime(horaInicio);
                LocalDateTime fimCompromisso = inicioCompromisso.toLocalDate().atTime(horaFim);

                DTOCreateCompromissos dtoCreateCompromissos = mapperCompromissosRecorrentes
                        .mapGerarCompromisso(compromissoRecorrente, inicioCompromisso, fimCompromisso);

                compromissosGerados.add(compromissosService.criarCompromisso(dtoCreateCompromissos));

                i = i + intervalo;
            }
        }
        return compromissosGerados;
    }

    @Transactional
    public Long apagarCompromissosAtreladosAoHorarioPorDia(HorariosFrequenciaDiaria horariosPorDia){
        CompromissosRecorrentesModel compromissosRecorrentesAtrelado = horariosPorDia.getCompromissoRecorrente();

        List<CompromissosModel> listaDosCompromissos = compromissosRecorrentesAtrelado.getCompromissosGerados();

        long numeroCompromissosApagados = 0;

        for(CompromissosModel compromisso : listaDosCompromissos){

            LocalTime horarioInicioCompromisso = compromisso.getInicio().toLocalTime();

            boolean compromissoFoiGeradoPeloHorario = horarioInicioCompromisso.equals(horariosPorDia.getHoraInicio());

            if(compromissoFoiGeradoPeloHorario){

                compromissosService.deletarCompromissoPorId(compromisso.getId());

                numeroCompromissosApagados++;
            }

        }
        return numeroCompromissosApagados;
    }

    public boolean verificarConflitoEntreHorariosDiarios(HorariosFrequenciaDiaria horariosPorDia_1, HorariosFrequenciaDiaria horariosPorDia_2){
        LocalDate baseParaVerificacao = LocalDate.of(2000, 1, 1);

        LocalDateTime h1Inicio = baseParaVerificacao.atTime(horariosPorDia_1.getHoraInicio());
        LocalDateTime h1Fim = horariosPorDia_1.getHoraFim().isAfter(horariosPorDia_1.getHoraInicio())
                ? baseParaVerificacao.atTime(horariosPorDia_1.getHoraFim())
                : baseParaVerificacao.plusDays(1).atTime(horariosPorDia_1.getHoraFim());

        LocalDateTime h2Inicio = baseParaVerificacao.atTime(horariosPorDia_2.getHoraInicio());
        LocalDateTime h2Fim = horariosPorDia_2.getHoraFim().isAfter(horariosPorDia_2.getHoraInicio())
                ? baseParaVerificacao.atTime(horariosPorDia_2.getHoraFim())
                : baseParaVerificacao.plusDays(1).atTime(horariosPorDia_2.getHoraFim());

        return h1Inicio.isBefore(h2Fim) && h1Fim.isAfter(h2Inicio);
    }

    public boolean verificarConflitosComHorarioNaLista(HorariosFrequenciaDiaria horario, List<HorariosFrequenciaDiaria> listaHorariosPorDia){
        return listaHorariosPorDia.stream()
                .anyMatch(h -> verificarConflitoEntreHorariosDiarios(h,horario) &&
                    !Objects.equals(h.getId(), horario.getId()));
    }
}
