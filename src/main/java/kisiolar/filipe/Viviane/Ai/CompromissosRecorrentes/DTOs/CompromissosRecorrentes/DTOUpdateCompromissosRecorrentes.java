package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.ModoDeRecorrenciaEnum;
import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.Enums.OrdenamentoDaSemanaNoMesEnum;

import java.time.LocalDate;

public class DTOUpdateCompromissosRecorrentes {

    private Long id;

    private String nome;

    private String descricao;

    private String local;

    private LocalDate dataInicioRecorrencia;

    private LocalDate dataFimRecorrencia;

    private Integer intervalo;

    private ModoDeRecorrenciaEnum modoDeRecorrencia;

    private boolean apenasDiasUteis;

    public DTOUpdateCompromissosRecorrentes() {
    }

    public DTOUpdateCompromissosRecorrentes(Long id, String nome, String descricao, String local, LocalDate dataInicioRecorrencia, LocalDate dataFimRecorrencia, Integer intervalo, ModoDeRecorrenciaEnum modoDeRecorrencia, boolean apenasDiasUteis) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
        this.dataInicioRecorrencia = dataInicioRecorrencia;
        this.dataFimRecorrencia = dataFimRecorrencia;
        this.intervalo = intervalo;
        this.modoDeRecorrencia = modoDeRecorrencia;
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
}