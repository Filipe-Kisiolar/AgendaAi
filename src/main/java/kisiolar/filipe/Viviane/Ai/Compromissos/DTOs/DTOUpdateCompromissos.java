package kisiolar.filipe.Viviane.Ai.Compromissos.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;

public class DTOUpdateCompromissos {

    private String nome;

    private String descricao;

    private String local;

    private LocalDate dia;

    private LocalTime horaInicial;

    private LocalTime horaFinal;

    public DTOUpdateCompromissos() {
    }

    public DTOUpdateCompromissos(String nome, String descricao, String local, LocalDate dia, LocalTime horaInicial, LocalTime horaFinal) {
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
        this.dia = dia;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;

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

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
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
}

