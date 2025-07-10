package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels;

import jakarta.persistence.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "horarios_frequencia_semanal")
@DiscriminatorValue("SEMANAL")
public class HorariosFrequenciaSemanal extends HorariosPorDiaModel {

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_da_semana_inicio",nullable = false)
    private DayOfWeek diaDaSemanaInicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_da_semana_fim",nullable = false)
    private DayOfWeek diaDaSemanaFim;

    @Column(name = "hora_inicio",nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim",nullable = false)
    private LocalTime horaFim;

    public HorariosFrequenciaSemanal() {
    }

    public HorariosFrequenciaSemanal(Long id, CompromissosRecorrentesModel compromissoRecorrente, DayOfWeek diaDaSemanaInicio, DayOfWeek diaDaSemanaFim, LocalTime horaInicio, LocalTime horaFim) {
        super(id, compromissoRecorrente);
        this.diaDaSemanaInicio = diaDaSemanaInicio;
        this.diaDaSemanaFim = diaDaSemanaFim;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
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
