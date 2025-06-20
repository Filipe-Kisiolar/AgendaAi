package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes;

import com.fasterxml.jackson.annotation.JsonInclude;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;

import java.util.List;

public class DTORespostaCompromissoRecorrente {

    private DTOSaidaCompromissosRecorrentes dtoSaidaCompromissosRecorrentes;
    private Boolean existeConflito;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DTOSaidaCompromissosRecorrentes> compromissosConflitantes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DTORespostaCompromisso> compromissosCriadosComConflito;

    public DTORespostaCompromissoRecorrente() {
    }

    public DTORespostaCompromissoRecorrente(DTOSaidaCompromissosRecorrentes dtoSaidaCompromissosRecorrentes) {
        this.dtoSaidaCompromissosRecorrentes = dtoSaidaCompromissosRecorrentes;
        this.existeConflito = false;
    }

    public DTORespostaCompromissoRecorrente(
            DTOSaidaCompromissosRecorrentes dtoSaidaCompromissosRecorrentes,
            List<DTOSaidaCompromissosRecorrentes> compromissosConflitantes,
            List<DTORespostaCompromisso> compromissosCriadosComConflito
    ) {
        this.dtoSaidaCompromissosRecorrentes = dtoSaidaCompromissosRecorrentes;
        this.compromissosConflitantes = compromissosConflitantes;
        this.compromissosCriadosComConflito = compromissosCriadosComConflito;
        this.existeConflito =
                (compromissosConflitantes != null && !compromissosConflitantes.isEmpty()) ||
                        (compromissosCriadosComConflito != null && !compromissosCriadosComConflito.isEmpty());
    }

    public static DTORespostaCompromissoRecorrente comConflitosRecorrentes(DTOSaidaCompromissosRecorrentes dto, List<DTOSaidaCompromissosRecorrentes> conflitos) {
        return new DTORespostaCompromissoRecorrente(dto, conflitos, null);
    }

    public static DTORespostaCompromissoRecorrente comConflitosGerados(DTOSaidaCompromissosRecorrentes dto, List<DTORespostaCompromisso> conflitosGerados) {
        return new DTORespostaCompromissoRecorrente(dto, null, conflitosGerados);
    }

    public DTOSaidaCompromissosRecorrentes getDtoCompromissosRecorrentes() {
        return dtoSaidaCompromissosRecorrentes;
    }

    public void setDtoCompromissosRecorrentes(DTOSaidaCompromissosRecorrentes dtoCriacaoCompromissosRecorrentes) {
        this.dtoSaidaCompromissosRecorrentes = dtoCriacaoCompromissosRecorrentes;
    }

    public Boolean getExisteConflito() {
        return existeConflito;
    }

    public void setExisteConflito(Boolean existeConflito) {
        this.existeConflito = existeConflito;
    }

    public List<DTOSaidaCompromissosRecorrentes> getCompromissosConflitantes() {
        return compromissosConflitantes;
    }

    public void setCompromissosConflitantes(List<DTOSaidaCompromissosRecorrentes> compromissosConflitantes) {
        this.compromissosConflitantes = compromissosConflitantes;
    }

    public List<DTORespostaCompromisso> getCompromissosCriadosComConflito() {
        return compromissosCriadosComConflito;
    }

    public void setCompromissosCriadosComConflito(List<DTORespostaCompromisso> compromissosCriadosComConflito) {
        this.compromissosCriadosComConflito = compromissosCriadosComConflito;
    }
}