package kisiolar.filipe.Viviane.Ai.Compromissos.DTOs;

import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDateTime;

public record DTOUpdateCompromissos(

        String nome,

        String descricao,

        String observacoes,

        String local,

        @FutureOrPresent(message = "Data de início não pode ser no passado")
        LocalDateTime inicio,

        @FutureOrPresent(message = "Data de fim não pode ser no passado")
        LocalDateTime fim
) {
}