package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.MonthDay;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOSaidaHorariosPorDia {

    private Long id;

    private DayOfWeek diaDaSemanaInicio;

    private LocalTime horaInicio;

    private DayOfWeek diaDaSemanaFim;

    private LocalTime horaFim;

    private MonthDay inicioDataEspecificaDoAno;

    private MonthDay fimDataEspecificaDoAno;

    private Integer inicioDiaEspecificoMes;

    private Integer fimDiaEspecificoMes;

    public DTOSaidaHorariosPorDia() {
    }

    public DTOSaidaHorariosPorDia(Long id, DayOfWeek diaDaSemanaInicio, LocalTime horaInicio, DayOfWeek diaDaSemanaFim, LocalTime horaFim, MonthDay inicioDataEspecificaDoAno, MonthDay fimDataEspecificaDoAno, Integer inicioDiaEspecificoMes, Integer fimDiaEspecificoMes) {
        this.id = id;
        this.diaDaSemanaInicio = diaDaSemanaInicio;
        this.horaInicio = horaInicio;
        this.diaDaSemanaFim = diaDaSemanaFim;
        this.horaFim = horaFim;
        this.inicioDataEspecificaDoAno = inicioDataEspecificaDoAno;
        this.fimDataEspecificaDoAno = fimDataEspecificaDoAno;
        this.inicioDiaEspecificoMes = inicioDiaEspecificoMes;
        this.fimDiaEspecificoMes = fimDiaEspecificoMes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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