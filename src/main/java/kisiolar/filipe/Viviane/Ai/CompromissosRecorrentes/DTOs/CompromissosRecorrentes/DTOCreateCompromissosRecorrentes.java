package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOCreateHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum;

import java.time.LocalDate;
import java.util.List;

public record DTOCreateCompromissosRecorrentes (
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        String descricao,

        @NotBlank(message = "O nome é obrigatório")
       String local,

        @Size(min = 1, message = "É necessário ao menos um horário.")
        @Valid
        List<DTOCreateHorariosPorDiaBase> horariosPorDia,

        @NotNull(message = "Data de início da recorrência é obrigatória")
        @FutureOrPresent(message = "Data de inícioRecorrencia não pode ser no passado")
        LocalDate dataInicioRecorrencia,

        @NotNull(message = "Data de fim da recorrência é obrigatória")
        @Future(message = "Data de fimRecorrencia precisa ser uma data futura")
        LocalDate dataFimRecorrencia,

        @NotNull(message = "O intervalo é obrigatório")
        Integer intervalo,

        @NotNull(message = "O tipo de frequência é obrigatório")
        ModoDeRecorrenciaEnum modoDeRecorrencia,

        boolean apenasDiasUteis
) {

        public @Size(min = 1, message = "É necessário ao menos um horário.") @Valid List<DTOCreateHorariosPorDiaBase> getHorariosPorDia() {
                return horariosPorDia;
        }
}