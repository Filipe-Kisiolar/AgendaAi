package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDiaBase;

import java.time.LocalTime;

public class DTOCreateHorariosDiaEspecificoMensal extends DTOCreateHorariosPorDiaBase {

    private Integer inicioDiaEspecificoMes;

    private Integer fimDiaEspecificoMes;

    public DTOCreateHorariosDiaEspecificoMensal() {
    }

    public DTOCreateHorariosDiaEspecificoMensal(LocalTime horaInicio, LocalTime horaFim, long compromissoRecorrenteId, Integer inicioDiaEspecificoMes, Integer fimDiaEspecificoMes) {
        super(horaInicio, horaFim, compromissoRecorrenteId);
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
