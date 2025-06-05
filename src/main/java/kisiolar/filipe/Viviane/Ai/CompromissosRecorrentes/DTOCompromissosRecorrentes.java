package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes;

import kisiolar.filipe.Viviane.Ai.Compromissos.DTOSaidaCompromissos;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DTOCompromissosRecorrentes {

    private Long id;

    private String nome;

    private String descricao;

    private String local;

    private LocalTime horaInicial;

    private LocalTime horaFinal;

    private List<DayOfWeek> diasDaSemana;

    private LocalDate dataInicioRecorrencia;

    private LocalDate dataFimRecorrencia;

    private List<DTOSaidaCompromissos> compromissosGerados;

    public DTOCompromissosRecorrentes() {
    }

    public DTOCompromissosRecorrentes(Long id, String nome, String descricao, String local, LocalTime horaInicial, LocalTime horaFinal, List<DayOfWeek> diasDaSemana, LocalDate dataInicioRecorrencia, LocalDate dataFimRecorrencia, List<DTOSaidaCompromissos> compromissosGerados) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.diasDaSemana = diasDaSemana;
        this.dataInicioRecorrencia = dataInicioRecorrencia;
        this.dataFimRecorrencia = dataFimRecorrencia;
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

    public List<DTOSaidaCompromissos> getCompromissosGerados() {
        return compromissosGerados;
    }

    public void setCompromissosGerados(List<DTOSaidaCompromissos> compromissosGerados) {
        this.compromissosGerados = compromissosGerados;
    }
}