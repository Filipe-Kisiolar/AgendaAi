package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;

import java.time.LocalTime;

@Entity
@Table(name = "horarios_frequencia_diaria")
public class HorariosFrequenciaDiaria extends HorariosPorDiaModel {

    @Column(name = "hora_de_inicio",nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_de_fim",nullable = false)
    private LocalTime horaFim;

    public HorariosFrequenciaDiaria() {
    }

    public HorariosFrequenciaDiaria(Long id, CompromissosRecorrentesModel compromissoRecorrente, LocalTime horaInicio, LocalTime horaFim) {
        super(id, compromissoRecorrente);
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
