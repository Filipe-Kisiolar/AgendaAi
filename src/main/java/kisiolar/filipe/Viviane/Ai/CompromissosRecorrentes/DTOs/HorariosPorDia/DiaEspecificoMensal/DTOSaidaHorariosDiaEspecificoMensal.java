package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOSaidaHorariosPorDiaBase;

import java.time.LocalTime;

public class DTOSaidaHorariosDiaEspecificoMensal extends DTOSaidaHorariosPorDiaBase {

    private Integer inicioDiaEspecificoMes;

    private Integer fimDiaEspecificoMes;

    public DTOSaidaHorariosDiaEspecificoMensal() {
    }

    public DTOSaidaHorariosDiaEspecificoMensal(long id, LocalTime horaInicio, LocalTime horaFim, Integer inicioDiaEspecificoMes, Integer fimDiaEspecificoMes) {
        super(id, horaInicio, horaFim);
        this.inicioDiaEspecificoMes = inicioDiaEspecificoMes;
        this.fimDiaEspecificoMes = fimDiaEspecificoMes;
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
