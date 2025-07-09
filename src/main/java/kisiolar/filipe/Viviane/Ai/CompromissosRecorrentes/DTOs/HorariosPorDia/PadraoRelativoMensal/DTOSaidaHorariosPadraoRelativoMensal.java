package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOSaidaHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOSaidaHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.OrdenamentoDaSemanaNoMesEnum;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DTOSaidaHorariosPadraoRelativoMensal extends DTOSaidaHorariosPorDiaBase {

    private DayOfWeek diaDaSemanaInicio;

    private DayOfWeek diaDaSemanaFim;

    private OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes;

    public DTOSaidaHorariosPadraoRelativoMensal() {
    }

    public DTOSaidaHorariosPadraoRelativoMensal(long id, LocalTime horaInicio, LocalTime horaFim, DayOfWeek diaDaSemanaInicio, DayOfWeek diaDaSemanaFim, OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes) {
        super(id, horaInicio, horaFim);
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
