package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia;

import jakarta.validation.constraints.AssertTrue;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.MonthDay;

public class DTOCreateHorariosPorDia {

    private DayOfWeek diaDaSemanaInicio;

    private LocalTime horaInicio;

    private DayOfWeek diaDaSemanaFim;

    private LocalTime horaFim;

    private MonthDay inicioDataEspecificaDoAno;

    private MonthDay fimDataEspecificaDoAno;

    private Integer inicioDiaEspecificoMes;

    private Integer fimDiaEspecificoMes;

    @AssertTrue(message = "Você deve preencher ou todos os campos padrões OU todos os campos de data específica.")
    public boolean isValid() {
        boolean usandoPadrao = diaDaSemanaInicio != null && diaDaSemanaFim != null && horaInicio != null && horaFim != null;
        boolean usandoDataEspecifica = inicioDataEspecificaDoAno != null && fimDataEspecificaDoAno != null;
        boolean usandoDiaEspecificoMes = inicioDiaEspecificoMes != null && fimDiaEspecificoMes != null
                && horaInicio != null && horaFim != null;
        boolean usandoTipoDeIntervaloDiario = horaInicio != null && horaFim != null;
        return usandoPadrao ^ usandoDataEspecifica ^ usandoDiaEspecificoMes ^ usandoTipoDeIntervaloDiario;
    }

    public DTOCreateHorariosPorDia() {
    }

    public DTOCreateHorariosPorDia(DayOfWeek diaDaSemanaInicio, LocalTime horaInicio, DayOfWeek diaDaSemanaFim, LocalTime horaFim, MonthDay inicioDataEspecificaDoAno, MonthDay fimDataEspecificaDoAno, Integer inicioDiaEspecificoMes, Integer fimDiaEspecificoMes) {
        this.diaDaSemanaInicio = diaDaSemanaInicio;
        this.horaInicio = horaInicio;
        this.diaDaSemanaFim = diaDaSemanaFim;
        this.horaFim = horaFim;
        this.inicioDataEspecificaDoAno = inicioDataEspecificaDoAno;
        this.fimDataEspecificaDoAno = fimDataEspecificaDoAno;
        this.inicioDiaEspecificoMes = inicioDiaEspecificoMes;
        this.fimDiaEspecificoMes = fimDiaEspecificoMes;
    }

    public DayOfWeek getDiaDaSemanaInicio() {
        return diaDaSemanaInicio;
    }

    public void setDiaDaSemanaInicio(DayOfWeek diaDaSemanaInicio) {
        this.diaDaSemanaInicio = diaDaSemanaInicio;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public DayOfWeek getDiaDaSemanaFim() {
        return diaDaSemanaFim;
    }

    public void setDiaDaSemanaFim(DayOfWeek diaDaSemanaFim) {
        this.diaDaSemanaFim = diaDaSemanaFim;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
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
}