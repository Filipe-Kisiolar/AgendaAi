package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class DTORespostasListasCompromissoRecorrentes {

    List<DTOCompromissosRecorrentes> listaCompromissosRecorrentes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<List<DTOCompromissosRecorrentes>> compromissosConflitantes;

    public DTORespostasListasCompromissoRecorrentes() {
    }

    public DTORespostasListasCompromissoRecorrentes(List<DTOCompromissosRecorrentes> listaCompromissosRecorrentes) {
        this.listaCompromissosRecorrentes = listaCompromissosRecorrentes;
    }

    public DTORespostasListasCompromissoRecorrentes(List<DTOCompromissosRecorrentes> listaCompromissosRecorrentes, List<List<DTOCompromissosRecorrentes>> compromissosConflitantes) {
        this.listaCompromissosRecorrentes = listaCompromissosRecorrentes;
        this.compromissosConflitantes = compromissosConflitantes;
    }

    public List<DTOCompromissosRecorrentes> getListaCompromissosRecorrentes() {
        return listaCompromissosRecorrentes;
    }

    public void setListaCompromissosRecorrentes(List<DTOCompromissosRecorrentes> listaCompromissosRecorrentes) {
        this.listaCompromissosRecorrentes = listaCompromissosRecorrentes;
    }

    public List<List<DTOCompromissosRecorrentes>> getCompromissosConflitantes() {
        return compromissosConflitantes;
    }

    public void setCompromissosConflitantes(List<List<DTOCompromissosRecorrentes>> compromissosConflitantes) {
        this.compromissosConflitantes = compromissosConflitantes;
    }
}
