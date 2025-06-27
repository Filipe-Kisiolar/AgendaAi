package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOSaidaHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.OrdenamentoDaSemanaNoMesEnum;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DTOSaidaHorariosPadraoRelativoMensal extends DTOSaidaHorariosFrequenciaSemanal {

    private OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes;

    public DTOSaidaHorariosPadraoRelativoMensal() {
    }

    public DTOSaidaHorariosPadraoRelativoMensal(long id, LocalTime horaInicio, LocalTime horaFim, DayOfWeek diaDaSemanaInicio, DayOfWeek diaDaSemanaFim, OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes) {
        super(id, horaInicio, horaFim, diaDaSemanaInicio, diaDaSemanaFim);
        this.ordenamentoDaSemanaNoMes = ordenamentoDaSemanaNoMes;
    }

    public OrdenamentoDaSemanaNoMesEnum getOrdenamentoDaSemanaNoMes() {
        return ordenamentoDaSemanaNoMes;
    }

    public void setOrdenamentoDaSemanaNoMes(OrdenamentoDaSemanaNoMesEnum ordenamentoDaSemanaNoMes) {
        this.ordenamentoDaSemanaNoMes = ordenamentoDaSemanaNoMes;
    }
}
