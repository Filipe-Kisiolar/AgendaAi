package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOUpdateHorariosPorDiaBase;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DTOUpdateHorariosFrequenciaSemanal extends DTOUpdateHorariosPorDiaBase {

    private DayOfWeek diaDaSemanaInicio;

    private DayOfWeek diaDaSemanaFim;

    public DTOUpdateHorariosFrequenciaSemanal() {
    }

    public DTOUpdateHorariosFrequenciaSemanal(LocalTime horaInicio, LocalTime horaFim, DayOfWeek diaDaSemanaInicio, DayOfWeek diaDaSemanaFim) {
        super(horaInicio, horaFim);
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
