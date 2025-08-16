package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record DTORespostasListasCompromissoRecorrentes (

    List<DTOSaidaCompromissosRecorrentes> listaCompromissosRecorrentes,

    @JsonInclude(JsonInclude.Include.NON_NULL)
            List<List<DTOSaidaCompromissosRecorrentes>> compromissosConflitantes

) {

    @Override
    public String toString() {
        return String.format("""
                DTORespostasListasCompromissoRecorrentes{
                    listaCompromissosRecorrentes=%s
                    compromissosConflitantes=%s
                }
                """,
                listaCompromissosRecorrentes != null ? listaCompromissosRecorrentes : "null",
                compromissosConflitantes != null ? compromissosConflitantes : "null"
                );
    }
}
