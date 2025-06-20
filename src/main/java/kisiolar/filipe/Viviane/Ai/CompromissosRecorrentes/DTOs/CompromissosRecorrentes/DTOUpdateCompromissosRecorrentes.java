package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.OrdenamentoDoDiaEnum;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.TipoDeFrequenciaFixaEnum;

import java.time.LocalDate;
import java.util.List;

public class DTOUpdateCompromissosRecorrentes {

    private Long id;

    private String nome;

    private String descricao;

    private String local;

    private List<DTOHorariosPorDia> horariosPorDias;

    private LocalDate dataInicioRecorrencia;

    private LocalDate dataFimRecorrencia;

    private Integer intervalo;

    private TipoDeFrequenciaFixaEnum tipoDeFrequenciaFixa;

    private ModoDeRecorrenciaEnum modoDeRecorrencia;

    private OrdenamentoDoDiaEnum ordenamentoDoDia;

    private boolean apenasDiasUteis;

    public DTOUpdateCompromissosRecorrentes() {
    }

    public DTOUpdateCompromissosRecorrentes(Long id, String nome, String descricao, String local, List<DTOHorariosPorDia> horariosPorDias, LocalDate dataInicioRecorrencia, LocalDate dataFimRecorrencia, Integer intervalo, TipoDeFrequenciaFixaEnum tipoDeFrequenciaFixa, ModoDeRecorrenciaEnum modoDeRecorrencia, OrdenamentoDoDiaEnum ordenamentoDoDia, boolean apenasDiasUteis) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
        this.horariosPorDias = horariosPorDias;
        this.dataInicioRecorrencia = dataInicioRecorrencia;
        this.dataFimRecorrencia = dataFimRecorrencia;
        this.intervalo = intervalo;
        this.tipoDeFrequenciaFixa = tipoDeFrequenciaFixa;
        this.modoDeRecorrencia = modoDeRecorrencia;
        this.ordenamentoDoDia = ordenamentoDoDia;
        this.apenasDiasUteis = apenasDiasUteis;
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

    public List<DTOHorariosPorDia> getHorariosPorDias() {
        return horariosPorDias;
    }

    public void setHorariosPorDias(List<DTOHorariosPorDia> horariosPorDias) {
        this.horariosPorDias = horariosPorDias;
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

    public TipoDeFrequenciaFixaEnum getTipoDeFrequenciaFixa() {
        return tipoDeFrequenciaFixa;
    }

    public void setTipoDeFrequenciaFixa(TipoDeFrequenciaFixaEnum tipoDeFrequenciaFixa) {
        this.tipoDeFrequenciaFixa = tipoDeFrequenciaFixa;
    }

    public ModoDeRecorrenciaEnum getModoDeRecorrencia() {
        return modoDeRecorrencia;
    }

    public void setModoDeRecorrencia(ModoDeRecorrenciaEnum modoDeRecorrencia) {
        this.modoDeRecorrencia = modoDeRecorrencia;
    }

    public OrdenamentoDoDiaEnum getOrdenamentoDoDia() {
        return ordenamentoDoDia;
    }

    public void setOrdenamentoDoDia(OrdenamentoDoDiaEnum ordenamentoDoDia) {
        this.ordenamentoDoDia = ordenamentoDoDia;
    }

    public boolean isApenasDiasUteis() {
        return apenasDiasUteis;
    }

    public void setApenasDiasUteis(boolean apenasDiasUteis) {
        this.apenasDiasUteis = apenasDiasUteis;
    }
}
