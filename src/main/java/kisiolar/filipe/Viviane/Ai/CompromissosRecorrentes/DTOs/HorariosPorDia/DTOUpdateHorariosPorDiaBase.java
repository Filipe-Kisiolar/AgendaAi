package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia;

import java.time.LocalTime;

public abstract class DTOUpdateHorariosPorDiaBase {

    private LocalTime horaInicio;

    private LocalTime horaFim;


    public DTOUpdateHorariosPorDiaBase() {
    }

    public DTOUpdateHorariosPorDiaBase(LocalTime horaInicio, LocalTime horaFim) {
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

}
