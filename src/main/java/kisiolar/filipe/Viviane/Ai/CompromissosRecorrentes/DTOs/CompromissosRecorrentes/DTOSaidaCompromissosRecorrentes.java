package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes;

import com.fasterxml.jackson.annotation.JsonInclude;
import kisiolar.filipe.Viviane.Ai.Compromissos.DTOs.DTOSaidaCompromissos;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.HorariosPorDia.DTOSaidaHorariosPorDiaBase;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.OrdenamentoDaSemanaNoMesEnum;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOSaidaCompromissosRecorrentes {

    private Long id;

    private String nome;

    private String descricao;

    private String local;

    private List<DTOSaidaHorariosPorDiaBase> horariosPorDia;

    private LocalDate dataInicioRecorrencia;

    private LocalDate dataFimRecorrencia;

    private Integer intervalo;

    private ModoDeRecorrenciaEnum modoDeRecorrencia;

    private boolean apenasDiasUteis;

    private List<DTOSaidaCompromissos> compromissosGerados;

    public DTOSaidaCompromissosRecorrentes() {
    }

    public DTOSaidaCompromissosRecorrentes(Long id, String nome, String descricao, String local, List<DTOSaidaHorariosPorDiaBase> horariosPorDia, LocalDate dataInicioRecorrencia, LocalDate dataFimRecorrencia, Integer intervalo, ModoDeRecorrenciaEnum modoDeRecorrencia, boolean apenasDiasUteis, List<DTOSaidaCompromissos> compromissosGerados) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
        this.horariosPorDia = horariosPorDia;
        this.dataInicioRecorrencia = dataInicioRecorrencia;
        this.dataFimRecorrencia = dataFimRecorrencia;
        this.intervalo = intervalo;
        this.modoDeRecorrencia = modoDeRecorrencia;
        this.apenasDiasUteis = apenasDiasUteis;
        this.compromissosGerados = compromissosGerados;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public List<DTOSaidaHorariosPorDiaBase> getHorariosPorDia() {
        return horariosPorDia;
    }

    public void setHorariosPorDia(List<DTOSaidaHorariosPorDiaBase> horariosPorDia) {
        this.horariosPorDia = horariosPorDia;
    }

    public LocalDate getDataInicioRecorrencia() {
        return dataInicioRecorrencia;
    }

    public void setDataInicioRecorrencia(LocalDate dataInicioRecorrencia) {
        this.dataInicioRecorrencia = dataInicioRecorrencia;
    }

    public LocalDate getDataFimRecorrencia() {
        return dataFimRecorrencia;
    }

    public void setDataFimRecorrencia(LocalDate dataFimRecorrencia) {
        this.dataFimRecorrencia = dataFimRecorrencia;
    }

    public Integer getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(Integer intervalo) {
        this.intervalo = intervalo;
    }

    public ModoDeRecorrenciaEnum getModoDeRecorrencia() {
        return modoDeRecorrencia;
    }

    public void setModoDeRecorrencia(ModoDeRecorrenciaEnum modoDeRecorrencia) {
        this.modoDeRecorrencia = modoDeRecorrencia;
    }

    public boolean isApenasDiasUteis() {
        return apenasDiasUteis;
    }

    public void setApenasDiasUteis(boolean apenasDiasUteis) {
        this.apenasDiasUteis = apenasDiasUteis;
    }

    public List<DTOSaidaCompromissos> getCompromissosGerados() {
        return compromissosGerados;
    }

    public void setCompromissosGerados(List<DTOSaidaCompromissos> compromissosGerados) {
        this.compromissosGerados = compromissosGerados;
    }

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