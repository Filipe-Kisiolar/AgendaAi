package kisiolar.filipe.Viviane.Ai.Compromissos.DTOs;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record DTOCreateCompromissos (
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O nome é obrigatório")
        String descricao,

        String observacoes,

        String local,

        @NotNull(message = "Data de início é obrigatória")
        @FutureOrPresent(message = "Data de início não pode ser no passado")
        LocalDateTime inicio,

        @NotNull(message = "Data de fim é obrigatória")
        @FutureOrPresent(message = "Data de fim não pode ser no passado")
        LocalDateTime fim,

        Long compromissoRecorrenteId
) {
}