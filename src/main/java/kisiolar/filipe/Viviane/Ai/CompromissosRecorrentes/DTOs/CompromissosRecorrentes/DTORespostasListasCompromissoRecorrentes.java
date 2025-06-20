package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOSaidaCompromissos;

import java.util.List;

public class DTORespostasListasCompromissoRecorrentes {

    List<DTOSaidaCompromissosRecorrentes> listaCompromissosRecorrentes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<List<DTOSaidaCompromissosRecorrentes>> compromissosConflitantes;

    public DTORespostasListasCompromissoRecorrentes() {
    }

    public DTORespostasListasCompromissoRecorrentes(List<DTOSaidaCompromissosRecorrentes> listaCompromissosRecorrentes) {
        this.listaCompromissosRecorrentes = listaCompromissosRecorrentes;
    }

    public DTORespostasListasCompromissoRecorrentes(List<DTOSaidaCompromissosRecorrentes> listaCompromissosRecorrentes, List<List<DTOSaidaCompromissosRecorrentes>> compromissosConflitantes) {
        this.listaCompromissosRecorrentes = listaCompromissosRecorrentes;
        this.compromissosConflitantes = compromissosConflitantes;
    }

    public List<DTOSaidaCompromissosRecorrentes> getListaCompromissosRecorrentes() {
        return listaCompromissosRecorrentes;
    }

    public void setListaCompromissosRecorrentes(List<DTOSaidaCompromissosRecorrentes> listaCompromissosRecorrentes) {
        this.listaCompromissosRecorrentes = listaCompromissosRecorrentes;
    }

    public List<List<DTOSaidaCompromissosRecorrentes>> getCompromissosConflitantes() {
        return compromissosConflitantes;
    }

    public void setCompromissosConflitantes(List<List<DTOSaidaCompromissosRecorrentes>> compromissosConflitantes) {
        this.compromissosConflitantes = compromissosConflitantes;
    }
}
