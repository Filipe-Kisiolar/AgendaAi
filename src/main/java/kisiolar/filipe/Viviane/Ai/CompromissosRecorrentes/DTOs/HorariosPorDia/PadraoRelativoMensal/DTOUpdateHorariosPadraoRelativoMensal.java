package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOUpdateHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.OrdenamentoDaSemanaNoMesEnum;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DTOUpdateHorariosPadraoRelativoMensal extends DTOUpdateHorariosFrequenciaSemanal {

    private OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes;

    public DTOUpdateHorariosPadraoRelativoMensal() {
    }

    public DTOUpdateHorariosPadraoRelativoMensal( LocalTime horaInicio, LocalTime horaFim, DayOfWeek diaDaSemanaInicio, DayOfWeek diaDaSemanaFim, OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes) {
        super(horaInicio, horaFim, diaDaSemanaInicio, diaDaSemanaFim);
        this.ordenamentoDaSemanaNoMes = ordenamentoDaSemanaNoMes;
    }

    public OrdenamentoDaSemanaNoMesEnum getOrdenamentoDaSemanaNoMes() {
        return ordenamentoDaSemanaNoMes;
    }

    public void setOrdenamentoDaSemanaNoMes(OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes) {
        this.ordenamentoDaSemanaNoMes = ordenamentoDaSemanaNoMes;
    }
}
