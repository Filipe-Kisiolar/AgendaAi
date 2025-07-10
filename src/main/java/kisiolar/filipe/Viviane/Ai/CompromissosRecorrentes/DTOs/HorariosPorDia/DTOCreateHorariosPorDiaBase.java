package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DataEspecificaAnual.DTOCreateHorariosDataEspecificaAnual;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal.DTOCreateHorariosDiaEspecificoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaDiaria.DTOCreateHorariosFrequenciaDiaria;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOCreateHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal.DTOCreateHorariosPadraoRelativoMensal;

import java.time.LocalTime;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo" // ou "@type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DTOCreateHorariosFrequenciaDiaria.class, name = "FREQUENCIA_DIARIA"),
        @JsonSubTypes.Type(value = DTOCreateHorariosFrequenciaSemanal.class, name = "FREQUENCIA_SEMANAL"),
        @JsonSubTypes.Type(value = DTOCreateHorariosPadraoRelativoMensal.class, name = "PADRAO_RELATIVO_MENSAL"),
        @JsonSubTypes.Type(value = DTOCreateHorariosDiaEspecificoMensal.class, name = "DIA_ESPECIFICO_MENSAL"),
        @JsonSubTypes.Type(value = DTOCreateHorariosDataEspecificaAnual.class, name = "DATA_ESPECIFICA_ANUAL")
})
public abstract class DTOCreateHorariosPorDiaBase {

    private LocalTime horaInicio;

    private LocalTime horaFim;

    public DTOCreateHorariosPorDiaBase() {
    }

    public DTOCreateHorariosPorDiaBase(LocalTime horaInicio, LocalTime horaFim) {
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }
}
