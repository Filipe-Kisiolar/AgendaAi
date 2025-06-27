package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia;

import jakarta.transaction.Transactional;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOSaidaCompromissos;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesRepository;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesService;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.MappersHorariosPorDia.MapperHorariosPorDia;
import kisiolar.filipe.Viviane.Ai.Exceptions.BadRequestException;
import kisiolar.filipe.Viviane.Ai.Exceptions.ResourceNotFindException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HorariosPorDiaService {

    @Autowired
    HorariosPorDiaRepository horariosPorDiaRepository;

    @Autowired
    CompromissosRecorrentesRepository compromissosRecorrentesRepository;

    @Autowired
    MapperHorariosPorDia mapperHorariosPorDia;

    private CompromissosRecorrentesService compromissosRecorrentesService;

    public void setCompromissosRecorrentesService(CompromissosRecorrentesService service) {
        this.compromissosRecorrentesService = service;
    }

    @Transactional
    public DTOSaidaHorariosPorDia adicionarHorario(Long compromissoRecorrenteId, DTOCreateHorariosPorDia dtoCreateHorariosPorDia){
        HorariosPorDiaModel horariosCriado = mapperHorariosPorDia.mapToModel(dtoCreateHorariosPorDia);

        CompromissosRecorrentesModel compromissoRecorrente = compromissosRecorrentesRepository.findById(compromissoRecorrenteId)
                .orElseThrow(() -> new ResourceNotFindException("Compromisso recorrente não encontrado"));

        horariosCriado.setCompromissoRecorrente(compromissoRecorrente);

        boolean haConflitos = verificarConflitosComHorarioNaLista(compromissoRecorrente,horariosCriado,compromissoRecorrente.getHorariosPorDias());

        if(haConflitos){
            throw new BadRequestException("Esse horário conflita com outros já criados no mesmo Compromisso Recorrente");
        }

        horariosPorDiaRepository.save(horariosCriado);

        compromissosRecorrentesService.criarCompromissosDiretamentePorHorariosPorDia(horariosCriado);

        return mapperHorariosPorDia.mapToDto(horariosCriado);
    }

    @Transactional
    public DTOSaidaHorariosPorDia alterarHorario(Long compromissoRecorrenteId,Long horarioId,DTOUpdateHorariosPorDia dtoUpdateHorariosPorDia){

        CompromissosRecorrentesModel compromissoRecorrente = compromissosRecorrentesRepository.findById(compromissoRecorrenteId)
                .orElseThrow(() -> new ResourceNotFindException("Compromisso recorrente não encontrado"));

        HorariosPorDiaModel horariosPorDia = compromissoRecorrente.getHorariosPorDias().stream()
                .filter(h -> h.getId().equals(horarioId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFindException("Horário não encontrado nesse compromisso"));

        mapperHorariosPorDia.atualizacao(dtoUpdateHorariosPorDia, horariosPorDia);

        List<HorariosPorDiaModel> outrosHorarios = compromissoRecorrente.getHorariosPorDias()
                .stream()
                .filter(h -> !h.getId().equals(horarioId))
                .toList();

        boolean haConflitos = verificarConflitosComHorarioNaLista(compromissoRecorrente,horariosPorDia,outrosHorarios);

        if(haConflitos){
            throw new BadRequestException("Esse horário conflita com outros já criados no mesmo Compromisso Recorrente");
        }

        compromissosRecorrentesService.apagarCompromissosAtreladosAoHorarioPorDia(horariosPorDia);

        horariosPorDiaRepository.save(horariosPorDia);

        compromissosRecorrentesService.criarCompromissosDiretamentePorHorariosPorDia(horariosPorDia);

        return mapperHorariosPorDia.mapToDto(horariosPorDia);
    }

    @Transactional
    public List<DTOSaidaCompromissos> deletarHorarioPorId(Long compromissoRecorrenteId, Long horarioId) {

        CompromissosRecorrentesModel compromissoRecorrente = compromissosRecorrentesRepository.findById(compromissoRecorrenteId)
                .orElseThrow(() -> new ResourceNotFindException("Compromisso recorrente não encontrado"));

        HorariosPorDiaModel horario = compromissoRecorrente.getHorariosPorDias()
                .stream()
                .filter(h -> h.getId().equals(horarioId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Horário não encontrado nesse compromisso"));

        List<DTOSaidaCompromissos> compromissosDeletados = compromissosRecorrentesService.apagarCompromissosAtreladosAoHorarioPorDia(horario);

        compromissoRecorrente.getHorariosPorDias().remove(horario);
        return compromissosDeletados;
    }

    public boolean verificarConflitoEntreHorariosDiarios(HorariosPorDiaModel horariosPorDia_1, HorariosPorDiaModel horariosPorDia_2){

        boolean horariosConflitam= horariosPorDia_1.getHoraInicio().isBefore(horariosPorDia_2.getHoraFim())
                && horariosPorDia_1.getHoraFim().isAfter(horariosPorDia_2.getHoraInicio());

        return horariosConflitam;
    }

    public boolean verificarConflitoEntreHorariosUsamDiasDaSemana(
            HorariosPorDiaModel horario_1, HorariosPorDiaModel horario_2) {

        Set<DayOfWeek> dias1 = getDiasDaSemanaEntre(horario_1.getDiaDaSemanaInicio(), horario_1.getDiaDaSemanaFim());
        Set<DayOfWeek> dias2 = getDiasDaSemanaEntre(horario_2.getDiaDaSemanaInicio(), horario_2.getDiaDaSemanaFim());

        boolean intersecaoDias = dias1.stream().anyMatch(dias2::contains);

        boolean horariosConflitam = verificarConflitoEntreHorariosDiarios(horario_1, horario_2);

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
    public boolean verificarConflitoEntreHorariosDiaEspecificoMensal(HorariosPorDiaModel horariosPorDia_1, HorariosPorDiaModel horariosPorDia_2){
        LocalDateTime inicioHorario_1 = LocalDateTime.of(
                LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                horariosPorDia_1.getInicioDiaEspecificoMes(),
                horariosPorDia_1.getHoraInicio().getHour(),
                horariosPorDia_1.getHoraInicio().getMinute()
        );

        LocalDateTime fimHorario_1 = LocalDateTime.of(
                LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                horariosPorDia_1.getFimDiaEspecificoMes(),
                horariosPorDia_1.getHoraFim().getHour(),
                horariosPorDia_1.getHoraFim().getMinute()
        );

        LocalDateTime inicioHorario_2 = LocalDateTime.of(
                LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                horariosPorDia_2.getInicioDiaEspecificoMes(),
                horariosPorDia_2.getHoraInicio().getHour(),
                horariosPorDia_2.getHoraInicio().getMinute()
        );

        LocalDateTime fimHorario_2 = LocalDateTime.of(
                LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                horariosPorDia_2.getFimDiaEspecificoMes(),
                horariosPorDia_2.getHoraFim().getHour(),
                horariosPorDia_2.getHoraFim().getMinute()
        );

        boolean conflitam = inicioHorario_1.isBefore(fimHorario_2)
                && fimHorario_1.isAfter(inicioHorario_2);

        return conflitam;
    }

    public boolean verificarConflitoEntreHorariosDiaEspecificoAnual(HorariosPorDiaModel horariosPorDia_1, HorariosPorDiaModel horariosPorDia_2){
        MonthDay diaDoMesInicioHorario_1 = horariosPorDia_1.getInicioDataEspecificaDoAno();

        MonthDay diaDoMesFimHorario_1 = horariosPorDia_1.getFimDataEspecificaDoAno();

        LocalDateTime inicioHorario_1 = diaDoMesInicioHorario_1.atYear(LocalDate.now().getYear())
                .atTime(horariosPorDia_1.getHoraInicio());

        LocalDateTime fimHorario_1 = diaDoMesFimHorario_1.atYear(inicioHorario_1.getYear())
                .atTime(horariosPorDia_1.getHoraFim());

        MonthDay diaDoMesInicioHorario_2 = horariosPorDia_2.getInicioDataEspecificaDoAno();

        MonthDay diaDoMesFimHorario_2 = horariosPorDia_2.getFimDataEspecificaDoAno();

        LocalDateTime inicioHorario_2 = diaDoMesInicioHorario_2.atYear(LocalDate.now().getYear())
                .atTime(horariosPorDia_2.getHoraInicio());

        LocalDateTime fimHorario_2 = diaDoMesFimHorario_2.atYear(inicioHorario_2.getYear())
                .atTime(horariosPorDia_2.getHoraFim());

        boolean conflitam = inicioHorario_1.isBefore(fimHorario_2)
                && fimHorario_1.isAfter(inicioHorario_2);

        return conflitam;
    }

    public boolean verificarConflitosComHorarioNaLista(CompromissosRecorrentesModel compromissosAtrelado,HorariosPorDiaModel horariosPorDia,List<HorariosPorDiaModel> listaHorariosPorDia){
        int i = 0;
        boolean haConflito = false;
        switch (compromissosAtrelado.getModoDeRecorrencia()){
            case FREQUENCIA_DIARIA -> {
                while (!haConflito && i<listaHorariosPorDia.size()){
                    HorariosPorDiaModel horarioAnalisado = listaHorariosPorDia.get(i);

                    haConflito = verificarConflitoEntreHorariosDiarios(horariosPorDia,horarioAnalisado);

                    i++;
                }
            }
            case FREQUENCIA_SEMANAL,PADRAO_RELATIVO_MENSAL -> {
                while (!haConflito && i<listaHorariosPorDia.size()){
                    HorariosPorDiaModel horarioAnalisado = listaHorariosPorDia.get(i);

                    haConflito = verificarConflitoEntreHorariosUsamDiasDaSemana(horariosPorDia,horarioAnalisado);
                    System.out.println("ha conflito dias da semana:" + haConflito);
                    i++;
                }
            }
            case DIA_ESPECIFICO_MENSAL -> {
                while (!haConflito && i<listaHorariosPorDia.size()){
                    HorariosPorDiaModel horarioAnalisado = listaHorariosPorDia.get(i);

                    haConflito = verificarConflitoEntreHorariosDiaEspecificoMensal(horariosPorDia,horarioAnalisado);

                    i++;
                }
            }
            case DATA_ESPECIFICA_ANUAL -> {
                while (!haConflito && i<listaHorariosPorDia.size()){
                    HorariosPorDiaModel horarioAnalisado = listaHorariosPorDia.get(i);

                    haConflito = verificarConflitoEntreHorariosDiaEspecificoAnual(horariosPorDia,horarioAnalisado);

                    i++;
                }
            }
        }
        return haConflito;
    }

}
