package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;

public record DTOUpdateCompromissosRecorrentes (
        String nome,

        String descricao,

        String local,

        @FutureOrPresent(message = "Data de inícioRecorrencia não pode ser no passado")
        LocalDate dataInicioRecorrencia,

        @Future(message = "Data de fimRecorrencia precisa ser uma data futura")
        LocalDate dataFimRecorrencia,

        Integer intervalo,

        boolean apenasDiasUteis
) {
}