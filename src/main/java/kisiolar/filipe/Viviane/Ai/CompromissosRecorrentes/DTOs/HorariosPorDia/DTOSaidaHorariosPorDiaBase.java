package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia;

import java.time.LocalTime;

public abstract class DTOSaidaHorariosPorDiaBase {
    private long id;

    private LocalTime horaInicio;

    private LocalTime horaFim;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DTOSaidaHorariosPorDiaBase() {
    }

    public DTOSaidaHorariosPorDiaBase(long id, LocalTime horaInicio, LocalTime horaFim) {
        this.id = id;
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
