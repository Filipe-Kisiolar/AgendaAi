package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDiaBase;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DTOCreateHorariosFrequenciaSemanal extends DTOCreateHorariosPorDiaBase {

    private DayOfWeek diaDaSemanaInicio;

    private DayOfWeek diaDaSemanaFim;

    public DTOCreateHorariosFrequenciaSemanal() {
    }

    public DTOCreateHorariosFrequenciaSemanal(LocalTime horaInicio, LocalTime horaFim, long compromissoRecorrenteId, DayOfWeek diaDaSemanaInicio, DayOfWeek diaDaSemanaFim) {
        super(horaInicio, horaFim, compromissoRecorrenteId);
        this.diaDaSemanaInicio = diaDaSemanaInicio;
        this.diaDaSemanaFim = diaDaSemanaFim;
    }

    public DayOfWeek getDiaDaSemanaInicio() {
        return diaDaSemanaInicio;
    }

    public void setDiaDaSemanaInicio(DayOfWeek diaDaSemanaInicio) {
        this.diaDaSemanaInicio = diaDaSemanaInicio;
    }

    public DayOfWeek getDiaDaSemanaFim() {
        return diaDaSemanaFim;
    }

    public void setDiaDaSemanaFim(DayOfWeek diaDaSemanaFim) {
        this.diaDaSemanaFim = diaDaSemanaFim;
    }
}
