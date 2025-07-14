package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.ServicesHorariosPorDia;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosModel;
import kisiolar.filipe.Viviane.Ai.Compromissos.CompromissosService;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOCreateCompromissos;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTORespostaHorariosPorDia;
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
    public DTORespostaHorariosPorDia adicionarHorario(CompromissosRecorrentesModel compromissoRecorrente, DTOCreateHorariosFrequenciaSemanal dtoHorario){
        HorariosFrequenciaSemanal horariosCriado = mapperHorariosFrequenciaSemanal.mapToModel(dtoHorario);

        horariosCriado.setCompromissoRecorrente(compromissoRecorrente);

        List<String> errosIdentificados = verificarValidadeDasInformacoes(horariosCriado,compromissoRecorrente);

        if (!errosIdentificados.isEmpty()){
            throw new BadRequestException("erros identificados no Horario :" + horariosCriado + "\n"+ errosIdentificados);
        }

        horariosFrequenciaSemanalRepository.save(horariosCriado);

        List<DTORespostaCompromisso> compromissosGerados =
                criarCompromissosDiretamentePorFrequenciaSemanal(compromissoRecorrente,horariosCriado);

        DTOSaidaHorariosFrequenciaSemanal dtoSaida = mapperHorariosFrequenciaSemanal.mapToDto(horariosCriado);

        return new DTORespostaHorariosPorDia(dtoSaida,compromissosGerados);
    }

    @Transactional
    public DTORespostaHorariosPorDia alterarHorario(CompromissosRecorrentesModel compromissoRecorrente, Long horarioId, DTOUpdateHorariosFrequenciaSemanal dtoUpdateHorario){
        HorariosFrequenciaSemanal horarioParaAtualizar = horariosFrequenciaSemanalRepository.findById(horarioId)
                .orElseThrow(()-> new ResourceNotFindException("Esse id não foi achado nesse tipo de horário"));

        LocalTime inicioHorarioAntigo = horarioParaAtualizar.getHoraInicio();
        DayOfWeek diaDaSemanaAntigo = horarioParaAtualizar.getDiaDaSemanaInicio();

        HorariosFrequenciaSemanal horarioComDadosAntigos = new HorariosFrequenciaSemanal();
        horarioComDadosAntigos.setHoraInicio(inicioHorarioAntigo);
        horarioComDadosAntigos.setDiaDaSemanaInicio(diaDaSemanaAntigo);
        horarioComDadosAntigos.setCompromissoRecorrente(compromissoRecorrente);

        mapperHorariosFrequenciaSemanal.atualizacao(dtoUpdateHorario,horarioParaAtualizar);

        List<String> errosIdentificados = verificarValidadeDasInformacoes(horarioParaAtualizar,compromissoRecorrente);

        if (!errosIdentificados.isEmpty()){
            throw new BadRequestException("erros identificados no Horario :" + horarioParaAtualizar + "\n"+ errosIdentificados);
        }

        apagarCompromissosAtreladosAoHorarioPorDia(horarioComDadosAntigos);

        horariosFrequenciaSemanalRepository.save(horarioParaAtualizar);

        List<DTORespostaCompromisso> compromissosGerados =
                criarCompromissosDiretamentePorFrequenciaSemanal(compromissoRecorrente,horarioParaAtualizar);

        DTOSaidaHorariosFrequenciaSemanal dtoSaida = mapperHorariosFrequenciaSemanal.mapToDto(horarioParaAtualizar);

        return new DTORespostaHorariosPorDia(dtoSaida,compromissosGerados);
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
    private List<DTORespostaCompromisso> criarCompromissosDiretamentePorFrequenciaSemanal(CompromissosRecorrentesModel compromissoRecorrente, HorariosFrequenciaSemanal horario){

        LocalDate inicioRecorrencia = compromissoRecorrente.getDataInicioRecorrencia();

        LocalDate fimRecorrencia = compromissoRecorrente.getDataFimRecorrencia();

        long intervalo = compromissoRecorrente.getIntervalo();

        return criarCompromissosPorFrequenciaSemanal(compromissoRecorrente,horario,inicioRecorrencia,
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

        long tamanhoInicial = listaDosCompromissos.size();

        DayOfWeek diaDaSemanaHorario = horariosPorDia.getDiaDaSemanaInicio();

        LocalTime inicioHorario = horariosPorDia.getHoraInicio();

        listaDosCompromissos.removeIf(c -> c.getInicio().getDayOfWeek().equals(diaDaSemanaHorario) &&
                c.getInicio().toLocalTime().equals(inicioHorario));

        long numeroCompromissosApagados = tamanhoInicial - listaDosCompromissos.size();

        return numeroCompromissosApagados;
    }

    @Transactional
    private List<String> verificarValidadeDasInformacoes(HorariosFrequenciaSemanal horario,CompromissosRecorrentesModel compromissoRecorrente){
        List<String> errosIdentificados = new ArrayList<>();

        boolean inconformidade_Inicio_Fim = horario.getDiaDaSemanaInicio().equals(horario.getDiaDaSemanaFim())
        && horario.getHoraInicio().isAfter(horario.getHoraFim());

        if (inconformidade_Inicio_Fim){
            errosIdentificados
                    .add("O Fim Do Horario Nao Pode Ser Antes Do Inicio" +
                            " Para Horarios Que O Fim é No Mesmo Dia Que O Inicio\n");
        }

        List<HorariosFrequenciaSemanal> listaHorarios = compromissoRecorrente.getHorariosPorDias().stream()
                .map(HorariosFrequenciaSemanal.class::cast)
                .toList();

        boolean haConflitos = verificarConflitosComHorarioNaLista(horario,listaHorarios);

        if(haConflitos){
            errosIdentificados.add("Esse horário conflita com outros já criados no mesmo Compromisso Recorrente\n");
        }

        return errosIdentificados;
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
