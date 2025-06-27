package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOUpdateHorariosPorDiaBase;

import java.time.LocalTime;

public class DTOUpdateHorariosDiaEspecificoMensal extends DTOUpdateHorariosPorDiaBase {

    private Integer inicioDiaEspecificoMes;

    private Integer fimDiaEspecificoMes;

    public DTOUpdateHorariosDiaEspecificoMensal() {
    }

    public DTOUpdateHorariosDiaEspecificoMensal(LocalTime horaInicio, LocalTime horaFim, Integer inicioDiaEspecificoMes, Integer fimDiaEspecificoMes) {
        super(horaInicio, horaFim);
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
