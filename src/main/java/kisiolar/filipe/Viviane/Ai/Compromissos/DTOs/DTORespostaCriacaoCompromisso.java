package kisiolar.filipe.Viviane.Ai.Compromissos.DTOs;

import java.util.List;

public class DTORespostaCriacaoCompromisso {

    private DTOSaidaCompromissos compromisso;
    private Boolean existeConflito;
    private List<DTOSaidaCompromissos> compromissosConflitantes;

    public DTORespostaCriacaoCompromisso() {
    }

    public DTORespostaCriacaoCompromisso(DTOSaidaCompromissos compromisso, List<DTOSaidaCompromissos> compromissosConflitantes) {
        this.compromisso = compromisso;
        this.existeConflito = !compromissosConflitantes.isEmpty();
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
}
