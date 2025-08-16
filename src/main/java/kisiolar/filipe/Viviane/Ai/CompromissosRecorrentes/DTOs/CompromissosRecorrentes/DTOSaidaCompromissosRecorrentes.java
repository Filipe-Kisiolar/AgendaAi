package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes;

import com.fasterxml.jackson.annotation.JsonInclude;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOSaidaCompromissos;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOSaidaHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DTOSaidaCompromissosRecorrentes (
    Long id,

    String nome,

    String descricao,

    String local,

    List<DTOSaidaHorariosPorDiaBase> horariosPorDia,

    LocalDate dataInicioRecorrencia,

    LocalDate dataFimRecorrencia,

    Integer intervalo,

    ModoDeRecorrenciaEnum modoDeRecorrencia,

    boolean apenasDiasUteis,

    List<DTOSaidaCompromissos> compromissosGerados
) {

    @Override
    public String toString() {
        return String.format("""
        DTOSaidaCompromissosRecorrentes {
            id=%s,
            nome='%s',
            descricao='%s',
            local='%s',
            horariosPorDia=%s,
            dataInicioRecorrencia=%s,
            dataFimRecorrencia=%s,
            intervalo=%s,
            modoDeRecorrencia=%s,
            apenasDiasUteis=%s,
            compromissosGerados=%s
        }
        """,
                id != null ? id : "null",
                nome != null ? nome : "",
                descricao != null ? descricao : "",
                local != null ? local : "",
                horariosPorDia != null ? horariosPorDia : "[]",
                dataInicioRecorrencia != null ? dataInicioRecorrencia : "null",
                dataFimRecorrencia != null ? dataFimRecorrencia : "null",
                intervalo != null ? intervalo : "null",
                modoDeRecorrencia != null ? modoDeRecorrencia : "null",
                apenasDiasUteis,
                compromissosGerados != null ? compromissosGerados : "[]"
        );
    }
}