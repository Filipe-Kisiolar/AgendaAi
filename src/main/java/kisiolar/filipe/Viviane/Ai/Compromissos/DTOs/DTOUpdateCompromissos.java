package kisiolar.filipe.Viviane.Ai.Compromissos.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DTOUpdateCompromissos {

    private String nome;

    private String descricao;

    private String local;

    private LocalDateTime inicio;

    private LocalDateTime fim;

    public DTOUpdateCompromissos() {
    }

    public DTOUpdateCompromissos(String nome, String descricao, String local, LocalDateTime inicio, LocalDateTime fim) {
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
        this.inicio = inicio;
        this.fim = fim;
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

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }
}

