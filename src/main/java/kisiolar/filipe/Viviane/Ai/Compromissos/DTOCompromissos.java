package kisiolar.filipe.Viviane.Ai.Compromissos;

import kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.CompromissosRecorrentesModel;
import java.time.LocalDate;
import java.time.LocalTime;

public class DTOCompromissos {

    private Long id;

    private String nome;

    private String descricao;

    private String local;

    private LocalDate dia;

    private LocalTime horaInicial;

    private LocalTime horaFinal;

    private CompromissosRecorrentesModel compromissosRecorrente;

    public DTOCompromissos() {
    }

    public DTOCompromissos(Long id, String nome, String descricao, String local, LocalDate dia, LocalTime horaInicial, LocalTime horaFinal, CompromissosRecorrentesModel compromissosRecorrente) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
        this.dia = dia;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.compromissosRecorrente = compromissosRecorrente;
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

    public CompromissosRecorrentesModel getCompromissosRecorrente() {
        return compromissosRecorrente;
    }

    public void setCompromissosRecorrente(CompromissosRecorrentesModel compromissosRecorrente) {
        this.compromissosRecorrente = compromissosRecorrente;
    }
}