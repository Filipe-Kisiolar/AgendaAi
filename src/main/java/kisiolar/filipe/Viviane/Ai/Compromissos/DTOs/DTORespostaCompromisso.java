package kisiolar.filipe.Viviane.Ai.Compromissos.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class DTORespostaCompromisso {

    private DTOSaidaCompromissos compromisso;

    private Boolean existeConflito;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DTOSaidaCompromissos> compromissosConflitantes;

    public DTORespostaCompromisso() {
    }

    public DTORespostaCompromisso(DTOSaidaCompromissos compromisso) {
        this.compromisso = compromisso;
        this.existeConflito = false;
    }

    public DTORespostaCompromisso(DTOSaidaCompromissos compromisso, List<DTOSaidaCompromissos> compromissosConflitantes) {
        this.compromisso = compromisso;
        this.existeConflito = compromissosConflitantes != null && !compromissosConflitantes.isEmpty();
        this.compromissosConflitantes = compromissosConflitantes;
    }

    public DTOSaidaCompromissos getCompromisso() {
        return compromisso;
    }

    public void setCompromisso(DTOSaidaCompromissos compromisso) {
        this.compromisso = compromisso;
    }

    public Boolean getExisteConflito() {
        return existeConflito;
    }

    public void setExisteConflito(Boolean existeConflito) {
        this.existeConflito = existeConflito;
    }

    public List<DTOSaidaCompromissos> getCompromissosConflitantes() {
        return compromissosConflitantes;
    }

    public void setCompromissosConflitantes(List<DTOSaidaCompromissos> compromissosConflitantes) {
        this.compromissosConflitantes = compromissosConflitantes;
    }

    @Override
    public String toString() {
        return "DTORespostaCompromisso{" +
                "compromisso=" + compromisso.toString() +
                ", existeConflito=" + existeConflito.toString() +
                ", compromissosConflitantes=" + compromissosConflitantes.toString() +
                '}';
    }
}
