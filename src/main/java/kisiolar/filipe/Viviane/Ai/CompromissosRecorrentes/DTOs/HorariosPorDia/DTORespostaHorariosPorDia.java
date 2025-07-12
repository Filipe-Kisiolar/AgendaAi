package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia;

import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;

import java.util.List;

public class DTORespostaHorariosPorDia {

    public DTOSaidaHorariosPorDiaBase horario;

    public List<DTORespostaCompromisso> compromissosCriados;

    public DTORespostaHorariosPorDia() {
    }

    public DTORespostaHorariosPorDia(DTOSaidaHorariosPorDiaBase horario, List<DTORespostaCompromisso> compromissosCriados) {
        this.horario = horario;
        this.compromissosCriados = compromissosCriados;
    }

    public DTOSaidaHorariosPorDiaBase getHorario() {
        return horario;
    }

    public void setHorario(DTOSaidaHorariosPorDiaBase horario) {
        this.horario = horario;
    }

    public List<DTORespostaCompromisso> getCompromissosCriados() {
        return compromissosCriados;
    }

    public void setCompromissosCriados(List<DTORespostaCompromisso> compromissosCriados) {
        this.compromissosCriados = compromissosCriados;
    }
}
