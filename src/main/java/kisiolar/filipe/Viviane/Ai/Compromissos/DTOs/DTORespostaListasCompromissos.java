package kisiolar.filipe.Viviane.Ai.Compromissos.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record DTORespostaListasCompromissos (

    List<DTOSaidaCompromissos> listaCompromissos,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<List<DTOSaidaCompromissos>> compromissosConflitantes
) {

    @Override
    public String toString() {
        return String.format("""
        DTORespostaListasCompromissos {
            listaCompromissos=%s,
            compromissosConflitantes=%s
        }
        """,
        listaCompromissos != null ? listaCompromissos : "null",
        compromissosConflitantes != null ? compromissosConflitantes : "null"
        );
    }
}
