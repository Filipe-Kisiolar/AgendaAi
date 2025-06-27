package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia;

import java.time.LocalTime;

public abstract class DTOCreateHorariosPorDiaBase {

    private LocalTime horaInicio;

    private LocalTime horaFim;

    private long compromissoRecorrenteId;

    public DTOCreateHorariosPorDiaBase() {
    }

    public DTOCreateHorariosPorDiaBase(LocalTime horaInicio, LocalTime horaFim, long compromissoRecorrenteId) {
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.compromissoRecorrenteId = compromissoRecorrenteId;
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

    public long getCompromissoRecorrenteId() {
        return compromissoRecorrenteId;
    }

    public void setCompromissoRecorrenteId(long compromissoRecorrenteId) {
        this.compromissoRecorrenteId = compromissoRecorrenteId;
    }
}
