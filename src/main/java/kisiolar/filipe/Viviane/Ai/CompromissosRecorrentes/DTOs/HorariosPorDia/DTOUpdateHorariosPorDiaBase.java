package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DataEspecificaAnual.DTOUpdateHorariosDataEspecificaAnual;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DiaEspecificoMensal.DTOUpdateHorariosDiaEspecificoMensal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaDiaria.DTOUpdateHorariosFrequenciaDiaria;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.FrequenciaSemanal.DTOUpdateHorariosFrequenciaSemanal;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.PadraoRelativoMensal.DTOUpdateHorariosPadraoRelativoMensal;

import java.time.LocalTime;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo" // ou "@type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DTOUpdateHorariosFrequenciaDiaria.class, name = "FREQUENCIA_DIARIA"),
        @JsonSubTypes.Type(value = DTOUpdateHorariosFrequenciaSemanal.class, name = "FREQUENCIA_SEMANAL"),
        @JsonSubTypes.Type(value = DTOUpdateHorariosPadraoRelativoMensal.class, name = "PADRAO_RELATIVO_MENSAL"),
        @JsonSubTypes.Type(value = DTOUpdateHorariosDiaEspecificoMensal.class, name = "DIA_ESPECIFICO_MENSAL"),
        @JsonSubTypes.Type(value = DTOUpdateHorariosDataEspecificaAnual.class, name = "DATA_ESPECIFICA_ANUAL")
})
public abstract class DTOUpdateHorariosPorDiaBase {

    private LocalTime horaInicio;

    private LocalTime horaFim;


    public DTOUpdateHorariosPorDiaBase() {
    }

    public DTOUpdateHorariosPorDiaBase(LocalTime horaInicio, LocalTime horaFim) {
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
