package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;

import java.util.List;

public class DTORespostaCompromissoRecorrente {

    private DTOCompromissosRecorrentes dtoCompromissosRecorrentes;
    private Boolean existeConflito;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DTOCompromissosRecorrentes> compromissosConflitantes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DTORespostaCompromisso> compromissosCriadosComConflito;

    public DTORespostaCompromissoRecorrente() {
    }

    public DTORespostaCompromissoRecorrente(DTOCompromissosRecorrentes dtoCompromissosRecorrentes) {
        this.dtoCompromissosRecorrentes = dtoCompromissosRecorrentes;
        this.existeConflito = false;
    }

    public DTORespostaCompromissoRecorrente(
            DTOCompromissosRecorrentes dtoCompromissosRecorrentes,
            List<DTOCompromissosRecorrentes> compromissosConflitantes,
            List<DTORespostaCompromisso> compromissosCriadosComConflito
    ) {
        this.dtoCompromissosRecorrentes = dtoCompromissosRecorrentes;
        this.compromissosConflitantes = compromissosConflitantes;
        this.compromissosCriadosComConflito = compromissosCriadosComConflito;
        this.existeConflito =
                (compromissosConflitantes != null && !compromissosConflitantes.isEmpty()) ||
                        (compromissosCriadosComConflito != null && !compromissosCriadosComConflito.isEmpty());
    }

    public static DTORespostaCompromissoRecorrente comConflitosRecorrentes(DTOCompromissosRecorrentes dto, List<DTOCompromissosRecorrentes> conflitos) {
        return new DTORespostaCompromissoRecorrente(dto, conflitos, null);
    }

    public static DTORespostaCompromissoRecorrente comConflitosGerados(DTOCompromissosRecorrentes dto, List<DTORespostaCompromisso> conflitosGerados) {
        return new DTORespostaCompromissoRecorrente(dto, null, conflitosGerados);
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

    public List<DTORespostaCompromisso> getCompromissosCriadosComConflito() {
        return compromissosCriadosComConflito;
    }

    public void setCompromissosCriadosComConflito(List<DTORespostaCompromisso> compromissosCriadosComConflito) {
        this.compromissosCriadosComConflito = compromissosCriadosComConflito;
    }
}