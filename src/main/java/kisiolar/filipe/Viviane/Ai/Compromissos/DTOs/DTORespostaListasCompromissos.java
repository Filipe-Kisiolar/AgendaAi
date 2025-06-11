package kisiolar.filipe.Viviane.Ai.Compromissos.DTOs;

import java.util.List;

public class DTORespostaListasCompromissos {

    private List<DTOSaidaCompromissos> listaCompromissos;

    private List<List<DTOSaidaCompromissos>> compromissosConflitantes;

    public DTORespostaListasCompromissos() {
    }

    public DTORespostaListasCompromissos(List<DTOSaidaCompromissos> listaCompromissos) {
        this.listaCompromissos = listaCompromissos;
    }

    public DTORespostaListasCompromissos(List<DTOSaidaCompromissos> listaCompromissos, List<List<DTOSaidaCompromissos>> compromissosConflitantes) {
        this.listaCompromissos = listaCompromissos;
        this.compromissosConflitantes = compromissosConflitantes;
    }

    public List<DTOSaidaCompromissos> getListaCompromissos() {
        return listaCompromissos;
    }

    public void setListaCompromissos(List<DTOSaidaCompromissos> listaCompromissos) {
        this.listaCompromissos = listaCompromissos;
    }

    public List<List<DTOSaidaCompromissos>> getCompromissosConflitantes() {
        return compromissosConflitantes;
    }

    public void setCompromissosConflitantes(List<List<DTOSaidaCompromissos>> compromissosConflitantes) {
        this.compromissosConflitantes = compromissosConflitantes;
    }
}
