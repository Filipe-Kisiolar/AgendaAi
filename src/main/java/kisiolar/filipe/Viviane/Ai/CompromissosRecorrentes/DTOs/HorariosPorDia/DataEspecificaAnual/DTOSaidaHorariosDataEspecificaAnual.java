package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DataEspecificaAnual;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOSaidaHorariosPorDiaBase;

import java.time.LocalTime;
import java.time.MonthDay;

public class DTOSaidaHorariosDataEspecificaAnual extends DTOSaidaHorariosPorDiaBase {

    private MonthDay inicioDataEspecificaDoAno;

    private MonthDay fimDataEspecificaDoAno;

    public DTOSaidaHorariosDataEspecificaAnual() {
    }

    public DTOSaidaHorariosDataEspecificaAnual(long id, LocalTime horaInicio, LocalTime horaFim, MonthDay inicioDataEspecificaDoAno, MonthDay fimDataEspecificaDoAno) {
        super(id, horaInicio, horaFim);
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
