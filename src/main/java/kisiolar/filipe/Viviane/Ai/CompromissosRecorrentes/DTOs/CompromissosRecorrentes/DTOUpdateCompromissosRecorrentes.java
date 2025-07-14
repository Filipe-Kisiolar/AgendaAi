package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs.CompromissosRecorrentes;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;

public class DTOUpdateCompromissosRecorrentes {

    private String nome;

    private String descricao;

    private String local;

    @FutureOrPresent(message = "Data de inícioRecorrencia não pode ser no passado")
    private LocalDate dataInicioRecorrencia;

    @Future(message = "Data de fimRecorrencia precisa ser uma data futura")
    private LocalDate dataFimRecorrencia;

    private Integer intervalo;

    private boolean apenasDiasUteis;

    public DTOUpdateCompromissosRecorrentes() {
    }

    public DTOUpdateCompromissosRecorrentes(String nome, String descricao, String local, LocalDate dataInicioRecorrencia, LocalDate dataFimRecorrencia, Integer intervalo, boolean apenasDiasUteis) {
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
        this.dataInicioRecorrencia = dataInicioRecorrencia;
        this.dataFimRecorrencia = dataFimRecorrencia;
        this.intervalo = intervalo;
        this.apenasDiasUteis = apenasDiasUteis;
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

    public boolean isApenasDiasUteis() {
        return apenasDiasUteis;
    }

    public void setApenasDiasUteis(boolean apenasDiasUteis) {
        this.apenasDiasUteis = apenasDiasUteis;
    }
}