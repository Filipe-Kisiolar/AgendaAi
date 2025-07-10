package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia.HorariosPorDiaModels;


import jakarta.persistence.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.OrdenamentoDaSemanaNoMesEnum;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "horarios_padrao_relativo_mensal")
@DiscriminatorValue("PADRAO_RELATIVO_MENSAL")
public class HorariosPadraoRelativoMensal extends HorariosPorDiaModel {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "ordenamento_da_semana_no_mes",nullable = false)
    private OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes;

    public HorariosPadraoRelativoMensal() {
    }

    public HorariosPadraoRelativoMensal(Long id, CompromissosRecorrentesModel compromissoRecorrente, DayOfWeek diaDaSemanaInicio, DayOfWeek diaDaSemanaFim, LocalTime horaInicio, LocalTime horaFim, OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes) {
        super(id, compromissoRecorrente);
        this.diaDaSemanaInicio = diaDaSemanaInicio;
        this.diaDaSemanaFim = diaDaSemanaFim;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.ordenamentoDaSemanaNoMes = ordenamentoDaSemanaNoMes;
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

    public OrdenamentoDaSemanaNoMesEnum getOrdenamentoDaSemanaNoMes() {
        return ordenamentoDaSemanaNoMes;
    }

    public void setOrdenamentoDaSemanaNoMes(OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes) {
        this.ordenamentoDaSemanaNoMes = ordenamentoDaSemanaNoMes;
    }
}
