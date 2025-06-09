package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.DTOs;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DTOUpdateCompromissosRecorrentes {

    private String nome;

    private String descricao;

    private String local;

    private LocalTime horaInicial;

    private LocalTime horaFinal;

    private List<DayOfWeek> diasDaSemana;

    private LocalDate dataInicioRecorrencia;

    private LocalDate dataFimRecorrencia;

    public DTOUpdateCompromissosRecorrentes() {
    }

    public DTOUpdateCompromissosRecorrentes(String nome, LocalDate dataFimRecorrencia, LocalDate dataInicioRecorrencia, LocalTime horaFinal, List<DayOfWeek> diasDaSemana, LocalTime horaInicial, String local, String descricao) {
        this.nome = nome;
        this.dataFimRecorrencia = dataFimRecorrencia;
        this.dataInicioRecorrencia = dataInicioRecorrencia;
        this.horaFinal = horaFinal;
        this.diasDaSemana = diasDaSemana;
        this.horaInicial = horaInicial;
        this.local = local;
        this.descricao = descricao;
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

    public LocalTime getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(LocalTime horaInicial) {
        this.horaInicial = horaInicial;
    }

    public LocalTime getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }

    public List<DayOfWeek> getDiasDaSemana() {
        return diasDaSemana;
    }

    public void setDiasDaSemana(List<DayOfWeek> diasDaSemana) {
        this.diasDaSemana = diasDaSemana;
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
}
