package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs;

import java.util.List;

public class DTORespostaCompromissoRecorrente {

    private DTOCompromissosRecorrentes dtoCompromissosRecorrentes;
    private Boolean existeConflito;
    private List<DTOCompromissosRecorrentes> compromissosConflitantes;

    public DTORespostaCompromissoRecorrente() {
    }

    public DTORespostaCompromissoRecorrente(DTOCompromissosRecorrentes dtoCompromissosRecorrentes) {
        this.dtoCompromissosRecorrentes = dtoCompromissosRecorrentes;
    }

    public DTORespostaCompromissoRecorrente(DTOCompromissosRecorrentes dtoCompromissosRecorrentes, List<DTOCompromissosRecorrentes> compromissosConflitantes) {
        this.dtoCompromissosRecorrentes = dtoCompromissosRecorrentes;
        this.existeConflito = !compromissosConflitantes.isEmpty();
        this.compromissosConflitantes = compromissosConflitantes;
    }

    public DTOCompromissosRecorrentes getDtoCompromissosRecorrentes() {
        return dtoCompromissosRecorrentes;
    }

    public void setDtoCompromissosRecorrentes(DTOCompromissosRecorrentes dtoCompromissosRecorrentes) {
        this.dtoCompromissosRecorrentes = dtoCompromissosRecorrentes;
    }

    public Boolean getExisteConflito() {
        return existeConflito;
    }

    public void setExisteConflito(Boolean existeConflito) {
        this.existeConflito = existeConflito;
    }

    public List<DTOCompromissosRecorrentes> getCompromissosConflitantes() {
        return compromissosConflitantes;
    }

    public void setCompromissosConflitantes(List<DTOCompromissosRecorrentes> compromissosConflitantes) {
        this.compromissosConflitantes = compromissosConflitantes;
    }
}
