package kisiolar.filipe.Viviane.Ai.Compromissos.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record DTORespostaCompromisso(
        DTOSaidaCompromissos compromisso,

        Boolean existeConflito,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<DTOSaidaCompromissos> compromissosConflitantes
) {

    @Override
    public String toString() {
        return String.format("""
        DTORespostaCompromisso {
            compromisso=%s,
            existeConflito=%s,
            compromissosConflitantes=%s
        }
        """,
        compromisso != null ? compromisso : "null",
        existeConflito != null ? existeConflito : "null",
        compromissosConflitantes != null ? compromissosConflitantes : "null"
        );
    }
}
