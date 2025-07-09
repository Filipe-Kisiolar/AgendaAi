package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.OrdenamentoDaSemanaNoMesEnum;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DTOCreateHorariosPadraoRelativoMensal extends DTOCreateHorariosPorDiaBase {

    private DayOfWeek diaDaSemanaInicio;

    private DayOfWeek diaDaSemanaFim;

    private OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes;

    public DTOCreateHorariosPadraoRelativoMensal() {
    }

    public DTOCreateHorariosPadraoRelativoMensal(LocalTime horaInicio, LocalTime horaFim, DayOfWeek diaDaSemanaInicio, DayOfWeek diaDaSemanaFim, OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes) {
        super(horaInicio, horaFim);
        this.diaDaSemanaInicio = diaDaSemanaInicio;
        this.diaDaSemanaFim = diaDaSemanaFim;
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

    public OrdenamentoDaSemanaNoMesEnum getOrdenamentoDaSemanaNoMes() {
        return ordenamentoDaSemanaNoMes;
    }

    public void setOrdenamentoDaSemanaNoMes(OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes) {
        this.ordenamentoDaSemanaNoMes = ordenamentoDaSemanaNoMes;
    }
}
