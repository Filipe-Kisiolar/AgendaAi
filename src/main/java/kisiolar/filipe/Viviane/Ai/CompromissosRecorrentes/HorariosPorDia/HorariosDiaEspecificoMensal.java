package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;

import java.time.LocalTime;

@Entity
@Table(name = "horarios_dia_especifico_mensal")
public class HorariosDiaEspecificoMensal extends HorariosPorDiaModel{

    @Column(name = "inicio_dia_especifico_do_mes",nullable = false)
    private Integer inicioDiaEspecificoMes;

    @Column(name = "fim_dia_especifico_do_mes",nullable = false)
    private Integer fimDiaEspecificoMes;

    @Column(name = "hora_de_inicio",nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_de_fim",nullable = false)
    private LocalTime horaFim;

    public HorariosDiaEspecificoMensal() {
    }

    public HorariosDiaEspecificoMensal(Long id, CompromissosRecorrentesModel compromissoRecorrente, Integer inicioDiaEspecificoMes, Integer fimDiaEspecificoMes, LocalTime horaInicio, LocalTime horaFim) {
        super(id, compromissoRecorrente);
        this.inicioDiaEspecificoMes = inicioDiaEspecificoMes;
        this.fimDiaEspecificoMes = fimDiaEspecificoMes;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public Integer getInicioDiaEspecificoMes() {
        return inicioDiaEspecificoMes;
    }

    public void setInicioDiaEspecificoMes(Integer inicioDiaEspecificoMes) {
        this.inicioDiaEspecificoMes = inicioDiaEspecificoMes;
    }

    public Integer getFimDiaEspecificoMes() {
        return fimDiaEspecificoMes;
    }

    public void setFimDiaEspecificoMes(Integer fimDiaEspecificoMes) {
        this.fimDiaEspecificoMes = fimDiaEspecificoMes;
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
