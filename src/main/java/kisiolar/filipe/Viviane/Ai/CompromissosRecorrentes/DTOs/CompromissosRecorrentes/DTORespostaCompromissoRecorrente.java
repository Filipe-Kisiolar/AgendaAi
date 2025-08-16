package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes;

import com.fasterxml.jackson.annotation.JsonInclude;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTORespostaCompromisso;

import java.util.List;

public record DTORespostaCompromissoRecorrente (
    DTOSaidaCompromissosRecorrentes dtoSaidaCompromissosRecorrentes,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<DTOSaidaCompromissosRecorrentes> compromissosConflitantes,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<DTORespostaCompromisso> compromissosCriadosComConflito
) {

    @Override
    public String toString() {
        return String.format("""
        DTORespostaCompromissoRecorrente{
            dtoSaidaCompromissosRecorrentes=%s,
            existeConflito=%s,
            compromissosConflitantes=%s,
            compromissosCriadosComConflito=%s
        }
        """,
                dtoSaidaCompromissosRecorrentes != null ? dtoSaidaCompromissosRecorrentes : "null",
                compromissosConflitantes != null ? compromissosConflitantes : "null",
                compromissosCriadosComConflito != null ? compromissosCriadosComConflito : "null"
        );
    }
}