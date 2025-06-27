package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DataEspecificaAnual;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDiaBase;

import java.time.LocalTime;
import java.time.MonthDay;

public class DTOCreateHorariosDataEspecificaAnual extends DTOCreateHorariosPorDiaBase {

    private MonthDay inicioDataEspecificaDoAno;

    private MonthDay fimDataEspecificaDoAno;

    public DTOCreateHorariosDataEspecificaAnual() {
    }

    public DTOCreateHorariosDataEspecificaAnual(LocalTime horaInicio, LocalTime horaFim, long compromissoRecorrenteId, MonthDay inicioDataEspecificaDoAno, MonthDay fimDataEspecificaDoAno) {
        super(horaInicio, horaFim, compromissoRecorrenteId);
        this.inicioDataEspecificaDoAno = inicioDataEspecificaDoAno;
        this.fimDataEspecificaDoAno = fimDataEspecificaDoAno;
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
}
