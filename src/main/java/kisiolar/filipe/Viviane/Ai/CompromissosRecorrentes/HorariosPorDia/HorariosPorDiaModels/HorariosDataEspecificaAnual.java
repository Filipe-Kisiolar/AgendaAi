package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels;

import jakarta.persistence.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;

import java.time.LocalTime;
import java.time.MonthDay;

@Entity
@Table(name = "horarios_data_especifica_anual")
public class HorariosDataEspecificaAnual extends HorariosPorDiaModel {

    @Convert(converter = MonthDayConverter.class)
    @Column(name = "inicio_data_especifica_do_ano",nullable = false)
    private MonthDay inicioDataEspecificaDoAno;

    @Convert(converter = MonthDayConverter.class)
    @Column(name = "fim_data_especifica_do_ano",nullable = false)
    private MonthDay fimDataEspecificaDoAno;

    @Column(name = "hora_de_inicio",nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_de_fim",nullable = false)
    private LocalTime horaFim;

    public HorariosDataEspecificaAnual() {
    }

    public HorariosDataEspecificaAnual(Long id, CompromissosRecorrentesModel compromissoRecorrente, MonthDay inicioDataEspecificaDoAno, MonthDay fimDataEspecificaDoAno, LocalTime horaInicio, LocalTime horaFim) {
        super(id, compromissoRecorrente);
        this.inicioDataEspecificaDoAno = inicioDataEspecificaDoAno;
        this.fimDataEspecificaDoAno = fimDataEspecificaDoAno;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public MonthDay getInicioDataEspecificaDoAno() {
        return inicioDataEspecificaDoAno;
    }

    public void setInicioDataEspecificaDoAno(MonthDay inicioDataEspecificaDoAno) {
        this.inicioDataEspecificaDoAno = inicioDataEspecificaDoAno;
    }

    public MonthDay getFimDataEspecificaDoAno() {
        return fimDataEspecificaDoAno;
    }

    public void setFimDataEspecificaDoAno(MonthDay fimDataEspecificaDoAno) {
        this.fimDataEspecificaDoAno = fimDataEspecificaDoAno;
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
